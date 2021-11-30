package io.clroot.study.shop.dto;

import io.clroot.study.shop.constant.ItemSellStatus;
import io.clroot.study.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDTO {

    private static ModelMapper modelMapper = new ModelMapper();

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemName;

    @NotBlank(message = "상품 설명은 필수 입력 값입니다.")
    private String itemDescription;

    @NotNull(message = "상품 가격은 필수 입력 값입니다.")
    private Integer price;

    @NotNull(message = "상품 재고는 필수 입력 값입니다.")
    private Integer stockQuantity;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();

    private List<Long> itemImgIdList = new ArrayList<>();

    public static ItemFormDTO of(Item item) {
        return modelMapper.map(item, ItemFormDTO.class);
    }

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }
}
