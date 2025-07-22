package com.moti.backend.core.show.domain.type;

public enum EventType {
	INITIAL_SEAT_STATUS,
	SEAT_RESERVED,
	SEAT_SELECTED,
	SEAT_DESELECTED,
	SEAT_DISABLED,
	//todo: error 이벤트 타입은 분류해야할지?
	SEAT_SELECT_ERROR,
	SEAT_LIMIT_ERROR,
	AUTH_ERROR
}
