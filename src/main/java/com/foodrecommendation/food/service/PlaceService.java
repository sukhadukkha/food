package com.foodrecommendation.food.service;

import com.foodrecommendation.food.entity.Place;
import com.foodrecommendation.food.repository.PlaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Transactional
    public void saveIfNotExists(List<Place> places) {
        for (Place place : places) {
            boolean exists = placeRepository
                    .findByNameAndAddress(place.getName(), place.getAddress())
                    .isPresent();
            if (!exists) {
                placeRepository.save(place);
            }
        }
    }

    public List<Place> findByNameOrKeywordsContaining(String keyword) {
        return placeRepository.findByNameContainingOrKeywordsContaining(keyword, keyword);
    }

}
