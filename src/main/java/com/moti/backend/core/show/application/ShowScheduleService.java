package com.moti.backend.core.show.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moti.backend.core.show.domain.entity.ShowSchedule;
import com.moti.backend.core.show.domain.service.ShowDomainService;
import com.moti.backend.core.show.domain.service.ShowScheduleDomainService;
import com.moti.backend.core.show.transfer.dto.ShowScheduleResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowScheduleService {
	private final ShowScheduleDomainService showScheduleDomainService;
	private final ShowDomainService showDomainService;

	public ShowScheduleResponseDTO.InfoList getShowSchedules(Long showId) {
		showDomainService.findShowById(showId);
		List<ShowSchedule> schedules = showScheduleDomainService.getShowSchedulesByShowId(showId);
		return ShowScheduleResponseDTO.InfoList.from(schedules);
	}
}
