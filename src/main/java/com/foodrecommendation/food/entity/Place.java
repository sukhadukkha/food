package com.foodrecommendation.food.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String phone;

    private String keywords;

    public Place(String name, String address, String phone) {
        this.name=name;
        this.address = address;
        this.phone=phone;
    }

    public void updateFrom(Place other) {
        this.name = other.name;
        this.address = other.address;
        this.phone = other.phone;
        this.keywords = other.keywords;
    }

}
