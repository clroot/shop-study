package io.clroot.study.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemDTO {

    private Long id;

    private String itemName;

    private Integer price;

    private String itemDescription;

    private String itemSellStatus;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
