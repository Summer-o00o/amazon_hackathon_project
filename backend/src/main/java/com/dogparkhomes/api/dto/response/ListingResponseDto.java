package com.dogparkhomes.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class ListingResponseDto implements Serializable {

    private String id;

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
    
    private String imageUrl;
}
