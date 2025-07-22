package com.moti.backend.core.show.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.moti.backend.core.place.infrastructure.persistence.SeatRepository;
import com.moti.backend.core.show.domain.type.EventType;
import com.moti.backend.core.show.domain.type.SeatStatus;
import com.moti.backend.core.show.infrastructure.cache.SeatCacheRepository;
import com.moti.backend.core.show.presentation.socket.SeatPublisher;
import com.moti.backend.core.show.presentation.socket.dto.SeatResponseDTO.SeatToggleResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatStatusService {

	private final SeatRepository seatRepository;
	private final SeatCacheRepository seatCacheRepository;
	private final SeatPublisher seatPublisher;

	public SeatToggleResponse selected(Long showScheduleId, Long seatId, Long memberId) {
		Long selectedCount = seatCacheRepository.select(showScheduleId, seatId, memberId);

		return SeatToggleResponse.from(
			EventType.SEAT_SELECTED,
			showScheduleId,
			seatId,
			SeatStatus.SELECTED,
			selectedCount,
			LocalDateTime.now()
		);
	}

	public SeatToggleResponse deselected(Long showScheduleId, Long seatId, Long memberId) {
		Long selectedCount = seatCacheRepository.deselected(showScheduleId, seatId, memberId);

		return SeatToggleResponse.from(
			EventType.SEAT_DESELECTED,
			showScheduleId,
			seatId,
			SeatStatus.AVAILABLE,
			selectedCount,
			LocalDateTime.now()
		);
	}

	public void reserved(Long showScheduleId, Long[] seatIds) {
		seatCacheRepository.reserved(showScheduleId, seatIds);
		seatPublisher.pubMessageByScheduleId(showScheduleId, seatIds);
	}
}
