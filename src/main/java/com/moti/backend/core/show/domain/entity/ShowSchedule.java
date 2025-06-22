package com.moti.backend.core.show.domain.entity;

import java.time.LocalDate;

import com.moti.backend.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "show_schedule")
@Getter
public class ShowSchedule extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDate showDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "show_id", nullable = false)
	private Show show;
}