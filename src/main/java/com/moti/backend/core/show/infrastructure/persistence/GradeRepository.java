package com.moti.backend.core.show.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moti.backend.core.show.domain.entity.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
	List<Grade> findByShowId(Long showId);
}
