package com.giraffelim.entity;

import com.giraffelim.entity.audit.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "CART")
@Getter
@Setter
@ToString
public class Cart extends BaseTimeEntity {

    @Id
    @Column(name = "CART_ID")
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

}
