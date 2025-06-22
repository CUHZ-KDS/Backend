package com.moti.backend.core.show.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moti.backend.core.show.application.ShowService;
import com.moti.backend.core.show.transfer.dto.ShowResponseDTO;
import com.moti.backend.global.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shows")
public class ShowController {
	private final ShowService showService;

	@GetMapping
	public ResponseEntity<ApiResponse<?>> getAllShows() {
		ShowResponseDTO.InfoList shows = showService.getAllShows();
		return ResponseEntity.ok(ApiResponse.success(shows));
	}

	@GetMapping("/{showId}")
	public ResponseEntity<ApiResponse<?>> getShowDetail(@PathVariable Long showId) {
		ShowResponseDTO.DetailInfo showDetail = showService.getShowDetail(showId);
		return ResponseEntity.ok(ApiResponse.success(showDetail));
	}
}
