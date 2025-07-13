package com.moti.backend.core.show.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.moti.backend.core.place.infrastructure.persistence.SeatRepository;
import com.moti.backend.core.show.infrastructure.cache.SeatCacheRepository;
import com.moti.backend.core.show.presentation.socket.EventType;
import com.moti.backend.core.show.presentation.socket.SeatPublisher;
import com.moti.backend.core.show.presentation.socket.SeatStatus;
import com.moti.backend.core.show.presentation.socket.dto.SeatResponseDTO.SeatToggleResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatStatusService {

	private final SeatRepository seatRepository;
	private final SeatCacheRepository seatCacheRepository;
	private final SeatPublisher seatPublisher;

	public SeatToggleResponse selected(Long showScheduleId, Long seatId) {
		// memberId를 사용하여 좌석을 선택한 사용자를 추적해야합니다.
		seatCacheRepository.select(showScheduleId, seatId);

		return SeatToggleResponse.from(
			EventType.SEAT_SELECTED,
			showScheduleId,
			seatId,
			SeatStatus.SELECTED,
			1,  // redis를 통해 조회한 선택된 좌석의 개수 추가 필요
			LocalDateTime.now()
		);
	}

	public SeatToggleResponse deselected(Long showScheduleId, Long seatId) {
		seatCacheRepository.deselected(showScheduleId, seatId);

		return SeatToggleResponse.from(
			EventType.SEAT_DESELECTED,// redis를 해당 좌석이 선택중인지 아닌지 확인 필요
			showScheduleId,
			seatId,
			SeatStatus.AVAILABLE,
			0,  // redis를 통해 조회한 선택된 좌석의 개수 추가 필요
			LocalDateTime.now()
		);
	}

	public void reserved(Long showScheduleId, Long[] seatIds) {
		seatCacheRepository.reserved(showScheduleId, seatIds);
		seatPublisher.pubMessageByScheduleId(showScheduleId, seatIds);
	}
}
