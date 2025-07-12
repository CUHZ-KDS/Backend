package com.moti.backend.core.show.presentation.socket.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.moti.backend.core.show.presentation.socket.EventType;
import com.moti.backend.core.show.presentation.socket.SeatStatus;

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
		private final int selectedCount;
		private final LocalDateTime timestamp;

		public static SeatToggleResponse from(EventType eventType, Long showScheduleId, Long seatId,
			SeatStatus currentStatus, int selectedCount, LocalDateTime timestamp) {
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
		private int ttlSeconds;
		private String timestamp;

		public static SeatReservationResponse from(EventType eventType, Long showScheduleId, Long[] seatIds,
			int ttlSeconds, String timestamp) {
			return SeatReservationResponse.builder()
				.eventType(eventType)
				.showScheduleId(showScheduleId)
				.seatIds(seatIds)
				.ttlSeconds(ttlSeconds)
				.timestamp(timestamp)
				.build();
		}
	}

	@Getter
	@Builder
	public static class InitialSeatResponse {
		private EventType eventType;
		private Long showScheduleId;
		private List<SeatInfo> seatIds;
		private String timestamp;

		public static InitialSeatResponse from(EventType eventType, Long showScheduleId, List<SeatInfo> seatInfoList,
			String timestamp) {
			return InitialSeatResponse.builder()
				.eventType(eventType)
				.showScheduleId(showScheduleId)
				.seatIds(seatInfoList)
				.timestamp(timestamp)
				.build();
		}
	}

	@Getter
	@Builder
	public static class SeatInfo {
		private Long seatId;
		private String status;

		public static SeatInfo from(Long seatId, String status) {
			return SeatInfo.builder()
				.seatId(seatId)
				.status(status)
				.build();
		}
	}
}
