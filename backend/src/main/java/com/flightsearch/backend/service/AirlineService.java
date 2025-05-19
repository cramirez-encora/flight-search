package com.flightsearch.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

@Service
public class AirlineService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AmadeusAuthService authService;

    public AirlineService(RestTemplate restTemplate, ObjectMapper objectMapper, AmadeusAuthService authService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.authService = authService;
    }

    public Optional<String> getAirlineName(String airlineCode) {
        String url = "https://test.api.amadeus.com/v1/reference-data/airlines?airlineCodes=" + airlineCode;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode data = root.path("data");
            if (data.isArray() && data.size() > 0) {
                JsonNode airline = data.get(0);
                return Optional.ofNullable(airline.path("businessName").asText());
            }
        } catch (Exception e) {
            // Handle exceptions appropriately
        }

        return Optional.empty();
    }
}

