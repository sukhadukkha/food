package com.foodrecommendation.food.repository;


import com.foodrecommendation.food.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    // 중복 저장 방지용 예시 (이름 + 위치 조합으로 중복 검사)
    Optional<com.foodrecommendation.food.entity.Place> findByNameAndAddress(String name, String address);
    List<Place> findByNameContainingOrKeywordsContaining(String name, String keywords);


}

