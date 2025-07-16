package com.foodrecommendation.food.service;

import com.foodrecommendation.food.dto.RestaurantDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.*;

import java.util.*;

@Service
public class KakaoLocalApiService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public List<RestaurantDto> search(String keyword) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + keyword;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = new RestTemplate().exchange(
                url, HttpMethod.GET, entity, String.class
        );

        JSONObject body = new JSONObject(response.getBody());
        JSONArray documents = body.getJSONArray("documents");

        List<RestaurantDto> result = new ArrayList<>();
        for (int i = 0; i < documents.length(); i++) {
            JSONObject obj = documents.getJSONObject(i);
            result.add(new RestaurantDto(
                    obj.getString("place_name"),
                    obj.getString("address_name"),
                    obj.optString("phone", "없음")
            ));
        }

        return result;
    }
}
