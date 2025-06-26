package com.moti.backend.core.reservation.domain.service;

import com.moti.backend.core.reservation.domain.entity.ShowSeatMapping;
import com.moti.backend.core.reservation.infrastructure.persistence.ShowSeatMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowSeatCompensationService {

    private final ShowSeatMappingRepository showSeatMappingRepository;

    @Transactional
    public void releaseShowSeats(List<Long> sortedShowSeatIds) {
        List<ShowSeatMapping> showSeats = showSeatMappingRepository.findAllByIdWithPessimisticLock(sortedShowSeatIds);

        for (ShowSeatMapping showSeat : showSeats) {
            showSeat.release(); // HOLD 에서 AVAILABLE 로 변경
        }
    }
}
