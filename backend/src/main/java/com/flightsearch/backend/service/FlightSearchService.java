package com.flightsearch.backend.service;

import com.flightsearch.backend.config.AmadeusConfig;
import com.flightsearch.backend.model.FlightSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

@Service
public class FlightSearchService {

    @Autowired
    private AmadeusAuthService authService;

    @Autowired
    private AmadeusConfig config;

    private final RestTemplate restTemplate = new RestTemplate();

    public String searchFlights(FlightSearchRequest request) {
        String accessToken = authService.getAccessToken();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(config.getFlightSearchUrl())
                .queryParam("originLocationCode", request.getOriginLocationCode())
                .queryParam("destinationLocationCode", request.getDestinationLocationCode())
                .queryParam("departureDate", request.getDepartureDate())
                .queryParam("adults", request.getAdults())
                .queryParam("currencyCode", request.getCurrencyCode())
                .queryParam("nonStop", request.isNonStop());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}
