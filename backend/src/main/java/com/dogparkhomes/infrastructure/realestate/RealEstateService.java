package com.dogparkhomes.infrastructure.realestate;

import com.dogparkhomes.api.dto.response.DogParkDto;
import com.dogparkhomes.api.dto.response.ListingResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RealEstateService {

    @Value("${realestate.api.api-key:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ListingResponseDto> searchHouses(List<DogParkDto> dogParks, double radiusMiles) {
        List<ListingResponseDto> listings = new ArrayList<>();
        for (DogParkDto dogPark : dogParks) {
            String url = String.format(
                    "https://api.rentcast.io/v1/listings/sale?latitude=%f&longitude=%f&radius=%f",
                    dogPark.getLatitude(),
                    dogPark.getLongitude(),
                    radiusMiles
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Api-Key", apiKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            String body = response.getBody();
            listings.addAll(parseListings(body));
        }
        return listings;
    }

    private List<ListingResponseDto> parseListings(String json) {
        List<ListingResponseDto> listings = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(json);
            for (JsonNode node : root) {
                ListingResponseDto dto = new ListingResponseDto();
                dto.setAddress(node.path("formattedAddress").asText());
                dto.setPrice(node.path("price").asDouble());
                dto.setBedrooms(node.path("bedrooms").asInt());
                dto.setBathrooms(node.path("bathrooms").asDouble());
                listings.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse houses", e);
        }
        return listings;
    }
}
