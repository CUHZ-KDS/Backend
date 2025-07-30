package com.moti.backend.core.show.infrastructure.cache;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import com.moti.backend.core.show.domain.type.SeatStatus;
import com.moti.backend.core.show.presentation.socket.dto.SeatInfoDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SeatCacheRepository {

	private final RedisTemplate<String, String> redisTemplate;
	// Redis의 Hash 자료구조를 다루기 위한 인터페이스
	private final HashOperations<String, String, String> hashOps;
	// Redis의 Set 자료구조를 다루기 위한 인터페이스
	private final SetOperations<String, String> setOps;
	private final RedissonClient redissonClient;

	@Autowired
	public SeatCacheRepository(RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient) {
		this.redisTemplate = redisTemplate;
		this.hashOps = redisTemplate.opsForHash();
		this.setOps = redisTemplate.opsForSet();
		this.redissonClient = redissonClient;
	}

	private static final String SEAT_SELECT_MEMBER_KEY = "seat:%d:select-member";
	private static final String SEAT_STATUS_KEY = "seat:%d:status";
	private static final String HOLD_SEAT_KEY = "hold:%d:%d";

	// atomic
	public Long select(Long showScheduleId, Long seatId, Long memberId) {
		String lockKey = String.format("lock:seat:%d:%d", showScheduleId, seatId);  // 좌석 단위로 락
		RLock lock = redissonClient.getLock(lockKey);

		try {
			lock.lock(5, TimeUnit.SECONDS);

			String statusKey = String.format(SEAT_STATUS_KEY, showScheduleId);
			String seatIdStr = String.valueOf(seatId);

			hashOps.put(statusKey, seatIdStr, String.valueOf(SeatStatus.SELECTED));
			return getCountAfterAddMember(showScheduleId, seatId, memberId);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	private Long getCountAfterAddMember(Long showScheduleId, Long seatId, Long memberId) {
		String key = String.format(SEAT_SELECT_MEMBER_KEY, showScheduleId);
		String seatIdStr = String.valueOf(seatId);
		String memberIdStr = String.valueOf(memberId);

		setOps.add(key + ":" + seatIdStr, memberIdStr);
		return setOps.size(key + ":" + seatIdStr);
	}

	// atomic
	public Long deselected(Long showScheduleId, Long seatId, Long memberId) {
		String lockKey = String.format("lock:seat:%d:%d", showScheduleId, seatId);  // 좌석 단위로 락
		RLock lock = redissonClient.getLock(lockKey);

		try {
			lock.lock(5, TimeUnit.SECONDS);

			String statusKey = String.format(SEAT_STATUS_KEY, showScheduleId);
			String seatIdStr = String.valueOf(seatId);

			Long count = getCountAfterRemoveMember(showScheduleId, seatId, memberId);
			if (count <= 0) {
				hashOps.put(statusKey, seatIdStr, String.valueOf(SeatStatus.AVAILABLE));
			}
			return count;
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	private Long getCountAfterRemoveMember(Long showScheduleId, Long seatId, Long memberId) {
		String key = String.format(SEAT_SELECT_MEMBER_KEY, showScheduleId);
		String seatIdStr = String.valueOf(seatId);
		String memberIdStr = String.valueOf(memberId);

		setOps.remove(key + ":" + seatIdStr, memberIdStr);
		return setOps.size(key + ":" + seatIdStr);
	}

	public void reserved(Long showScheduleId, Long[] seatIds) {
		String statusKey = String.format(SEAT_STATUS_KEY, showScheduleId);

		for (Long seatId : seatIds) {
			// 1. Hash에 좌석 상태를 'DISABLED'로 업데이트
			hashOps.put(statusKey, String.valueOf(seatId), String.valueOf(SeatStatus.DISABLED));

			// 2. HOLD TTL 상태 업데이트
			String holdKey = String.format(HOLD_SEAT_KEY, showScheduleId, seatId);
			redisTemplate.opsForValue().set(holdKey, "HELD", Duration.ofMinutes(10));
		}
	}

	// 공연 스케줄 생성 시, 호출
	public void initSeatsForShowSchedule(Long showScheduleId, Long[] seatIds) {
		String key = String.format(SEAT_STATUS_KEY, showScheduleId);
		for (Long seatId : seatIds) {
			// 모든 좌석을 AVAILABLE 상태로 초기화
			hashOps.put(key, String.valueOf(seatId), String.valueOf(SeatStatus.AVAILABLE));
		}
	}

	// 해당 공연의 UI 상태를 반환하는 로직(상태 + count)
	public List<SeatInfoDTO> getSeatsStatusForShowSchedule(Long showScheduleId) {
		String key = String.format(SEAT_STATUS_KEY, showScheduleId);
		Map<String, String> seatStatusMap = hashOps.entries(key);
		List<SeatInfoDTO> result = new ArrayList<>();

		// 해당 공연의 모든 좌석 상태를 가져오는 로직
		for (String seatId : seatStatusMap.keySet()) {
			System.out.println("seatId = " + seatId);
			String status = seatStatusMap.get(seatId);
			Long count = 0L;
			if (status.equals(SeatStatus.SELECTED.toString())) {
				count = getSeatsCountForShowSchedule(showScheduleId, Long.valueOf(seatId));
			}
			result.add(SeatInfoDTO.from(Long.valueOf(seatId), SeatStatus.valueOf(status), count));
		}

		return result;
	}

	// 해당 공연의 모든 좌석 선택된 인원 수를 가져오는 로직
	private Long getSeatsCountForShowSchedule(Long showScheduleId, Long seatId) {
		String key = String.format(SEAT_SELECT_MEMBER_KEY, showScheduleId) + ":" + seatId;
		return setOps.size(key);
	}

	//최적화 키워드: Pipeline
	public List<Long> getSelectedSeats(Long showScheduleId, Long memberId) {
		String key = String.format(SEAT_SELECT_MEMBER_KEY, showScheduleId);

		List<Long> seatIds = new ArrayList<>();
		Map<String, String> seatStatusMap = hashOps.entries(String.format(SEAT_STATUS_KEY, showScheduleId));
		for (String seatId : seatStatusMap.keySet()) {
			String seatMemberKey = key + ":" + seatId;
			if (setOps.isMember(seatMemberKey, String.valueOf(memberId))) {
				seatIds.add(Long.valueOf(seatId));
			}
		}
		return seatIds;
	}
}
