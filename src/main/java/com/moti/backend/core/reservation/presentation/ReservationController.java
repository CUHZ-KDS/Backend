package com.moti.backend.core.reservation.presentation;

import com.moti.backend.core.reservation.application.ReservationService;
import com.moti.backend.core.reservation.transfer.CreateReservationRequest;
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
    public ResponseEntity<ApiResponse<String>> CreateReservation(@RequestBody CreateReservationRequest request) {
        reservationService.reserve(request);
        return ResponseEntity.ok(ApiResponse.success("예매하기가 성공적으로 이루어졌습니다."));
    }
}
