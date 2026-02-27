package com.dogparkhomes.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DogParkDto {

    @JsonIgnore
    private String placeId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private double rating;
    private int userRatingCount;
    private List<String> reviews;
    private DogParkAnalysisDto analysis;
}
