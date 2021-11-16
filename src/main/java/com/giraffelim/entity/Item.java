package com.giraffelim.entity;

import com.giraffelim.constant.ItemSellStatus;
import com.giraffelim.dto.ItemFormDto;
import com.giraffelim.entity.audit.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ITEM")
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;    // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm;  // 상품명

    @Column(name = "PRICE", nullable = false)
    private int price;  // 가격

    @Column(nullable = false)
    private int stockNumber;    // 재고수량

    @Lob
    private String itemDetail;  // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

}
