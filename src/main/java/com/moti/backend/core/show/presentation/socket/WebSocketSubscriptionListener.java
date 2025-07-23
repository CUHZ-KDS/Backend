package com.moti.backend.core.show.presentation.socket;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.show.application.SeatStatusService;
import com.moti.backend.core.show.presentation.socket.dto.SeatResponseDTO.SeatInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketSubscriptionListener {

	private final SimpMessagingTemplate messagingTemplate;
	private final SeatStatusService seatStatusService; // 좌석 상태 조회 서비스

	@EventListener
	public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String destination = accessor.getDestination(); // 예: /topic/show-schedule/123/seats

		// 1. destination 주소 패턴 체크
		if (destination != null && destination.matches("/topic/show-schedule/\\d+/seats")) {
			log.info("WebSocket 구독 이벤트 감지 - 세션: {}, 목적지: {}", accessor.getSessionId(), destination);
			// 2. showScheduleId 추출
			Long showScheduleId = extractShowScheduleId(destination);

			// 3. 세션에서 사용자 객체 가져오기
			Member member = (Member)accessor.getSessionAttributes().get("user");
			if (member != null) {
				// 4. 좌석 정보 조회
				List<SeatInfo> seats = seatStatusService.getSeatStatusByShowScheduleId(showScheduleId);

				// 5. 유저에게만(개별적으로) 데이터 전송
				// 기본적으로 Spring의 convertAndSendToUser는 user name을 필요로 함
				messagingTemplate.convertAndSendToUser(
					member.getId().toString(),      // user 식별자 (String)
					"/queue/show-schedule/" + showScheduleId + "/seats", // 유니크한 개인 큐
					seats
				);
			}
		}
	}

	private Long extractShowScheduleId(String destination) {
		String[] parts = destination.split("/");
		return Long.parseLong(parts[3]);
	}
}
