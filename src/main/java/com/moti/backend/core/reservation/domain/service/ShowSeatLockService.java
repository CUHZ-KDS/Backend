package com.moti.backend.core.reservation.domain.service;

import com.moti.backend.core.reservation.domain.entity.ShowSeatMapping;
import com.moti.backend.core.reservation.exception.ShowSeatNotFoundException;
import com.moti.backend.core.reservation.infrastructure.ShowSeatMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShowSeatLockService {

    private final ShowSeatMappingRepository showSeatMappingRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ShowSeatMapping> lockAndHoldShowSeats(List<Long> showSeatIds) {
        List<Long> sortedShowSeatIds = showSeatIds.stream().sorted().toList();

        List<ShowSeatMapping> showSeats = showSeatMappingRepository.findAllByIdWithPessimisticLock(sortedShowSeatIds);

        if (showSeats.size() != sortedShowSeatIds.size()) {
            throw new ShowSeatNotFoundException("예약할 수 없는 좌석이 포함되어있습니다.");
        }

        for (ShowSeatMapping showSeat : showSeats) {
            showSeat.hold();
        }

        return showSeats;
    }
}