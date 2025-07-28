package com.moti.backend.core.show.presentation.socket.dto;

import com.moti.backend.core.show.domain.type.SeatStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatInfoDTO {
	private final Long seatId;
	private final SeatStatus status;
	private final Long selectedCount;

	public static SeatInfoDTO from(Long seatId, SeatStatus status, Long selectedCount) {
		return SeatInfoDTO.builder()
			.seatId(seatId)
			.status(status)
			.selectedCount(selectedCount)
			.build();
	}
}