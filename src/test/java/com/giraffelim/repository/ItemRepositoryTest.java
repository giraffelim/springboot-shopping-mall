package com.giraffelim.repository;

import com.giraffelim.constant.ItemSellStatus;
import com.giraffelim.entity.Item;
import com.giraffelim.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);

            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 저장 테스트")
    void create_item_test() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);

        Item savedItem = itemRepository.save(item);
        assertThat(savedItem).isEqualTo(item);
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    void find_by_item_nm_test() {
        List<Item> findItemList = itemRepository.findByItemNm("테스트 상품1");
        for (Item item : findItemList) {
            System.out.println(item.toString());
            assertThat(item.getItemNm()).isEqualTo("테스트 상품1");
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    void find_by_item_nm_or_item_detail_test() {
        List<Item> findItemList = itemRepository
                .findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");

        for (Item item : findItemList) {
            System.out.println(item.toString());
        }

        assertThat(findItemList).hasSize(2);
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    void find_by_price_less_than_test() {
        List<Item> findItemList = itemRepository.findByPriceLessThan(10005);

        for (Item item : findItemList) {
            System.out.println(item.toString());
        }

        assertThat(findItemList).hasSize(4);
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    void find_by_price_less_than_order_by_price_desc_test() {
        List<Item> findItemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);

        for (Item item : findItemList) {
            System.out.println(item);
        }

        assertThat(findItemList).hasSize(4);
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    void find_by_item_detail_test() {
        List<Item> findItemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");

        for (Item item : findItemList) {
            System.out.println(item.toString());
        }

        assertThat(findItemList).hasSize(10);
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    void find_by_item_detail_by_native_test() {
        List<Item> findItemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");

        for (Item item : findItemList) {
            System.out.println(item.toString());
        }

        assertThat(findItemList).hasSize(10);
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    void queryDslTest() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;

        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> findItemList = query.fetch();

        for (Item item : findItemList) {
            System.out.println(item);
        }

        assertThat(findItemList).hasSize(10);
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    void queryDslTest2() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem qitem = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(qitem.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(qitem.price.gt(price));

        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(qitem.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingList = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements: " + itemPagingList.getTotalElements());

        List<Item> resultItemList = itemPagingList.getContent();
        for (Item item : resultItemList) {
            System.out.println(item.toString());
        }
    }

}