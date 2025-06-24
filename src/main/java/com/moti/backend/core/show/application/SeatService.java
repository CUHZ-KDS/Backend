package com.moti.backend.core.show.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moti.backend.core.place.domain.entity.Zone;
import com.moti.backend.core.place.domain.service.ZoneDomainService;
import com.moti.backend.core.reservation.domain.entity.ShowSeatMapping;
import com.moti.backend.core.reservation.domain.service.ShowSeatMappingDomainService;
import com.moti.backend.core.show.domain.entity.Show;
import com.moti.backend.core.show.domain.entity.ShowSchedule;
import com.moti.backend.core.show.domain.service.ShowScheduleDomainService;
import com.moti.backend.core.show.transfer.dto.SeatResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatService {

	private final ShowScheduleDomainService showScheduleDomainService;
	private final ShowSeatMappingDomainService showSeatMappingDomainService;
	private final ZoneDomainService zoneDomainService;

	public SeatResponseDTO.ShowSeatsInfo getShowScheduleSeats(Long showScheduleId) {
		ShowSchedule showSchedule = showScheduleDomainService.getShowScheduleWithShow(showScheduleId);
		Show show = showSchedule.getShow();
		List<ShowSeatMapping> seatMappings = showSeatMappingDomainService.getShowSeatMappingsByScheduleId(showScheduleId);
		List<Zone> zones = zoneDomainService.getZonesByPlaceId(show.getPlace().getId());

		List<SeatResponseDTO.SeatInfo> seats = seatMappings.stream()
			.map(SeatResponseDTO.SeatInfo::from)
			.toList();

		List<SeatResponseDTO.ZoneInfo> zoneInfos = zones.stream()
			.map(SeatResponseDTO.ZoneInfo::from)
			.toList();

		return SeatResponseDTO.ShowSeatsInfo.of(show, showSchedule, seats, zoneInfos);
	}
}