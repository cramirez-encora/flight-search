package com.flightsearch.backend.service;

import com.flightsearch.backend.config.AmadeusConfig;
import com.flightsearch.backend.model.AirportResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AirportService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AmadeusConfig config;
    private final AmadeusAuthService authService;

    public AirportService(RestTemplate restTemplate, ObjectMapper objectMapper,
                          AmadeusConfig config, AmadeusAuthService authService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.config = config;
        this.authService = authService;
    }

    // 🔍 Search by keyword (airport/city name or code)
    @Cacheable(value = "airportSearch", key = "#keyword.toLowerCase()")
    public List<AirportResponse> searchAirports(String keyword) {
        String url = config.getAirportSearchUrl() + "?subType=AIRPORT,CITY&keyword=" + keyword;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        List<AirportResponse> result = new ArrayList<>();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode data = root.get("data");

            if (data != null && data.isArray()) {
                for (JsonNode node : data) {
                    String name = node.get("name").asText();
                    String iataCode = node.get("iataCode").asText();
                    String city = node.get("address").get("cityName").asText("");
                    String country = node.get("address").get("countryName").asText("");

                    result.add(new AirportResponse(name, iataCode, city, country));
                }
            }
        } catch (Exception e) {

        }

        return result;
    }

    public Optional<AirportResponse> getAirportByIATACode(String iataCode) {
        String url = config.getAirportSearchUrl() + "?subType=AIRPORT,CITY&keyword=" + iataCode;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode data = root.get("data");

            if (data != null && data.isArray()) {
                for (JsonNode node : data) {
                    if (node.get("iataCode").asText().equalsIgnoreCase(iataCode)) {
                        String name = node.get("name").asText();
                        String city = node.get("address").get("cityName").asText("");
                        String country = node.get("address").get("countryName").asText("");
                        return Optional.of(new AirportResponse(name, iataCode, city, country));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching airport for code " + iataCode + ": " + e.getMessage());
        }

        return Optional.empty();
    }
}
