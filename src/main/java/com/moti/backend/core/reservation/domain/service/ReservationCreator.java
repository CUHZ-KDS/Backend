package com.moti.backend.core.reservation.domain.service;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.reservation.domain.entity.Reservation;
import com.moti.backend.core.reservation.domain.entity.ShowSeatMapping;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationCreator {

    @Transactional
    public List<Reservation> createReservations(List<ShowSeatMapping> showSeats, Member member) {
        return showSeats.stream()
                .map(showSeat -> Reservation.create(member, showSeat)) // 좌석마다 1개
                .collect(Collectors.toList());
    }
}
