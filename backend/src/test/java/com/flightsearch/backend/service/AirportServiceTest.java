package com.flightsearch.backend.service;

import com.flightsearch.backend.config.AmadeusConfig;
import com.flightsearch.backend.model.AirportResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AirportServiceTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private AmadeusConfig config;
    private AmadeusAuthService authService;
    private AirportService airportService;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        objectMapper = new ObjectMapper();
        config = mock(AmadeusConfig.class);
        authService = mock(AmadeusAuthService.class);
        airportService = new AirportService(restTemplate, objectMapper, config, authService);
    }

    @Test
    void searchAirports_shouldReturnListOfAirports() throws Exception {
        String keyword = "Mexico";
        String fakeUrl = "https://fake.api/amadeus/airports";
        String mockJson = """
        {
          "data": [
            {
              "name": "Benito Juarez Intl",
              "iataCode": "MEX",
              "address": {
                "cityName": "Mexico City",
                "countryName": "Mexico"
              }
            }
          ]
        }
        """;

        when(config.getAirportSearchUrl()).thenReturn(fakeUrl);
        when(authService.getAccessToken()).thenReturn("mock_token");

        ResponseEntity<String> responseEntity = new ResponseEntity<>(mockJson, HttpStatus.OK);
        when(restTemplate.exchange(
                ArgumentMatchers.eq(fakeUrl + "?subType=AIRPORT,CITY&keyword=" + keyword),
                ArgumentMatchers.eq(HttpMethod.GET),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.eq(String.class)
        )).thenReturn(responseEntity);

        List<AirportResponse> airports = airportService.searchAirports(keyword);

        assertEquals(1, airports.size());
        AirportResponse airport = airports.get(0);
        assertEquals("Benito Juarez Intl", airport.getName());
        assertEquals("MEX", airport.getIataCode());
        assertEquals("Mexico City", airport.getCity());
        assertEquals("Mexico", airport.getCountry());
    }

    @Test
    void getAirportByIATACode_shouldReturnMatchingAirport() throws Exception {
        String iataCode = "MEX";
        String fakeUrl = "https://fake.api/amadeus/airports";
        String mockJson = """
        {
          "data": [
            {
              "name": "Benito Juarez Intl",
              "iataCode": "MEX",
              "address": {
                "cityName": "Mexico City",
                "countryName": "Mexico"
              }
            }
          ]
        }
        """;

        when(config.getAirportSearchUrl()).thenReturn(fakeUrl);
        when(authService.getAccessToken()).thenReturn("mock_token");

        ResponseEntity<String> responseEntity = new ResponseEntity<>(mockJson, HttpStatus.OK);
        when(restTemplate.exchange(
                ArgumentMatchers.eq(fakeUrl + "?subType=AIRPORT,CITY&keyword=" + iataCode),
                ArgumentMatchers.eq(HttpMethod.GET),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.eq(String.class)
        )).thenReturn(responseEntity);

        Optional<AirportResponse> result = airportService.getAirportByIATACode(iataCode);

        assertTrue(result.isPresent());
        AirportResponse airport = result.get();
        assertEquals("MEX", airport.getIataCode());
        assertEquals("Benito Juarez Intl", airport.getName());
        assertEquals("Mexico City", airport.getCity());
        assertEquals("Mexico", airport.getCountry());
    }

    @Test
    void getAirportByIATACode_shouldReturnEmptyIfNoMatch() throws Exception {
        String iataCode = "XXX";
        String fakeUrl = "https://fake.api/amadeus/airports";
        String mockJson = """
        {
          "data": [
            {
              "name": "Other Airport",
              "iataCode": "YYY",
              "address": {
                "cityName": "Other City",
                "countryName": "Other Country"
              }
            }
          ]
        }
        """;

        when(config.getAirportSearchUrl()).thenReturn(fakeUrl);
        when(authService.getAccessToken()).thenReturn("mock_token");

        ResponseEntity<String> responseEntity = new ResponseEntity<>(mockJson, HttpStatus.OK);
        when(restTemplate.exchange(
                ArgumentMatchers.eq(fakeUrl + "?subType=AIRPORT,CITY&keyword=" + iataCode),
                ArgumentMatchers.eq(HttpMethod.GET),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.eq(String.class)
        )).thenReturn(responseEntity);

        Optional<AirportResponse> result = airportService.getAirportByIATACode(iataCode);

        assertTrue(result.isEmpty());
    }
}
