package com.dogparkhomes.api.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DogParkAnalysisDto implements Serializable {

    private int parkingScore;

    private int crowdedScore;

    private int cleanlinessScore;

    private int dogFriendlinessScore;

    private int parkSizeScore;
}