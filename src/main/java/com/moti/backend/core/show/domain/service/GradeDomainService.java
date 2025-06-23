package com.moti.backend.core.show.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moti.backend.core.show.domain.entity.Grade;
import com.moti.backend.core.show.infrastructure.persistence.GradeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradeDomainService {
	private final GradeRepository gradeRepository;

	public List<Grade> findGradesByShowId(Long showId) {
		return gradeRepository.findByShowId(showId);
	}
}
