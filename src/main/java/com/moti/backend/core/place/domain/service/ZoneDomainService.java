package com.moti.backend.core.place.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moti.backend.core.place.domain.entity.Zone;
import com.moti.backend.core.place.infrastructure.persistence.ZoneRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ZoneDomainService {
	private final ZoneRepository zoneRepository;

	public List<Zone> getZonesByPlaceId(Long placeId) {
		return zoneRepository.findByPlaceId(placeId);
	}
}
