package com.moti.backend.core.show.presentation.rest.dto;

import java.time.LocalDate;
import java.util.List;

import com.moti.backend.core.show.domain.entity.ShowSchedule;

import lombok.Builder;
import lombok.Getter;

public class ShowScheduleResponseDTO {
	@Getter
	@Builder
	public static class Info {
		private final Long id;
		private final LocalDate showDate;

		public static Info from(ShowSchedule entity) {
			return Info.builder()
				.id(entity.getId())
				.showDate(entity.getShowDate())
				.build();
		}
	}

	@Getter
	@Builder
	public static class InfoList {
		private final List<Info> schedules;

		public static InfoList from(List<ShowSchedule> entities) {
			List<Info> schedules = entities.stream()
				.map(Info::from)
				.toList();

			return InfoList.builder()
				.schedules(schedules)
				.build();
		}
	}
}
