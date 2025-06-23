package com.moti.backend.core.show.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moti.backend.core.show.domain.entity.ShowSchedule;

public interface ShowScheduleRepository extends JpaRepository<ShowSchedule, Long> {
	@Query("SELECT ss FROM ShowSchedule ss WHERE ss.show.id = :showId ORDER BY ss.showDate ASC")
	List<ShowSchedule> findByShowIdOrderByShowDateTime(@Param("showId") Long showId);

	@Query("SELECT ss FROM ShowSchedule ss " +
		"LEFT JOIN FETCH ss.show s " +
		"WHERE ss.id = :showScheduleId")
	Optional<ShowSchedule> findByIdWithShow(@Param("showScheduleId") Long showScheduleId);
}
