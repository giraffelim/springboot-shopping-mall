package com.giraffelim.entity;

import com.giraffelim.entity.audit.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "CART_ITEM")
public class CartItem extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "CART_ITEM_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CART_ID")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    private int count;
}
