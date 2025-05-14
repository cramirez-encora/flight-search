package com.flightsearch.backend.service;

import com.flightsearch.backend.config.AmadeusConfig;
import com.flightsearch.backend.model.AirportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AirportService {

    @Autowired
    private AmadeusConfig config;

    @Autowired
    private AmadeusAuthService authService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable(value = "airportSearch", key = "#keyword.toLowerCase()")
    public List<AirportResponse> searchAirports(String keyword) {
        String url = config.getAirportSearchUrl() + "?subType=AIRPORT,CITY&keyword=" + keyword;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        List<AirportResponse> result = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");

            for (Map<String, Object> item : data) {
                String name = (String) item.get("name");
                String iataCode = (String) item.get("iataCode");

                Map<String, Object> address = (Map<String, Object>) item.get("address");
                String city = address != null ? (String) address.get("cityName") : "";
                String country = address != null ? (String) address.get("countryName") : "";

                result.add(new AirportResponse(name, iataCode, city, country));
            }
        }

        return result;
    }
}
