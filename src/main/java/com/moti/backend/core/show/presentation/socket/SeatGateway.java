package com.moti.backend.core.show.presentation.socket;

import static com.moti.backend.core.show.presentation.socket.dto.SeatRequestDTO.*;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
