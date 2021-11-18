package io.clroot.study.shop.entity;

import io.clroot.study.shop.constant.ItemSellStatus;
import io.clroot.study.shop.repository.ItemRepository;
import io.clroot.study.shop.repository.MemberRepository;
import io.clroot.study.shop.repository.OrderItemRepository;
import io.clroot.study.shop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        Item item = new Item();

        item.setItemName("test");
        item.setPrice(10000);
        item.setItemDescription("test description");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockQuantity(100);

        return item;
    }

    public Order createOrder() {
        Order order = new Order();

        IntStream.range(0, 3).forEach(i -> {
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setQuantity(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        });

        Member member = new Member();
        memberRepository.save(member);

        orderRepository.saveAndFlush(order);
        return order;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = createOrder();

        Order savedOrder =
                orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);

        assertEquals(3, savedOrder.getOrderItems().size());
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = createOrder();
        int orderItemCount = orderItemRepository.findByOrderId(order.getId()).size();

        order.getOrderItems().remove(0);
        em.flush();

        int changedOrderItemCount = orderItemRepository.findByOrderId(order.getId()).size();
        assertNotEquals(changedOrderItemCount, orderItemCount);
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem =
                orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class: " + orderItem.getOrder().getClass());
        System.out.println("====================");
        System.out.println("OrderDate: " + orderItem.getOrder().getOrderDate());
        System.out.println("====================");
    }
}