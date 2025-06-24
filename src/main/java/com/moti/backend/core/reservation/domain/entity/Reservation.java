package com.moti.backend.core.reservation.domain.entity;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.reservation.domain.type.ReservationStatus;
import com.moti.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "show_seat_mapping_id", nullable = false)
    private ShowSeatMapping showSeatMapping;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    private Reservation(ShowSeatMapping showSeatMapping, Member member, ReservationStatus status) {
        this.showSeatMapping = showSeatMapping;
        this.member = member;
        this.status = status;
    }

    public static Reservation create(Member member, ShowSeatMapping showSeat) {
        return new Reservation(showSeat, member, ReservationStatus.PENDING);
    }
}

