package com.moti.backend.core.show.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.moti.backend.core.show.domain.entity.Grade;
import com.moti.backend.core.show.domain.entity.Show;
import com.moti.backend.core.show.domain.service.GradeDomainService;
import com.moti.backend.core.show.domain.service.ShowDomainService;
import com.moti.backend.core.show.transfer.dto.ShowResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowService {
	private final ShowDomainService showDomainService;
	private final GradeDomainService gradeDomainService;

	public ShowResponseDTO.InfoList getAllShows() {
		return ShowResponseDTO.InfoList.from(showDomainService.findAllShows());
	}

	public ShowResponseDTO.DetailInfo getShowDetail(Long showId) {
		Show show = showDomainService.findShowById(showId);
		List<Grade> grades = gradeDomainService.findGradesByShowId(showId);
		LocalDateTime serverTime = LocalDateTime.now();
		return ShowResponseDTO.DetailInfo.from(show, grades, serverTime);
	}
}
