package com.moti.backend.core.place.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moti.backend.core.place.domain.entity.Zone;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
	List<Zone> findByPlaceId(Long placeId);
}