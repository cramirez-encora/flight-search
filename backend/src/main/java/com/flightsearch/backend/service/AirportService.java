package com.flightsearch.backend.service;

import com.flightsearch.backend.config.AmadeusConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AirportService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AmadeusAuthService amadeusAuthService;

    @Autowired
    private AmadeusConfig amadeusConfig;

    public String searchAirports(String keyword) {
        String url = amadeusConfig.getAirportSearchUrl() + "?subType=AIRPORT&keyword=" + keyword;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(amadeusAuthService.getAccessToken());
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to search airports from Amadeus");
        }
    }
}
