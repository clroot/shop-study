package io.clroot.study.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDTO {

    private Long id;

    private String itemName;

    private String itemDescription;

    private String imgUrl;

    private Integer price;

    @QueryProjection
    public MainItemDTO(Long id, String itemName, String itemDescription,
                       String imgUrl, Integer price) {
        this.id = id;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
