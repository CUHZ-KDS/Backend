package com.moti.backend.core.show.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moti.backend.core.show.application.SeatService;
import com.moti.backend.core.show.transfer.dto.SeatResponseDTO;
import com.moti.backend.global.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/show-schedules")
@RequiredArgsConstructor
public class SeatController {
	private final SeatService seatService;

	@GetMapping("/{showScheduleId}/seats")
	public ResponseEntity<ApiResponse<SeatResponseDTO.ShowSeatsInfo>> getShowSeats(
		@PathVariable Long showScheduleId
	) {
		SeatResponseDTO.ShowSeatsInfo response = seatService.getShowScheduleSeats(showScheduleId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}
}
