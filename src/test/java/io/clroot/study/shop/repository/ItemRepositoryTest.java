package io.clroot.study.shop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.clroot.study.shop.constant.ItemSellStatus;
import io.clroot.study.shop.entity.Item;
import io.clroot.study.shop.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class ItemRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ItemRepository itemRepository;

    public void createItemList() {
        IntStream.range(0, 10).forEach(i -> {
            Item item = new Item();
            item.setItemName("상품" + i);
            item.setPrice(10000 + i);
            item.setItemDescription("상품" + i + " 설명");
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockQuantity(100 + i);

            itemRepository.save(item);
        });
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemName("상품1");

        assertTrue(itemList.size() > 0);

        for (Item item : itemList) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemText() {
        Item item = new Item();
        item.setItemName("상품1");
        item.setPrice(10000);
        item.setItemDescription("상품1 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockQuantity(100);

        Item savedItem = itemRepository.save(item);

        assertEquals(savedItem.getItemName(), item.getItemName());
        assertEquals(savedItem.getPrice(), item.getPrice());
        assertEquals(savedItem.getItemDescription(), item.getItemDescription());
        assertEquals(savedItem.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(savedItem.getStockQuantity(), item.getStockQuantity());
        System.out.println(savedItem);
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNameOrItemDescriptionTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNameOrItemDescription("상품1", "상품5 설명");
        assertTrue(itemList.size() > 0);

        for (Item item : itemList) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        assertTrue(itemList.size() > 0);
        for (Item item : itemList) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        assertTrue(itemList.size() > 0);
        for (Item item : itemList) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("@Query 를 이용한 상품 조회 테스트")
    public void findByItemDescriptionTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDescription("1 설명");
        assertTrue(itemList.size() > 0);
        for (Item item : itemList) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("nativeQuery 속서을 이용한 상품 조회 테스트")
    public void findByItemDescriptionNativeTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDescriptionNative("1 설명");
        assertTrue(itemList.size() > 0);
        for (Item item : itemList) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트 1")
    public void queryDslTest() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDescription.like("%" + "1 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();

        assertTrue(itemList.size() > 0);
        for (Item item : itemList) {
            System.out.println(item);
        }
    }
}