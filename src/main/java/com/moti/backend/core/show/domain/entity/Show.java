package com.moti.backend.core.show.domain.entity;

import java.time.LocalDateTime;

import com.moti.backend.core.place.domain.entity.Place;
import com.moti.backend.core.show.domain.type.Category;
import com.moti.backend.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "`show`")
public class Show extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;

	@Column(nullable = false)
	private LocalDateTime startDate;

	@Column(nullable = false)
	private LocalDateTime endDate;

	@Column(nullable = false)
	private LocalDateTime ticketStartDateTime;

	@Column(nullable = false)
	private LocalDateTime ticketEndDateTime;

	@Column(nullable = false)
	private Integer minAge;

	@Column(nullable = false)
	private Integer runningTimeMinute;

	@Column(nullable = false)
	private Integer intermissionTime;

	private String showImg;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "created_by", nullable = false)
	// private Admin createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id", nullable = false)
	private Place place;
}