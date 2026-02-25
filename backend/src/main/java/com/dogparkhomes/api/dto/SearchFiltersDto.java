package com.dogparkhomes.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchFiltersDto {

    private String location;

    private String property_type;

    private List<String> amenities;

    private String price_range;

}
