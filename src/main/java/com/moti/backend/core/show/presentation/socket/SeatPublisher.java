package com.moti.backend.core.show.presentation.socket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static com.moti.backend.core.show.presentation.socket.dto.SeatResponseDTO.*;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SeatPublisher {
	private final SimpMessagingTemplate messagingTemplate;
	private static final Long TTL_SECONDS = 600L;

	public void pubMessageByScheduleId(Long showScheduleId, Long[] seatIds) {

		SeatReservationResponse response = SeatReservationResponse.from(
			EventType.SEAT_DISABLED,// redis를 해당 좌석이 선택중인지 아닌지 확인 필요
			showScheduleId,
			seatIds,
			TTL_SECONDS,
 			LocalDateTime.now()
		);

		String destination = "/topic/show-schedule/" + showScheduleId + "/seats";
		messagingTemplate.convertAndSend(destination, response);
	}
}
