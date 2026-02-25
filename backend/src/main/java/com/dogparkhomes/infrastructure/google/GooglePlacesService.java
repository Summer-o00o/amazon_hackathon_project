package com.dogparkhomes.infrastructure.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GooglePlacesService {

    private final RestTemplate restTemplate = new RestTemplate();

    // TODO: move to application.properties
    private final String API_KEY = "AIzaSyDXnr-0m8TMs0I8hlDMO7xF1qdi52kiLb8";

    public JsonNode searchDogParks(String location) {

        String url = "https://places.googleapis.com/v1/places:searchText";

        String requestBody = """
                {
                  "textQuery": "dog park in %s"
                }
                """.formatted(location);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", API_KEY);
        headers.set("X-Goog-FieldMask", "places.displayName,places.formattedAddress,places.location");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Google Places response", e);
        }
    }
}
