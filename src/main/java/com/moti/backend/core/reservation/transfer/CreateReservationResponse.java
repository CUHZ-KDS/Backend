package com.moti.backend.core.reservation.transfer;

import java.util.List;

public record CreateReservationResponse(
        String orderToken,
        Long totalAmount,
        List<Long> reservedSeatIds
) {}