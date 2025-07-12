package com.moti.backend.core.show.presentation.socket;

import static com.moti.backend.core.show.presentation.socket.dto.SeatRequestDTO.*;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

	private final SeatStatusService showSocketService;
	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/seat/select")
	public void selectSeat(SeatToggleRequest request) {

		// Principal principal
		// SeatToggleResponse response = showSocketService.selected(
		// 	request.getShowScheduleId(),
		// 	request.getSeatId(),
		// 	getMemberIdFromPrincipal(principal)
		// );
		log.info("selectSeat request: {}", request);

		SeatToggleResponse response = showSocketService.selected(
			request.getShowScheduleId(),
			request.getSeatId()
		);

		String destination = "/topic/show-schedule/" + request.getShowScheduleId() + "/seats";
		messagingTemplate.convertAndSend(destination, response);
	}

	// @MessageMapping("/seat/deselect")
	// public void deselectSeat(SeatToggleRequest request, Principal principal) {
	//
	// 	SeatToggleResponse response = showSocketService.disSelected(
	// 		request.getShowScheduleId(),
	// 		request.getSeatId(),
	// 		getMemberIdFromPrincipal(principal)
	// 	);
	//
	// 	String destination = "/topic/show-schedule/" + request.getShowScheduleId() + "/seats";
	// 	messagingTemplate.convertAndSend(destination, response);
	// }

	// @MessageMapping("/seat/reserve")
	// public SeatReservationResponse reserveSeat(
	// 	// 3. @MessageMapping 경로의 {showId} 변수를 파라미터로 가져옵니다.
	// 	@DestinationVariable String show_schedule_id,
	// 	// 4. 클라이언트가 보낸 JSON 데이터를 자바 객체로 변환하여 받습니다.
	// 	SeatReservationRequest request) {
	//
	// 	// 5. 서비스 계층을 호출하여 비즈니스 로직을 수행합니다.
	// 	SeatReservationResponse changedStatus = showSocketService.reserved(show_schedule_id, request.getSeatId());
	//
	// 	// 6. 반환된 객체가 @SendTo에 지정된 토픽으로 전송됩니다.
	// 	return changedStatus;
	// }

	//todo: GateWay에서 시큐리티 컨텍스트에서 직접 접근하여 member 정보를 가져오는게 적합한지 확인 필요
	private Long getMemberIdFromPrincipal(Principal principal) {
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			Object principalObj = ((UsernamePasswordAuthenticationToken)principal).getPrincipal();
			if (principalObj instanceof Member) {
				return ((Member)principalObj).getId();
			}
		}
		throw new JwtAuthenticationException("인증 정보에서 memberId를 찾을 수 없습니다.");
	}

}
