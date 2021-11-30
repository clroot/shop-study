package io.clroot.study.shop.dto;

import io.clroot.study.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDTO {

    private static ModelMapper modelMapper = new ModelMapper();

    private Long id;

    private String imgName;

    private String originImgName;

    private String imgUrl;

    private Boolean repImg;

    public static ItemImgDTO of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDTO.class);
    }
}
