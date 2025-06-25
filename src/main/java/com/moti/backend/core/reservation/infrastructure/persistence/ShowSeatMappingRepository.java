package com.moti.backend.core.reservation.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.moti.backend.core.reservation.domain.entity.ShowSeatMapping;

@Repository
public interface ShowSeatMappingRepository extends JpaRepository<ShowSeatMapping, Long> {
	@Query("SELECT ssm FROM ShowSeatMapping ssm " +
		"LEFT JOIN FETCH ssm.seat s " +
		"LEFT JOIN FETCH s.zone z " +
		"LEFT JOIN FETCH ssm.grade g " +
		"WHERE ssm.showSchedule.id = :showScheduleId")
	List<ShowSeatMapping> findByShowScheduleId(@Param("showScheduleId") Long showScheduleId);
}
