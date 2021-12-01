package io.clroot.study.shop.repository;

import io.clroot.study.shop.dto.ItemSearchDTO;
import io.clroot.study.shop.dto.MainItemDTO;
import io.clroot.study.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);

    Page<MainItemDTO> getMainItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);
}
