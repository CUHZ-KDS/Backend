package com.moti.backend.core.show.presentation.socket;

import static com.moti.backend.core.show.presentation.socket.dto.SeatRequestDTO.*;

import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.show.application.SeatStatusService;
import com.moti.backend.core.show.presentation.socket.dto.SeatResponseDTO;
import com.moti.backend.core.show.presentation.socket.dto.SeatResponseDTO.SeatToggleResponse;
import com.moti.backend.global.exception.JwtAuthenticationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SeatGateway {

	private final SeatStatusService seatStatusService;
	private final SimpMessagingTemplate messagingTemplate;

	@SubscribeMapping("/show-schedule/{showScheduleId}/session-init")
	public List<SeatResponseDTO.SeatInfo> handleSeatSubscription(@DestinationVariable Long showScheduleId,
		StompHeaderAccessor headerAccessor) {

		Member member = (Member)headerAccessor.getSessionAttributes().get("user");

		if (member == null) {
			log.warn("세션에서 사용자 정보를 찾을 수 없음");
			//throw new UnauthorizedException("인증되지 않은 사용자");
		}

		return seatStatusService.getSeatStatusByShowScheduleId(showScheduleId);
	}

	@MessageMapping("/seat/select")
	public void selectSeat(SeatToggleRequest request, StompHeaderAccessor accessor) {
		Long memberId = getMemberIdFromStompHeaderAccessor(accessor);

		SeatToggleResponse response = seatStatusService.selected(
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

		SeatToggleResponse response = seatStatusService.deselected(
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
