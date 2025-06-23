package com.moti.backend.core.show.transfer.dto;

import java.util.List;

import com.moti.backend.core.place.domain.entity.Zone;
import com.moti.backend.core.reservation.domain.entity.ShowSeatMapping;
import com.moti.backend.core.show.domain.entity.Show;
import com.moti.backend.core.show.domain.entity.ShowSchedule;
import com.moti.backend.core.show.domain.type.SeatingTier;

import lombok.Builder;
import lombok.Getter;

public class SeatResponseDTO {
	@Getter
	@Builder
	public static class ShowSeatsInfo {
		private Long placeId;
		private Long showId;
		private Long showScheduleId;
		private String showTitle;
		private List<SeatInfo> seats;
		private List<ZoneInfo> zones;
		public static ShowSeatsInfo of(Show show, ShowSchedule showSchedule, List<SeatInfo> seats, List<ZoneInfo> zones) {
			return ShowSeatsInfo.builder()
				.placeId(show.getPlace().getId())
				.showId(show.getId())
				.showScheduleId(showSchedule.getId())
				.showTitle(show.getTitle())
				.seats(seats)
				.zones(zones)
				.build();
		}
	}

	@Getter
	@Builder
	public static class SeatInfo {
		private Long zoneId;
		private Long id;
		private Integer row;
		private Integer col;
		private SeatingTier name;
		private Boolean isVisible;
		private String status;
		private Integer price;
		public static SeatInfo from(ShowSeatMapping mapping) {
			return SeatInfo.builder()
				.zoneId(mapping.getSeat().getZone().getId())
				.id(mapping.getSeat().getId())
				.row(mapping.getSeat().getRow())
				.col(mapping.getSeat().getColumn())
				.name(mapping.getGrade().getGrade())
				.isVisible(mapping.getSeat().isStatus())
				.status(mapping.getStatus().name().toLowerCase())
				.price(mapping.getGrade().getPrice())
				.build();
		}
	}

	@Getter
	@Builder
	public static class ZoneInfo {
		private Long zoneId;
		private String zoneName;
		public static ZoneInfo from(Zone zone) {
			return ZoneInfo.builder()
				.zoneId(zone.getId())
				.zoneName(zone.getName())
				.build();
		}
	}
}
