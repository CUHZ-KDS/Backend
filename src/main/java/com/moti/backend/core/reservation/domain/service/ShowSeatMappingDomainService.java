package com.moti.backend.core.reservation.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moti.backend.core.reservation.domain.entity.ShowSeatMapping;
import com.moti.backend.core.reservation.infrastructure.persistence.ShowSeatMappingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowSeatMappingDomainService {
	private final ShowSeatMappingRepository showSeatMappingRepository;

	public List<ShowSeatMapping> getShowSeatMappingsByScheduleId(Long showScheduleId) {
		return showSeatMappingRepository.findByShowScheduleId(showScheduleId);
	}
}
