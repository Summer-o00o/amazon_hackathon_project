package com.dogparkhomes.infrastructure.google;

import com.dogparkhomes.infrastructure.image.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StreetViewService {

    private static final Logger log = LoggerFactory.getLogger(StreetViewService.class);
    private static final String STREET_VIEW_BASE_URL ="https://maps.googleapis.com/maps/api/streetview";

    private final ImageService imageService;

    @Value("${google.places.api-key}")
    private String apiKey;

    public StreetViewService(ImageService imageService) {
        this.imageService = imageService;
    }

    //download the street view image from the url and return the local url
    public String getStreetViewImage(String listingId, double latitude, double longitude) {
        String baseId = String.valueOf(Math.abs((long) listingId.hashCode()));

        String remoteUrl = STREET_VIEW_BASE_URL
                + "?size=800x600"
                + "&location=" + latitude + "," + longitude
                + "&fov=80"
                + "&pitch=10"
                + "&key=" + apiKey;

        try {
            String localUrl = imageService.getOrDownloadImage(baseId, remoteUrl);
            if (localUrl != null) {
                return localUrl;
            }
        } catch (Exception e) {
            log.warn("Street View image failed for {}: {}", listingId, e.getMessage());
        }

        String fallback = "https://picsum.photos/seed/" + baseId + "/800/600";
        try {
            String fallbackId = baseId + "_p";
            String fallbackUrl = imageService.getOrDownloadImage(fallbackId, fallback);
            return fallbackUrl != null ? fallbackUrl : null;
        } catch (Exception e) {
            log.warn("Picsum fallback failed for {}: {}", listingId, e.getMessage());
            return null;
        }
    }
}

