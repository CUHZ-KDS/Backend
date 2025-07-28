package com.moti.backend.core.show.presentation.socket.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.moti.backend.core.show.domain.type.EventType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessionInitResponseDTO {

	private final EventType eventType;
	private final Long showScheduleId;
	private final List<SeatInfoDTO> seats;
	private final SessionInfo sessionInfo;
	private final UserContext userContext;
	private final LocalDateTime timestamp;

	@Getter
	@Builder
	public static class SessionInfo {
		private final String sessionId;
		private final LocalDateTime connectionTime;

		public static SessionInfo from(String sessionId, LocalDateTime connectionTime) {
			return SessionInfo.builder()
				.sessionId(sessionId)
				.connectionTime(connectionTime)
				.build();
		}
	}

	@Getter
	@Builder
	public static class UserContext {
		private final SeatSelection seatSelection;

		public static UserContext from(SeatSelection seatSelection) {
			return UserContext.builder()
				.seatSelection(seatSelection)
				.build();
		}
	}

	@Getter
	@Builder
	public static class SeatSelection {
		private final List<Long> selectedSeats;
		private final HeldSeats heldSeats;
		private final List<Long> reservedSeats;

		public static SeatSelection from(List<Long> selectedSeats, HeldSeats heldSeats,
			List<Long> reservedSeats) {
			return SeatSelection.builder()
				.selectedSeats(selectedSeats)
				.heldSeats(heldSeats)
				.reservedSeats(reservedSeats)
				.build();
		}
	}

	@Getter
	@Builder
	public static class HeldSeats {
		private final List<Long> seatIds;
		private final String orderId;
		private final LocalDateTime holdStartTime;
		private final Long ttl;
		private final Long maxHoldTime;

		public static HeldSeats from(List<Long> seatIds, String orderId, LocalDateTime holdStartTime, Long ttl,
			Long maxHoldTime) {
			return HeldSeats.builder()
				.seatIds(seatIds)
				.orderId(orderId)
				.holdStartTime(holdStartTime)
				.ttl(ttl)
				.maxHoldTime(maxHoldTime)
				.build();
		}
	}

	public static SessionInitResponseDTO from(Long showScheduleId, List<SeatInfoDTO> seats,
		SessionInfo sessionInfo, UserContext userContext, LocalDateTime timestamp) {
		return SessionInitResponseDTO.builder()
			.eventType(EventType.INITIAL_SEAT_STATUS)
			.showScheduleId(showScheduleId)
			.seats(seats)
			.sessionInfo(sessionInfo)
			.userContext(userContext)
			.timestamp(timestamp)
			.build();
	}
}
