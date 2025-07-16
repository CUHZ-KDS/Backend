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

	// 이전 대화에서 논의한 타입별 전용 RedisTemplate을 주입받는 것이 가장 좋습니다.
	// 여기서는 설명을 위해 간단한 String 템플릿을 사용한다고 가정합니다.
	@Autowired
	public SeatCacheRepository(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.hashOps = redisTemplate.opsForHash();
		this.setOps = redisTemplate.opsForSet();
	}

	private static final String SEAT_STATUS_KEY = "seat:%d:status";
	private static final String HOLD_SEAT_KEY = "hold:%d:%d";

	public void select(Long showScheduleId, Long seatId) {
		String statusKey = String.format(SEAT_STATUS_KEY, showScheduleId);
		String seatIdStr = String.valueOf(seatId);

		// 1. Hash에 좌석 상태를 'SELECTED'로 업데이트
		hashOps.put(statusKey, seatIdStr, String.valueOf(SeatStatus.SELECTED));
	}

	public void deselected(Long showScheduleId, Long seatId) {
		String statusKey = String.format(SEAT_STATUS_KEY, showScheduleId);
		String seatIdStr = String.valueOf(seatId);

		// 1. Hash에 좌석 상태를 'AVAILABLE'로 업데이트
		hashOps.put(statusKey, seatIdStr, String.valueOf(SeatStatus.AVAILABLE));
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
