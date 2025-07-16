package com.foodrecommendation.food.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class RestaurantDto {
    private String name;
    private String address;
    private String phone;
}
