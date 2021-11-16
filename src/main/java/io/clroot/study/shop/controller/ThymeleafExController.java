package io.clroot.study.shop.controller;

import io.clroot.study.shop.dto.ItemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafExController {

    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model) {
        model.addAttribute("data", "타임리프 예제 입니다.");
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping("/ex02")
    public String thymeleafEx02(Model model) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemName("아이템 이름");
        itemDTO.setItemDescription("아이템 설명");
        itemDTO.setPrice(10000);
        itemDTO.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDTO", itemDTO);

        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping("/ex03")
    public String thymeleafEx03(Model model) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemName("아이템 이름" + i);
            itemDTO.setItemDescription("아이템 설명" + i);
            itemDTO.setPrice(10000 + i);
            itemDTO.setRegTime(LocalDateTime.now());

            itemDTOList.add(itemDTO);
        });

        model.addAttribute("itemDTOList", itemDTOList);

        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping("/ex04")
    public String thymeleafEx04(Model model) {
        List<ItemDTO> itemDTOList = new ArrayList<>();

        IntStream.range(0, 10).forEach(i -> {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemName("아이템 이름" + i);
            itemDTO.setItemDescription("아이템 설명" + i);
            itemDTO.setPrice(10000 + i);
            itemDTO.setRegTime(LocalDateTime.now());

            itemDTOList.add(itemDTO);
        });

        model.addAttribute("itemDTOList", itemDTOList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping("/ex05")
    public String thymeleafEx05() {
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping("/ex06")
    public String thymeleafEx06(String param1, String param2, Model model) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);

        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping("/ex07")
    public String thymeleafEx07() {
        return "thymeleafEx/thymeleafEx07";
    }
}
