package com.moti.backend.core.reservation.domain.service;

import com.moti.backend.core.reservation.exception.SeatLimitExceededException;
import org.springframework.stereotype.Service;

@Service
public class ReservationPolicyService {

    private static final int MAX_SEATS_PER_USER = 2;

    public void validateSeatCount(int seatIdsCount) {
        if (seatIdsCount > MAX_SEATS_PER_USER) {
            throw new SeatLimitExceededException("좌석 예매 한도를 초과했습니다.");
        }
    }
}

