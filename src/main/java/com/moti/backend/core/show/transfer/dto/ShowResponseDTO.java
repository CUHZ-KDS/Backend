package com.moti.backend.core.show.transfer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.moti.backend.core.show.domain.entity.Show;
import com.moti.backend.core.show.domain.type.Category;

import lombok.Builder;
import lombok.Getter;

public class ShowResponseDTO {
	@Getter
	@Builder
	public static class InfoList {
		private List<Info> shows;

		public static InfoList from(List<Show> showList) {
			List<Info> shows = showList.stream()
				.map(Info::from)
				.collect(Collectors.toList());

			return InfoList.builder()
				.shows(shows)
				.build();
		}
	}

	@Getter
	@Builder
	public static class Info {
		private Long id;
		private String title;
		private String placeName;
		private Category category;
		private LocalDate startDate;
		private LocalDate endDate;
		private LocalDateTime ticketStartDateTime;
		private Integer minAge;
		private Integer runningTimeMinute;
		private Integer intermissionTime;
		private String imgSource;

		public static Info from(Show show) {
			return Info.builder()
					.id(show.getId())
					.title(show.getTitle())
					.placeName(show.getPlace().getName())
					.category(show.getCategory())
					.startDate(show.getStartDate().toLocalDate())
					.endDate(show.getEndDate().toLocalDate())
					.ticketStartDateTime(show.getTicketStartDateTime())
					.minAge(show.getMinAge())
					.runningTimeMinute(show.getRunningTimeMinute())
					.intermissionTime(show.getIntermissionTime())
					.imgSource(show.getShowImg())
				.build();
		}
	}
}
