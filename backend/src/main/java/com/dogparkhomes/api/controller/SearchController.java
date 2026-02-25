package com.dogparkhomes.api.controller;

import com.dogparkhomes.api.dto.request.SearchRequestDto;
import com.dogparkhomes.api.dto.response.ListingResponseDto;
import com.dogparkhomes.api.dto.response.SearchFiltersDto;
import com.dogparkhomes.infrastructure.nova.NovaService;
import com.dogparkhomes.infrastructure.google.GooglePlacesService;
import com.dogparkhomes.infrastructure.realestate.RealEstateService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dogparkhomes.api.dto.response.DogParkDto;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final NovaService novaService;
    private final GooglePlacesService googlePlacesService;
    private final RealEstateService realEstateService;

    public SearchController(NovaService novaService, GooglePlacesService googlePlacesService, RealEstateService realEstateService) {
        this.novaService = novaService;
        this.googlePlacesService = googlePlacesService;
        this.realEstateService = realEstateService;
    }

    @PostMapping("/search")
    public List<ListingResponseDto> search(@RequestBody SearchRequestDto request) {
        String query = request.getQuery();
        SearchFiltersDto filters = novaService.parseUserQuery(query);

        // call Google Places API
        List<DogParkDto> parks =
                googlePlacesService.searchDogParks(filters.getLocation());
                
        // call Real Estate API
        return realEstateService.searchHouses(parks,2);
    }

}
