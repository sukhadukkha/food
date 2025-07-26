package com.foodrecommendation.food.controller;

import com.foodrecommendation.food.entity.Place;
import com.foodrecommendation.food.repository.PlaceRepository;
import com.foodrecommendation.food.service.KakaoLocalApiService;
import com.foodrecommendation.food.dto.RestaurantDto;
import com.foodrecommendation.food.service.PlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class MainController {

    @Autowired
    private KakaoLocalApiService kakaoService;
    @Autowired
    private PlaceService placeService;
    @Autowired
    private PlaceRepository placeRepository;

    @GetMapping("/")
    public String redirectToMain() {
        return "redirect:/login";
    }


    @GetMapping("/main")
    public String showMain() {
        return "main";
    }

    // 폼 보여주기 (GET)
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("place", new Place());
        return "addForm"; // src/main/resources/templates/addForm.html (템플릿 엔진 쓰는 경우)
    }

    // 폼 제출 처리 (POST)
    @PostMapping("/add")
    public String addPlace(@ModelAttribute Place place) {

        placeRepository.save(place);

        return "redirect:main"; // 저장 후 메인 페이지 또는 검색 페이지로 이동
    }

    @GetMapping("/search")
    public String search(@RequestParam String q, Model model) {
        // q 변수에 검색어가 담겨 들어옴
        List<RestaurantDto> results  = kakaoService.search(q);

        String searchQuery = q;
        // RestaurantDto -> Place로 변환
        List<Place> places = results.stream()   // 1. results 리스트를 스트림으로 변환 (데이터 하나씩 처리 가능)
                .map(dto -> {                       // 2. 각 RestaurantDto(dto)를 Place 객체로 매핑
                    Place place = new Place(dto.getName(), dto.getAddress(), dto.getPhone());  // 3. dto에서 이름, 주소, 전화번호 추출해서 Place 객체 생성

                    // 4. 키워드 문자열 생성 (예: 검색어 + 주소의 시/도 이름 등)
                    String keywordString = extractKeywords(dto, searchQuery);

                    // 5. Place 객체에 키워드 설정
                    place.setKeywords(keywordString);

                    return place;  // 6. 새로 만든 Place 객체 반환
                })
                .toList();  // 7. 매핑된 Place 객체들을 리스트로 다시 수집

        placeService.saveIfNotExists(places);
        // DB에서 검색어로 Place 리스트 조회해서 뷰에 전달
        String processedKeyword = preprocessKeyword(q); // 핵심어 추출
        List<Place> placesFromDb = placeService.findByNameOrKeywordsContaining(processedKeyword);
        System.out.println("DB에서 받아온 결과 개수: " + placesFromDb.size()); //확인용
        model.addAttribute("results", placesFromDb);
        return "searchResult"; // 결과 보여줄 뷰 이름
    }

    @GetMapping("/place/{id}")
    public String showPlaceDetail(@PathVariable Long id, Model model) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다. id = " + id));

        model.addAttribute("place", place);
        return "placeDetail";
    }

    // 수정 폼 보여주기
    @GetMapping("/place/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다. id = " + id));
        model.addAttribute("place", place);
        return "editForm";
    }

    @PostMapping("/place/edit/{id}")
    public String updatePlace(@PathVariable Long id, @ModelAttribute Place updatePlace) {

        log.info("updatePlaceName = {}", updatePlace.getName());
        Place updatedPlace = placeService.updatePlace(id, updatePlace);
        placeRepository.save(updatedPlace);
        return "redirect:/place/" + id;
    }

    @PostMapping("/place/delete/{id}")
    public String deletePlace(@PathVariable Long id) {
        placeRepository.deleteById(id);
        return "redirect:/main";
    }

    private String extractKeywords(RestaurantDto dto, String searchQuery) {

        String address = dto.getAddress();
        String region = "";
        String district = "";

        if (address != null) {
            String[] parts = address.split(" ");
            if (parts.length >= 2) {
                region = parts[0];   // 시/도 → "서울"
                district = parts[1]; // 구/군 → "마포구"
            }
        }

        String processedSearchQuery = preprocessKeyword(searchQuery); // 예: "마포" 등
        return String.join(",", processedSearchQuery, region, district); // "마포,서울,마포구"
    }


    private String preprocessKeyword(String keyword) {
        if (keyword != null && keyword.length() > 2) {
            return keyword.substring(0, 2);  // 핵심어 추출
        }
        return keyword;
    }

}
