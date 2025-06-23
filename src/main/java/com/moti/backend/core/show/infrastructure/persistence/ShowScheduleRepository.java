package com.moti.backend.core.show.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moti.backend.core.show.domain.entity.ShowSchedule;

public interface ShowScheduleRepository extends JpaRepository<ShowSchedule, Long> {
	@Query("SELECT ss FROM ShowSchedule ss WHERE ss.show.id = :showId ORDER BY ss.showDate ASC")
	List<ShowSchedule> findByShowIdOrderByShowDateTime(@Param("showId") Long showId);
}
