package com.moti.backend.core.show.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moti.backend.core.place.domain.entity.Zone;
import com.moti.backend.core.place.infrastructure.persistence.ZoneRepository;
import com.moti.backend.core.reservation.domain.entity.ShowSeatMapping;
import com.moti.backend.core.reservation.infrastructure.persistence.ShowSeatMappingRepository;
import com.moti.backend.core.show.domain.entity.Show;
import com.moti.backend.core.show.domain.entity.ShowSchedule;
import com.moti.backend.core.show.exception.ShowScheduleNotFoundException;
import com.moti.backend.core.show.infrastructure.persistence.ShowScheduleRepository;
import com.moti.backend.core.show.transfer.dto.SeatResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatDomainService {

	private final ShowScheduleRepository showScheduleRepository;
	private final ShowSeatMappingRepository showSeatMappingRepository;
	private final ZoneRepository zoneRepository;

	public SeatResponseDTO.ShowSeatsInfo getShowSeats(Long showScheduleId) {
		// 공연 스케줄 조회 (공연 정보도 함께)
		ShowSchedule showSchedule = showScheduleRepository.findByIdWithShow(showScheduleId)
			.orElseThrow(() -> new ShowScheduleNotFoundException("해당 공연 일정을 찾을 수 없습니다."));

		Show show = showSchedule.getShow();

		// 공연의 모든 좌석 매핑 조회
		List<ShowSeatMapping> seatMappings = showSeatMappingRepository.findByShowScheduleId(showScheduleId);

		// 좌석 정보 변환
		List<SeatResponseDTO.SeatInfo> seats = seatMappings.stream()
			.map(SeatResponseDTO.SeatInfo::from)
			.toList();

		// 장소의 구역 정보 조회
		List<Zone> zones = zoneRepository.findByPlaceId(show.getPlace().getId());
		List<SeatResponseDTO.ZoneInfo> zoneInfos = zones.stream()
			.map(SeatResponseDTO.ZoneInfo::from)
			.toList();

		return SeatResponseDTO.ShowSeatsInfo.of(show, showSchedule, seats, zoneInfos);
	}
}