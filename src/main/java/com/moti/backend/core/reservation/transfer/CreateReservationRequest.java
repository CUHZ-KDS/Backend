package com.moti.backend.core.reservation.transfer;

import lombok.Getter;

@Getter
public class CreateReservationRequest {
    private Long[] showSeatMappingIds;
}
