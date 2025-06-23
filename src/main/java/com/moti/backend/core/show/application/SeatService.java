package com.moti.backend.core.show.application;

import org.springframework.stereotype.Service;

import com.moti.backend.core.show.domain.service.SeatDomainService;
import com.moti.backend.core.show.transfer.dto.SeatResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {

	private final SeatDomainService seatDomainService;

	public SeatResponseDTO.ShowSeatsInfo getShowScheduleSeats(Long showId) {
		return seatDomainService.getShowSeats(showId);
	}
}