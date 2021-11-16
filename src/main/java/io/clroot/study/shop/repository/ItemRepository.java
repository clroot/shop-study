package io.clroot.study.shop.repository;

import io.clroot.study.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

    List<Item> findByItemName(String itemName);

    List<Item> findByItemNameOrItemDescription(String itemName, String itemDescription);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Item i " +
            "where i.itemDescription like %:itemDescription% " +
            "order by i.price desc ")
    List<Item> findByItemDescription(@Param("itemDescription") String itemDescription);

    @Query(value = "select * from item i " +
            "where i.item_description like %:itemDescription% " +
            "order by i.price desc ", nativeQuery = true)
    List<Item> findByItemDescriptionNative(@Param("itemDescription") String itemDescription);
}
