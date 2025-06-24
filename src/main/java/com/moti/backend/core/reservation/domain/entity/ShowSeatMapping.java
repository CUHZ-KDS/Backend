package com.moti.backend.core.reservation.domain.entity;

import com.moti.backend.core.place.domain.entity.Seat;
import com.moti.backend.core.reservation.domain.type.SeatStatus;
import com.moti.backend.core.reservation.exception.InvalidReserveStateException;
import com.moti.backend.core.reservation.exception.InvalidHoldStateException;
import com.moti.backend.core.show.domain.entity.Grade;
import com.moti.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "show_seat_mapping")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ShowSeatMapping extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "show_schedule_id", nullable = false)
    private Long showScheduleId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status;

    public void reserve() {
        if (this.status != SeatStatus.HOLD && this.status != SeatStatus.AVAILABLE) {
            throw new InvalidReserveStateException("RESERVED 혹은 AVAILABLE 상태는 예약 확정이 불가합니다. 현재 상태: " + this.status);
        }
        this.status = SeatStatus.RESERVED;
    }

    public void hold() {
        if (this.status != SeatStatus.AVAILABLE) {
            throw new InvalidHoldStateException("HOLD 혹은 BOOKED 상태는 예매가 불가합니다. 현재 상태: " + this.status);
        }
        this.status = SeatStatus.HOLD;
    }

    public void release() {
        this.status = SeatStatus.AVAILABLE;
    }
}
