package io.clroot.study.shop.entity;

import io.clroot.study.shop.constant.ItemSellStatus;
import io.clroot.study.shop.dto.ItemFormDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemName;

    @Column(nullable = false, name = "price")
    private int price;

    @Column(nullable = false)
    private int stockQuantity;

    @Lob
    @Column(nullable = false)
    private String itemDescription;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    public void updateItem(ItemFormDTO itemFormDTO) {
        this.itemName = itemFormDTO.getItemName();
        this.price = itemFormDTO.getPrice();
        this.stockQuantity = itemFormDTO.getStockQuantity();
        this.itemDescription = itemFormDTO.getItemDescription();
        this.itemSellStatus = itemFormDTO.getItemSellStatus();
    }
}
