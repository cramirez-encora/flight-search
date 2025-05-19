package com.flightsearch.backend.service;

import com.flightsearch.backend.config.AmadeusConfig;
import com.flightsearch.backend.model.AirportResponse;
import com.flightsearch.backend.model.FlightSearchRequest;
import com.flightsearch.backend.model.FlightSearchResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

@Service
public class FlightSearchService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AmadeusConfig amadeusConfig;
    private final AmadeusAuthService authService;
    private final AirportService airportService;
    private final AirlineService airlineService;

    public FlightSearchService(RestTemplate restTemplate, ObjectMapper objectMapper,
                               AmadeusConfig amadeusConfig, AmadeusAuthService authService,
                               AirportService airportService, AirlineService airlineService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.amadeusConfig = amadeusConfig;
        this.authService = authService;
        this.airportService = airportService;
        this.airlineService = airlineService;
    }


    @Cacheable(value = "flightSearchCache", key = "#request.toString()")
    public List<FlightSearchResponse> searchFlights(FlightSearchRequest request) {
        String token = authService.getAccessToken();
        String url = buildFlightSearchUrl(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            List<FlightSearchResponse> results = parseFlightResults(root, request);
            sortFlightResults(results, request.getSortBy(), request.getSortOrder());
            return results;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse flight search response: " + e.getMessage(), e);
        }
    }

    private String buildFlightSearchUrl(FlightSearchRequest request) {
        StringBuilder sb = new StringBuilder(amadeusConfig.getFlightSearchUrl());
        sb.append("?originLocationCode=").append(request.getOriginLocationCode());
        sb.append("&destinationLocationCode=").append(request.getDestinationLocationCode());
        sb.append("&departureDate=").append(request.getDepartureDate());
        if (request.getReturnDate() != null) {
            sb.append("&returnDate=").append(request.getReturnDate());
        }
        sb.append("&adults=").append(request.getAdults());
        sb.append("&nonStop=").append(request.isNonStop());
        sb.append("&currencyCode=").append(request.getCurrencyCode());
        sb.append("&max=10");
        return sb.toString();
    }

    private List<FlightSearchResponse> parseFlightResults(JsonNode root, FlightSearchRequest request) {
        List<FlightSearchResponse> results = new ArrayList<>();
        JsonNode data = root.get("data");
        if (data == null || !data.isArray()) return results;

        for (JsonNode offer : data) {
            try {
                JsonNode itinerary = offer.get("itineraries").get(0);
                JsonNode segments = itinerary.get("segments");
                JsonNode firstSegment = segments.get(0);
                JsonNode lastSegment = segments.get(segments.size() - 1);

                String departureTime = firstSegment.get("departure").get("at").asText();
                String arrivalTime = lastSegment.get("arrival").get("at").asText();

                String departureAirportCode = firstSegment.get("departure").get("iataCode").asText();
                String departureName = airportService.getAirportByIATACode(departureAirportCode)
                        .map(AirportResponse::getName)
                        .orElse("Unknown");

                String arrivalAirportCode = lastSegment.get("arrival").get("iataCode").asText();
                String arrivalName = airportService.getAirportByIATACode(arrivalAirportCode)
                        .map(AirportResponse::getName)
                        .orElse("Unknown");

                String airlineCode = firstSegment.get("carrierCode").asText();
                String airlineName = airlineService.getAirlineName(airlineCode).orElse(airlineCode);

                String operatingCode = firstSegment.has("operating") ?
                        firstSegment.get("operating").get("carrierCode").asText() : airlineCode;

                String operatingName = airlineService.getAirlineName(operatingCode).orElse(operatingCode);

                String isoDuration = itinerary.get("duration").asText();
                String formattedDuration = formatDuration(isoDuration);
                int stops = segments.size() - 1;

                String pricePerTraveler = offer.get("price").get("total").asText();
                double total = Double.parseDouble(pricePerTraveler) * request.getAdults();
                String totalPrice = String.format("%.2f", total);

                FlightSearchResponse flight = new FlightSearchResponse(
                        departureTime, arrivalTime,
                        departureName, departureAirportCode,
                        arrivalName, arrivalAirportCode,
                        airlineName, airlineCode,
                        operatingCode.equals(airlineCode) ? null : operatingName,
                        operatingCode.equals(airlineCode) ? null : operatingCode,
                        formattedDuration, stops,
                        totalPrice, pricePerTraveler
                );

                results.add(flight);
            } catch (Exception e) {
                // Skip malformed entries
            }
        }
        return results;
    }

    private String formatDuration(String isoDuration) {
        Duration duration = Duration.parse(isoDuration);
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return (hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m" : "").trim();
    }

    private long parseDurationToMinutes(String durationStr) {
        long minutes = 0;
        String[] parts = durationStr.split(" ");
        for (String part : parts) {
            if (part.endsWith("h")) {
                minutes += Integer.parseInt(part.replace("h", "")) * 60;
            } else if (part.endsWith("m")) {
                minutes += Integer.parseInt(part.replace("m", ""));
            }
        }
        return minutes;
    }

    private void sortFlightResults(List<FlightSearchResponse> results, String sortBy, String sortOrder) {
        Comparator<FlightSearchResponse> comparator;

        if ("duration".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(f -> parseDurationToMinutes(f.getDuration()));
        } else {
            comparator = Comparator.comparing(f -> new BigDecimal(f.getTotalPrice()));
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        results.sort(comparator);
    }
}
