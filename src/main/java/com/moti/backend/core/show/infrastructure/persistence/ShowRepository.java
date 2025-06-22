package com.moti.backend.core.show.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moti.backend.core.show.domain.entity.Show;

public interface ShowRepository extends JpaRepository<Show, Long> {
}
