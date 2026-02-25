package com.dogparkhomes.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListingResponseDto {

    private String address;

    private double price;

    private int bedrooms;

    private double bathrooms;

    // private int squareFootage;

    // private String propertyType;
}
