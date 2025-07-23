package com.moti.backend.core.show.presentation.socket;

import static com.moti.backend.core.show.presentation.socket.dto.SeatRequestDTO.*;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.show.application.SeatStatusService;
import com.moti.backend.core.show.presentation.socket.dto.SeatResponseDTO.SeatToggleResponse;
import com.moti.backend.global.exception.JwtAuthenticationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SeatGateway {

	private final SeatStatusService showStatusService;
	private final SimpMessagingTemplate messagingTemplate;

	// message -> 구독한 memberId로 보내야하는건데
	@SubscribeMapping("(app)/show-schedule/{showScheduleId}/seats")
	public String handleSeatSubscription(@DestinationVariable Long showScheduleId,
		StompHeaderAccessor accessor) {
		log.info("Client subscribed to seat status for show schedule: {}", showScheduleId);
		// showStatusService에서 현재 좌석 현황을 가져오는 메서드를 호출합니다. (새로 만들어야 할 수 있습니다)
		// 이 메서드의 반환 값은 구독을 요청한 클라이언트에게만 자동으로 전송됩니다.
		return "P";
	}

	@MessageMapping("/seat/select")
	public void selectSeat(SeatToggleRequest request, StompHeaderAccessor accessor) {
		Long memberId = getMemberIdFromStompHeaderAccessor(accessor);

		SeatToggleResponse response = showStatusService.selected(
			request.getShowScheduleId(),
			request.getSeatId(),
			memberId
		);

		String destination = "/topic/show-schedule/" + request.getShowScheduleId() + "/seats";
		messagingTemplate.convertAndSend(destination, response);
	}

	@MessageMapping("/seat/deselect")
	public void deselectSeat(SeatToggleRequest request, StompHeaderAccessor accessor) {
		Long memberId = getMemberIdFromStompHeaderAccessor(accessor);

		SeatToggleResponse response = showStatusService.deselected(
			request.getShowScheduleId(),
			request.getSeatId(),
			memberId
		);

		String destination = "/topic/show-schedule/" + request.getShowScheduleId() + "/seats";
		messagingTemplate.convertAndSend(destination, response);
	}

	private Long getMemberIdFromStompHeaderAccessor(StompHeaderAccessor accessor) {
		Member member = (Member)accessor.getSessionAttributes().get("user");
		if (member == null) {
			log.error("WebSocket 세션에 사용자 정보가 없습니다. 세션: {}", accessor.getSessionId());
			throw new JwtAuthenticationException("사용자 정보가 세션에 없습니다.");
		}
		return member.getId();
	}
}
