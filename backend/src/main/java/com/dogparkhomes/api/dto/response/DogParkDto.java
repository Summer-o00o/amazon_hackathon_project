package com.dogparkhomes.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DogParkDto {

    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private double rating;
    private int userRatingCount;
}
