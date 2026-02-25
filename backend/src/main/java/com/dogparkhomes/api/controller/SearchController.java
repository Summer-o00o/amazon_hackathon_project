package com.dogparkhomes.api.controller;

import com.dogparkhomes.api.dto.request.SearchRequestDto;
import com.dogparkhomes.infrastructure.nova.NovaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dogparkhomes.api.dto.SearchFiltersDto;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final NovaService novaService;

    public SearchController(NovaService novaService) {
        this.novaService = novaService;
    }

    @PostMapping("/search")
    public SearchFiltersDto search(@RequestBody SearchRequestDto request) {
        String query = request.getQuery();
        return novaService.parseUserQuery(query);
    }

}
