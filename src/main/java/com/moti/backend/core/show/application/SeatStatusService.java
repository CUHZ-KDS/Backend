package com.moti.backend.core.show.application;

import static com.moti.backend.core.show.presentation.socket.dto.SeatResponseDTO.*;
import static com.moti.backend.core.show.presentation.socket.dto.SessionInitResponseDTO.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.moti.backend.core.place.infrastructure.persistence.SeatRepository;
import com.moti.backend.core.show.domain.type.EventType;
import com.moti.backend.core.show.domain.type.SeatStatus;
import com.moti.backend.core.show.infrastructure.cache.SeatCacheRepository;
import com.moti.backend.core.show.presentation.socket.SeatPublisher;
import com.moti.backend.core.show.presentation.socket.dto.SeatInfoDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatStatusService {

	private final SeatRepository seatRepository;
	private final SeatCacheRepository seatCacheRepository;
	private final SeatPublisher seatPublisher;

	public List<SeatInfoDTO> getSeatStatusByShowScheduleId(Long showScheduleId) {
		return seatCacheRepository.getSeatsStatusForShowSchedule(showScheduleId);
	}

	public SeatSelection getSelectedAndHeldSeats(Long showScheduleId, Long memberId) {
		// 선택한 좌석들 조회
		List<Long> selectedSeats = seatCacheRepository.getSelectedSeats(showScheduleId, memberId);

		// todo: 좌석 선점기능 추가 후 변경 필요(현재는 더미 데이터 사용)
		HeldSeats heldSeats = HeldSeats.from(
			List.of(1L, 2L, 3L),
			"ORDER123",
			LocalDateTime.now(),
			300L,
			600L
		);
		return SeatSelection.from(selectedSeats, heldSeats);
	}

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
	// 공연 인스턴스 생성 -> redis 모든 좌석 available 설정하기
	// 초기데이터를 보냄 -> 2번째 구조 통으로 보내기

	public SeatToggleResponse deselected(Long showScheduleId, Long seatId, Long memberId) {
		Long selectedCount = seatCacheRepository.deselected(showScheduleId, seatId, memberId);

		return SeatToggleResponse.from(
			EventType.SEAT_DESELECTED,
			showScheduleId,
			seatId,
			selectedCount == 0 ? SeatStatus.AVAILABLE : SeatStatus.SELECTED,
			selectedCount,
			LocalDateTime.now()
		);
	}

	public void reserved(Long showScheduleId, Long[] seatIds) {
		seatCacheRepository.reserved(showScheduleId, seatIds);
		seatPublisher.pubMessageByScheduleId(showScheduleId, seatIds);
	}
}
