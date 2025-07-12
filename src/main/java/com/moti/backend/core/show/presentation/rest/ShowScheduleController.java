package com.moti.backend.core.show.presentation.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moti.backend.core.show.application.ShowScheduleService;
import com.moti.backend.core.show.presentation.rest.dto.ShowScheduleResponseDTO;
import com.moti.backend.global.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/shows")
@RequiredArgsConstructor
public class ShowScheduleController {

	private final ShowScheduleService showScheduleService;

	@GetMapping("/{showId}/schedules")
	public ResponseEntity<ApiResponse<ShowScheduleResponseDTO.InfoList>> getShowSchedules(@PathVariable Long showId) {
		ShowScheduleResponseDTO.InfoList schedules = showScheduleService.getShowSchedules(showId);
		return ResponseEntity.ok(ApiResponse.success(schedules, "공연 일정 목록 조회 성공"));
	}
}