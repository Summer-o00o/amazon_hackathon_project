package com.dogparkhomes.api.controller;

import com.dogparkhomes.api.dto.request.SearchRequestDto;
import com.dogparkhomes.infrastructure.nova.NovaService;
import com.dogparkhomes.infrastructure.google.GooglePlacesService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dogparkhomes.api.dto.SearchFiltersDto;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final NovaService novaService;
    private final GooglePlacesService googlePlacesService;

    public SearchController(NovaService novaService, GooglePlacesService googlePlacesService) {
        this.novaService = novaService;
        this.googlePlacesService = googlePlacesService;
    }

    @PostMapping("/search")
    public SearchFiltersDto search(@RequestBody SearchRequestDto request) {
        String query = request.getQuery();
        SearchFiltersDto filters = novaService.parseUserQuery(query);

        // call Google Places API
        JsonNode dogParks = googlePlacesService.searchDogParks(filters.getLocation());

        System.out.println("Dog parks response: " + dogParks.toString());

        return filters;
    }

}
