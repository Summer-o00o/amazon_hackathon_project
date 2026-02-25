package com.dogparkhomes.api.dto;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonAlias;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchFiltersDto {

    private String location;

    private String property_type;

    @JsonAlias({"amenities", "nearby_amenities", "close_amenities"})
    private List<String> amenities;

    private String price_range;

}
