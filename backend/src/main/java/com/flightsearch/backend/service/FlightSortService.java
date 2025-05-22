package com.flightsearch.backend.service;

import com.flightsearch.backend.model.FlightSearchResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class FlightSortService {

    public void sort(List<FlightSearchResponse> results, String sortBy, String sortOrder) {
        Comparator<FlightSearchResponse> comparator;

        if ("duration".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(this::parseDurationToMinutes);
        } else {
            comparator = Comparator.comparing(f -> new BigDecimal(f.getTotalPrice()));
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        results.sort(comparator);
    }

    private long parseDurationToMinutes(FlightSearchResponse flight) {
        long minutes = 0;
        String[] parts = flight.getDuration().split(" ");
        for (String part : parts) {
            if (part.endsWith("h")) {
                minutes += Integer.parseInt(part.replace("h", "")) * 60;
            } else if (part.endsWith("m")) {
                minutes += Integer.parseInt(part.replace("m", ""));
            }
        }
        return minutes;
    }
}
