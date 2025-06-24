package com.moti.backend.core.order.domain.entity;

import com.moti.backend.core.member.domain.entity.Member;
import com.moti.backend.core.order.domain.type.OrderStatus;
import com.moti.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import lombok.Getter;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_token", nullable = false, unique = true, length = 64)
    private String orderToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;

    private Order(String orderToken, Member member, Long totalAmount, OrderStatus status) {
        this.orderToken = orderToken;
        this.member = member;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public static Order create(String orderToken, Member member, Long totalAmount) {
        return new Order(orderToken, member, totalAmount, OrderStatus.CREATED);
    }
}
