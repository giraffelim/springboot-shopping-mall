package com.giraffelim.repository;

import com.giraffelim.constant.ItemSellStatus;
import com.giraffelim.entity.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

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

}