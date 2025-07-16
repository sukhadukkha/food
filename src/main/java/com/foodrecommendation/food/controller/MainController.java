package com.foodrecommendation.food.controller;

import com.foodrecommendation.food.service.KakaoLocalApiService;
import com.foodrecommendation.food.dto.RestaurantDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
public class MainController {

    @Autowired
    private KakaoLocalApiService kakaoService;

    @GetMapping("/")
    public String redirectToMain() {
        return "redirect:/main";
    }


    @GetMapping("/main")
    public String showMain(Model model) {
        List<RestaurantDto> restaurants = kakaoService.search("홍대맛집"); // 키워드는 나중에 바꿔도 돼요
        model.addAttribute("restaurants", restaurants);
        return "main";
    }


    @GetMapping("/main/{category}")
    public String showRestaurants(Model model, @PathVariable String category) {
        List<RestaurantDto> restaurants = kakaoService.search(category); // 키워드는 나중에 바꿔도 돼요
        model.addAttribute("restaurants", restaurants);
        log.info("카테고리: {}", category);
        return "main";
    }

}
