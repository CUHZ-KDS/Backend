package com.moti.backend.core.show.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moti.backend.core.show.domain.entity.Show;
import com.moti.backend.core.show.exception.ShowNotFoundException;
import com.moti.backend.core.show.infrastructure.persistence.ShowRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowDomainService {
	private final ShowRepository showRepository;

	public List<Show> findAllShows() {
		return showRepository.findAll();
	}

	public Show findShowById(Long showId) {
		return showRepository.findById(showId)
			.orElseThrow(() -> new ShowNotFoundException("해당 공연을 찾을 수 없습니다."));
	}
}
