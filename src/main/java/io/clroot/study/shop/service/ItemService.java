package io.clroot.study.shop.service;

import io.clroot.study.shop.dto.ItemFormDTO;
import io.clroot.study.shop.dto.ItemImgDTO;
import io.clroot.study.shop.dto.ItemSearchDTO;
import io.clroot.study.shop.entity.Item;
import io.clroot.study.shop.entity.ItemImg;
import io.clroot.study.shop.repository.ItemImgRepository;
import io.clroot.study.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgRepository itemImgRepository;

    private final ItemImgService itemImgService;

    @Transactional
    public Long saveItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemFormDTO.createItem();
        itemRepository.save(item);

        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            itemImg.setRepImg(i == 0);

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    public ItemFormDTO getItemDetail(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        ItemFormDTO itemFormDTO = ItemFormDTO.of(item);
        List<ItemImgDTO> itemImgDTOList = itemImgList.stream()
                .map(ItemImgDTO::of)
                .collect(Collectors.toList());

        itemFormDTO.setItemImgDTOList(itemImgDTOList);

        return itemFormDTO;
    }

    @Transactional
    public Long updateItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemRepository.findById(itemFormDTO.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDTO);

        List<Long> itemImgIdList = itemFormDTO.getItemImgIdList();

        for (int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIdList.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }

    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDTO, pageable);
    }
}