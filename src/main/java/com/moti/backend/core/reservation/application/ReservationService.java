package com.moti.backend.core.reservation.application;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.member.exception.MemberNotFoundException;
import com.moti.backend.core.member.infrastructure.persistence.MemberRepository;
import com.moti.backend.core.order.domain.entity.Order;
import com.moti.backend.core.order.infrastructure.OrderRepository;
import com.moti.backend.core.reservation.domain.entity.Reservation;
import com.moti.backend.core.reservation.domain.entity.ShowSeatMapping;
import com.moti.backend.core.reservation.domain.helper.OrderTokenGenerator;
import com.moti.backend.core.reservation.domain.service.*;
import com.moti.backend.core.reservation.exception.SeatHoldFailedException;
import com.moti.backend.core.reservation.infrastructure.ReservationRepository;
import com.moti.backend.core.reservation.infrastructure.ShowSeatMappingRepository;
import com.moti.backend.core.reservation.transfer.CreateReservationRequest;
import com.moti.backend.core.reservation.transfer.CreateReservationResponse;
import com.moti.backend.global.exception.BusinessException;
import com.moti.backend.global.type.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ShowSeatMappingRepository showSeatMappingRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    private final ShowSeatLockService showSeatLockService;
    private final ShowSeatCompensationService showSeatCompensationService;
    private final ReservationCreator reservationCreator;

    @Transactional
    public CreateReservationResponse reserve(CreateReservationRequest dto) {
        List<Long> sortedShowSeatIds = Arrays.stream(dto.getShowSeatMappingIds())
                .sorted()
                .toList();

        List<ShowSeatMapping> showSeats;

        try {
            // show_seat_mapping hold로 상태 변경
            showSeats = showSeatLockService.lockAndHoldShowSeats(sortedShowSeatIds);
        } catch (Exception e) {
            throw new SeatHoldFailedException("좌석 확보 실패");
        }

        try {
            Member member = memberRepository.findById(1L)
                    .orElseThrow(() -> new MemberNotFoundException("예매 시도자를 시스템에서 찾을 수 없습니다."));

            // Reservation 레코드 생성
            // 예매한 좌석의 개수만큼 reservation 레코드 생성 및 저장
            List<Reservation> reservations = reservationCreator.createReservations(showSeats, member);
            reservationRepository.saveAll(reservations);

            // Order 레코드 생성
            // orderToken 생성기 토스에서 결제 요청이 보내진 뒤 결제 승인 때 인증하는 용도의 토큰
            String orderToken = OrderTokenGenerator.generateToken();

            // 결제할 금액 계산 로직
            Long totalAmount = showSeatMappingRepository.findPricesByShowSeatIds(sortedShowSeatIds)
                    .stream()
                    .reduce(0L, Long::sum);

            // order 레코드 생성
            Order order = Order.create(orderToken, member, totalAmount);
            orderRepository.save(order);

            return new CreateReservationResponse(orderToken, totalAmount, sortedShowSeatIds);

        } catch (Exception e) {
            // 예외 발생 시 보상 로직 실행
            // 새롭게 생성된 트랜잭션의 결과인 좌석 상태 복구
            showSeatCompensationService.releaseShowSeats(sortedShowSeatIds);
            throw e;
        }
    }
}
