package com.moti.backend.core.show.application;

import org.springframework.stereotype.Service;

import com.moti.backend.core.show.domain.service.ShowDomainService;
import com.moti.backend.core.show.transfer.dto.ShowResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowService {
	private final ShowDomainService showDomainService;

	public ShowResponseDTO.InfoList getAllShows() {
		return ShowResponseDTO.InfoList.from(showDomainService.findAllShows());
	}
}
