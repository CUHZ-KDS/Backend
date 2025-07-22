package com.moti.backend.core.show.infrastructure.cache;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import com.moti.backend.core.show.domain.type.SeatStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SeatCacheRepository {

	private final RedisTemplate<String, String> redisTemplate;
	// Redis의 Hash 자료구조를 다루기 위한 인터페이스
	private final HashOperations<String, String, String> hashOps;
	// Redis의 Set 자료구조를 다루기 위한 인터페이스
	private final SetOperations<String, String> setOps;

	@Autowired
	public SeatCacheRepository(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.hashOps = redisTemplate.opsForHash();
		this.setOps = redisTemplate.opsForSet();
	}

	private static final String SEAT_SELECT_MEMBER_KEY = "seat:%d:select-member";
	private static final String SEAT_STATUS_KEY = "seat:%d:status";
	private static final String HOLD_SEAT_KEY = "hold:%d:%d";

	// atomic
	public Long select(Long showScheduleId, Long seatId, Long memberId) {
		String statusKey = String.format(SEAT_STATUS_KEY, showScheduleId);
		String seatIdStr = String.valueOf(seatId);

		hashOps.put(statusKey, seatIdStr, String.valueOf(SeatStatus.SELECTED));
		return getCountAfterAddMember(showScheduleId, seatId, memberId);
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
		String statusKey = String.format(SEAT_STATUS_KEY, showScheduleId);
		String seatIdStr = String.valueOf(seatId);

		hashOps.put(statusKey, seatIdStr, String.valueOf(SeatStatus.AVAILABLE));
		return getCountAfterRemoveMember(showScheduleId, seatId, memberId);
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
}
