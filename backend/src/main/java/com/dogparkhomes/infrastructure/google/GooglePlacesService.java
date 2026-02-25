package com.dogparkhomes.infrastructure.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.dogparkhomes.api.dto.response.DogParkDto;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class GooglePlacesService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.places.api-key}")
    private String apiKey;

    public List<DogParkDto> searchDogParks(String location) {

        String url = "https://places.googleapis.com/v1/places:searchText";

        String requestBody = """
                {
                  "textQuery": "dog park in %s"
                }
                """.formatted(location);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", apiKey);
        headers.set("X-Goog-FieldMask", "places.displayName,places.formattedAddress,places.location,places.rating,places.userRatingCount");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {

            ObjectMapper mapper = new ObjectMapper();
        
            JsonNode root = mapper.readTree(response.getBody());
        
            List<DogParkDto> parks = new ArrayList<>();
        
            for (JsonNode place : root.path("places")) {
        
                DogParkDto dto = new DogParkDto();
        
                dto.setName(
                    place.path("displayName").path("text").asText()
                );
        
                dto.setAddress(
                    place.path("formattedAddress").asText()
                );
        
                dto.setLatitude(
                    place.path("location").path("latitude").asDouble()
                );
        
                dto.setLongitude(
                    place.path("location").path("longitude").asDouble()
                );
        
                dto.setRating(
                    place.path("rating").asDouble(0)
                );
        
                dto.setUserRatingCount(
                    place.path("userRatingCount").asInt(0)
                );
        
                parks.add(dto);
            }
        
            // filter rating >= 4.8
            parks.removeIf(p -> p.getRating() < 4.8);
        
            // sort by rating desc
            parks.sort(
                Comparator.comparingDouble(DogParkDto::getRating)
                    .reversed()
            );
        
            // return top 2 to test the response
            return parks.stream().limit(2).toList();
        
        } catch (Exception e) {
        
            throw new RuntimeException("Failed to parse Google Places response", e);
        }
    }
}
