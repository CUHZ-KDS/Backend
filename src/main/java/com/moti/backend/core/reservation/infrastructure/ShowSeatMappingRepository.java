package com.moti.backend.core.reservation.infrastructure;

import com.moti.backend.core.reservation.domain.entity.ShowSeatMapping;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowSeatMappingRepository extends JpaRepository<ShowSeatMapping, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ShowSeatMapping s WHERE s.id IN :ids")
    List<ShowSeatMapping> findAllByIdWithPessimisticLock(@Param("ids") List<Long> ids);

    @Query("SELECT s.grade.price FROM ShowSeatMapping s WHERE s.id IN :ids")
    List<Long> findPricesByShowSeatIds(@Param("ids") List<Long> ids);
}
