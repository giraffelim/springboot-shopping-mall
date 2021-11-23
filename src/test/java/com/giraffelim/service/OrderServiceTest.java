package com.giraffelim.service;

import com.giraffelim.constant.ItemSellStatus;
import com.giraffelim.constant.OrderStatus;
import com.giraffelim.dto.OrderDto;
import com.giraffelim.entity.Item;
import com.giraffelim.entity.Member;
import com.giraffelim.entity.Order;
import com.giraffelim.exception.OutOfStockException;
import com.giraffelim.repository.ItemRepository;
import com.giraffelim.repository.MemberRepository;
import com.giraffelim.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;


@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Item saveItem() {
        Item item = new Item();
        item.setItemNm("청바지");
        item.setPrice(49000);
        item.setStockNumber(10);
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setItemDetail("신축성이 좋은 청바지");
        return itemRepository.save(item);
    }

    private Member saveMember() {
        Member member = new Member();
        member.setEmail("test2@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    void order_test() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(item.getId());
        orderDto.setCount(5);

        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        int totalPrice = orderDto.getCount() * item.getPrice();

        Assertions.assertThat(totalPrice).isEqualTo(order.getTotalPrice());
    }

    @Test
    @DisplayName("주문 실패 테스트 - 상품 재고 수량 부족")
    void order_fail_when_out_of_stock_exception_test() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(item.getId());
        orderDto.setCount(300);

        Assertions.assertThatThrownBy(() ->
                orderService.order(orderDto, member.getEmail())).isInstanceOf(OutOfStockException.class)
                .hasMessageContaining("상품의 재고가 부족합니다.");
    }

    @Test
    @DisplayName("주문 취소 테스트")
    void order_cancel_test() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(item.getId());
        orderDto.setCount(5);

        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);

        Assertions.assertThat(item.getStockNumber()).isEqualTo(10);
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }



}