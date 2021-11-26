package com.giraffelim.repository;

import com.giraffelim.dto.CartDetailDto;
import com.giraffelim.dto.QCartDetailDto;
import com.giraffelim.entity.QCartItem;
import com.giraffelim.entity.QItem;
import com.giraffelim.entity.QItemImg;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class CartItemRepositoryCustomImpl implements CartItemRepositoryCustom {

    JPAQueryFactory queryFactory;

    public CartItemRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CartDetailDto> getCartList(Long cartId) {

        QCartItem cartItem = QCartItem.cartItem;
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<CartDetailDto> results = queryFactory.select(new QCartDetailDto(
                        cartItem.id,
                        item.itemNm,
                        item.price,
                        cartItem.count,
                        itemImg.imgUrl)
                ).from(cartItem)
                .join(cartItem.item, item)
                .join(itemImg).on(cartItem.item.id.eq(itemImg.item.id))
                .where(cartItem.cart.id.eq(cartId), itemImg.repImgYn.eq("Y"))
                .orderBy(cartItem.regTime.desc())
                .fetchResults();

        return results.getResults();
    }
}
