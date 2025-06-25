package com.moti.backend.core.show.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moti.backend.core.show.domain.entity.ShowSchedule;
import com.moti.backend.core.show.exception.ShowScheduleNotFoundException;
import com.moti.backend.core.show.infrastructure.persistence.ShowScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowScheduleDomainService {
	private final ShowScheduleRepository showScheduleRepository;

	public List<ShowSchedule> getShowSchedulesByShowId(Long showId) {
		return showScheduleRepository.findByShowIdOrderByShowDateTime(showId);
	}

	public ShowSchedule getShowScheduleWithShow(Long showScheduleId) {
		return showScheduleRepository.findByIdWithShow(showScheduleId)
			.orElseThrow(() -> new ShowScheduleNotFoundException("해당 공연 일정을 찾을 수 없습니다."));
	}
}