package com.dogparkhomes.infrastructure.realestate;

import com.dogparkhomes.api.dto.response.ListingResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.dogparkhomes.infrastructure.image.ImageService;
import com.dogparkhomes.infrastructure.google.StreetViewService;
import java.util.ArrayList;
import java.util.List;

@Component
public class RentCastClient {

    @Value("${realestate.api.api-key:}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ImageService imageService;
    private final StreetViewService streetViewService;

    public RentCastClient(ImageService imageService,
                          StreetViewService streetViewService) {
        this.imageService = imageService;
        this.streetViewService = streetViewService;
    }

    @Cacheable(
            value = "listing-cache",
            key = "#latitude + ',' + #longitude + ',' + #radiusMiles"
    )
    public List<ListingResponseDto> fetchListings(double latitude, double longitude, double radiusMiles) {
        String url = String.format(
                "https://api.rentcast.io/v1/listings/sale?latitude=%f&longitude=%f&radius=%f",
                latitude,
                longitude,
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
        return parseListings(body);
    }

    private List<ListingResponseDto> parseListings(String json) {
        List<ListingResponseDto> listings = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(json);
            for (JsonNode node : root) {
                ListingResponseDto dto = new ListingResponseDto();
                dto.setId(node.path("id").asText());
                dto.setAddress(node.path("formattedAddress").asText());
                dto.setPrice(node.path("price").asDouble());
                dto.setBedrooms(node.path("bedrooms").asInt());
                dto.setBathrooms(node.path("bathrooms").asDouble());
                dto.setLatitude(node.path("latitude").asDouble());
                dto.setLongitude(node.path("longitude").asDouble());

                //get the street view image from the url
                String localImageUrl = streetViewService.getStreetViewImage(dto.getId(), dto.getLatitude(), dto.getLongitude());
                dto.setImageUrl(localImageUrl);
                
                //add image local url to the dto, or download the image from the url
                // String rentCastImageUrl = "https://picsum.photos/seed/" + dto.getId().hashCode() + "/800/600";
                // String localImageUrl = imageService.getOrDownloadImage(dto.getId(), rentCastImageUrl);
                // dto.setImageUrl(localImageUrl);
                
                listings.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse RentCast response", e);
        }
        return listings;
    }
}
