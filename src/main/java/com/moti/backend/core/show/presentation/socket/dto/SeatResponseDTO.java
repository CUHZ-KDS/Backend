package com.moti.backend.core.show.presentation.socket.dto;

import java.time.LocalDateTime;

import com.moti.backend.core.show.domain.type.EventType;
import com.moti.backend.core.show.domain.type.SeatStatus;

import lombok.Builder;
import lombok.Getter;

public class SeatResponseDTO {

	@Getter
	@Builder
	public static class SeatToggleResponse {
		private final EventType eventType;
		private final Long showScheduleId;
		private final Long seatId;
		private final SeatStatus currentStatus;
		private final Long selectedCount;
		private final LocalDateTime timestamp;

		public static SeatToggleResponse from(EventType eventType, Long showScheduleId, Long seatId,
			SeatStatus currentStatus, Long selectedCount, LocalDateTime timestamp) {
			return SeatToggleResponse.builder()
				.eventType(eventType)
				.showScheduleId(showScheduleId)
				.seatId(seatId)
				.currentStatus(currentStatus)
				.selectedCount(selectedCount)
				.timestamp(timestamp)
				.build();
		}
	}

	@Getter
	@Builder
	public static class SeatReservationResponse {
		private EventType eventType;
		private Long showScheduleId;
		private Long[] seatIds;
		private SeatStatus seatStatus;
		private long ttlSeconds;
		private LocalDateTime timestamp;

		public static SeatReservationResponse from(EventType eventType, Long showScheduleId, Long[] seatIds,
			long ttlSeconds, LocalDateTime timestamp) {
			return SeatReservationResponse.builder()
				.eventType(eventType)
				.showScheduleId(showScheduleId)
				.seatIds(seatIds)
				.seatStatus(SeatStatus.DISABLED)
				.ttlSeconds(ttlSeconds)
				.timestamp(timestamp)
				.build();
		}
	}
}
