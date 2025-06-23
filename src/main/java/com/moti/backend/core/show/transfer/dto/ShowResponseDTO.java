package com.moti.backend.core.show.transfer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.moti.backend.core.show.domain.entity.Grade;
import com.moti.backend.core.show.domain.entity.Show;
import com.moti.backend.core.show.domain.type.Category;
import com.moti.backend.core.show.domain.type.SeatingTier;

import lombok.Builder;
import lombok.Getter;

public class ShowResponseDTO {
	// 목록 조회용 (Grade 정보 없음)
	@Getter
	@Builder
	public static class SimpleInfo {
		private Long id;
		private String title;
		private String placeName;
		private Category category;
		private LocalDate startDate;
		private LocalDate endDate;
		private LocalDateTime ticketDateTime;
		private Integer minAge;
		private Integer runningTimeMinute;
		private Integer intermissionTime;
		private String showImgUrl;

		public static SimpleInfo from(Show show) {
			return SimpleInfo.builder()
				.id(show.getId())
				.title(show.getTitle())
				.placeName(show.getPlace().getName())
				.category(show.getCategory())
				.startDate(show.getStartDate().toLocalDate())
				.endDate(show.getEndDate().toLocalDate())
				.ticketDateTime(show.getTicketStartDateTime())
				.minAge(show.getMinAge())
				.runningTimeMinute(show.getRunningTimeMinute())
				.intermissionTime(show.getIntermissionTime())
				.showImgUrl(show.getShowImgUrl())
				.build();
		}
	}

	// 상세 조회용 (Grade 정보 포함)
	@Getter
	@Builder
	public static class DetailInfo {
		private Long id;
		private String title;
		private String placeName;
		private Category category;
		private LocalDate startDate;
		private LocalDate endDate;
		private LocalDateTime ticketDateTime;
		private Integer minAge;
		private Integer runningTimeMinute;
		private Integer intermissionTime;
		private String showImgUrl;
		private List<GradeInfo> grade;

		public static DetailInfo from(Show show, List<Grade> grades) {
			return DetailInfo.builder()
				.id(show.getId())
				.title(show.getTitle())
				.placeName(show.getPlace().getName())
				.category(show.getCategory())
				.startDate(show.getStartDate().toLocalDate())
				.endDate(show.getEndDate().toLocalDate())
				.ticketDateTime(show.getTicketStartDateTime())
				.minAge(show.getMinAge())
				.runningTimeMinute(show.getRunningTimeMinute())
				.intermissionTime(show.getIntermissionTime())
				.showImgUrl(show.getShowImgUrl())
				.grade(grades.stream()
					.map(GradeInfo::from)
					.toList())
				.build();
		}
	}

	// 목록 응답용
	@Getter
	@Builder
	public static class InfoList {
		private List<SimpleInfo> shows;

		public static InfoList from(List<Show> showList) {
			List<SimpleInfo> shows = showList.stream()
				.map(SimpleInfo::from)
				.toList();

			return InfoList.builder()
				.shows(shows)
				.build();
		}
	}

	@Getter
	@Builder
	public static class GradeInfo {
		private SeatingTier name;
		private Integer price;

		public static GradeInfo from(Grade grade) {
			return GradeInfo.builder()
				.name(grade.getGrade())
				.price(grade.getPrice())
				.build();
		}
	}
}
