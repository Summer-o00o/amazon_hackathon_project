package com.dogparkhomes.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListingResponseDto {

    private String address;

    private double price;

    private int bedrooms;

    private double bathrooms;

    @JsonIgnore
    private double latitude;

    @JsonIgnore
    private double longitude;

    private String nearestDogParkName;

    private double nearestDogParkRating;

    private double distanceToDogPark;
}
