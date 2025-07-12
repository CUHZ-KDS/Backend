package com.moti.backend.core.show.presentation.socket.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SeatRequestDTO {

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SeatToggleRequest {
		private Long showScheduleId;
		private Long seatId;

		public static SeatToggleRequest from(Long showScheduleId, Long seatId) {
			return SeatToggleRequest.builder()
				.showScheduleId(showScheduleId)
				.seatId(seatId)
				.build();
		}
	}

	@Getter
	@Builder
	public static class SeatReservationRequest {
		private final Long showScheduleId;
		private final List<Long> seatId;

		public static SeatReservationRequest from(Long showScheduleId, List<Long> seatId) {
			return SeatReservationRequest.builder()
				.showScheduleId(showScheduleId)
				.seatId(seatId)
				.build();
		}
	}
}
