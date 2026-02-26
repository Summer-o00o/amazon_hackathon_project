package com.dogparkhomes.api.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.dogparkhomes.api.dto.response.DogParkAnalysisDto;

@Getter
@Setter
public class DogParkDto {

    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private double rating;
    private int userRatingCount;
    private List<String> reviews;
    private DogParkAnalysisDto analysis;
}
