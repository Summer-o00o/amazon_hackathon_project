package com.dogparkhomes.api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResponseDto {

    private List<ListingResponseDto> listings;
    private List<DogParkDto> dogParks;

    public SearchResponseDto(List<ListingResponseDto> listings, List<DogParkDto> dogParks) {
        this.listings = listings;
        this.dogParks = dogParks;
    }
}
