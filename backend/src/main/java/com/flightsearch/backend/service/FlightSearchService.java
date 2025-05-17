package com.flightsearch.backend.service;

import com.flightsearch.backend.config.AmadeusConfig;
import com.flightsearch.backend.model.AirportResponse;
import com.flightsearch.backend.model.FlightSearchRequest;
import com.flightsearch.backend.model.FlightSearchResponse;
import com.flightsearch.backend.service.AmadeusAuthService;
import com.flightsearch.backend.service.AirlineService;
import com.flightsearch.backend.service.AirportService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
            return parseFlightResults(root, request);

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

                String duration = formatDuration(itinerary.get("duration").asText());
                int stops = segments.size() - 1;

                String pricePerTraveler = offer.get("price").get("total").asText();
                double total = Double.parseDouble(pricePerTraveler) * request.getAdults();
                String totalPrice = String.format("%.2f", total);

                results.add(new FlightSearchResponse(
                        departureTime, arrivalTime,
                        departureName, departureAirportCode,
                        arrivalName, arrivalAirportCode,
                        airlineName, airlineCode,
                        operatingCode.equals(airlineCode) ? null : operatingName,
                        operatingCode.equals(airlineCode) ? null : operatingCode,
                        duration, stops,
                        totalPrice, pricePerTraveler
                ));
            } catch (Exception e) {
                // Skip malformed entries
            }


        }
        return results;
    }
    private String formatDuration(String isoDuration) {
        // Converts ISO 8601 duration like PT4H45M to 4h 45m
        String duration = isoDuration.replace("PT", "")
                .replace("H", "h ")
                .replace("M", "m")
                .trim();
        return duration.isEmpty() ? "0m" : duration;
    }
}
