package com.moti.backend.core.reservation.presentation;

import com.moti.backend.core.reservation.application.ReservationService;
import com.moti.backend.core.reservation.transfer.CreateReservationRequest;
import com.moti.backend.core.reservation.transfer.CreateReservationResponse;
import com.moti.backend.global.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateReservationResponse>> CreateReservation(@RequestBody CreateReservationRequest request) {
        CreateReservationResponse response = reservationService.reserve(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
