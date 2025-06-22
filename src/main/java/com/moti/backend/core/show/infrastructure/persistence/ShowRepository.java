package com.moti.backend.core.show.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moti.backend.core.show.domain.entity.Show;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
}
