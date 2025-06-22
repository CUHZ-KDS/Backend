package com.moti.backend.core.show.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moti.backend.core.show.domain.entity.Show;
import com.moti.backend.core.show.infrastructure.persistence.ShowRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowDomainService {
	private final ShowRepository showRepository;

	public List<Show> findAllShows() {
		return showRepository.findAll();
	}

	// TODO: 병합 후 커스텀 예외 처리 적용
	public Show findShowById(Long showId) {
		return showRepository.findById(showId)
			.orElseThrow(() -> new RuntimeException("해당 공연을 찾을 수 없습니다."));
			// .orElseThrow(() -> new ShowNotFoundException("해당 공연을 찾을 수 없습니다."));
	}
}
