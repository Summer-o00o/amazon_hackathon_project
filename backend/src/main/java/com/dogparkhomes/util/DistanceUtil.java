package com.dogparkhomes.util;

public final class DistanceUtil {

    private DistanceUtil() {
    }

    // Calculates distance between two points using the Haversine formula, in miles (2 decimal places).
    public static double haversineMiles(double lat1, double lon1, double lat2, double lon2) {
        final double R = 3958.8; // Earth's radius in miles
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round((R * c) * 100.0) / 100.0;
    }
}
