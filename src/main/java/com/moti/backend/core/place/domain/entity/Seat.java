package com.moti.backend.core.place.domain.entity;

import com.moti.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "seat")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Seat extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(name = "seat_row", nullable = false)
    private int row;

    @Column(name = "seat_column", nullable = false)
    private int column;

    @Column(name = "status", nullable = false)
    private boolean status;
}
