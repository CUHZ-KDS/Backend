package com.moti.backend.core.place.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moti.backend.core.place.domain.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
