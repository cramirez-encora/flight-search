package com.flightsearch.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightsearch.backend.config.AmadeusConfig;
import com.flightsearch.backend.model.AirportResponse;
import com.flightsearch.backend.model.FlightSearchRequest;
import com.flightsearch.backend.model.FlightSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightSearchServiceTest {

    @InjectMocks
    private FlightSearchService flightSearchService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AmadeusConfig amadeusConfig;

    @Mock
    private AmadeusAuthService authService;

    @Mock
    private AirportService airportService;

    @Mock
    private AirlineService airlineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchFlights_ReturnParsedResults() throws Exception {

        FlightSearchRequest request = new FlightSearchRequest();
        request.setOriginLocationCode("JFK");
        request.setDestinationLocationCode("LAX");
        request.setDepartureDate(LocalDate.parse("2025-06-01"));
        request.setAdults(1);
        request.setCurrencyCode("USD");
        request.setNonStop(false);

        String token = "dummy-token";
        String url = "https://api.amadeus.com/v2/shopping/flight-offers?originLocationCode=JFK&destinationLocationCode=LAX&departureDate=2025-06-01&adults=1&nonStop=false&currencyCode=USD&max=10";
        String mockJson = "{ \"data\": [ { \"itineraries\": [ { \"segments\": [ { \"departure\": { \"iataCode\": \"JFK\", \"at\": \"2025-06-01T08:00\" }, \"arrival\": { \"iataCode\": \"LAX\", \"at\": \"2025-06-01T11:00\" }, \"carrierCode\": \"AA\" } ], \"duration\": \"PT6H\" } ], \"price\": { \"total\": \"300.00\" } } ] }";

        when(authService.getAccessToken()).thenReturn(token);
        when(amadeusConfig.getFlightSearchUrl()).thenReturn("https://api.amadeus.com/v2/shopping/flight-offers");
        when(airportService.getAirportByIATACode("JFK")).thenReturn(Optional.of(new AirportResponse("John F Kennedy Intl", "JFK", "New York", "USA")));
        when(airportService.getAirportByIATACode("LAX")).thenReturn(Optional.of(new AirportResponse("Los Angeles Intl", "LAX", "Los Angeles", "USA")));
        when(airlineService.getAirlineName("AA")).thenReturn(Optional.of("American Airlines"));

        ResponseEntity<String> responseEntity = new ResponseEntity<>(mockJson, HttpStatus.OK);
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);

        JsonNode root = new ObjectMapper().readTree(mockJson);
        when(objectMapper.readTree(mockJson)).thenReturn(root);

        List<FlightSearchResponse> results = flightSearchService.searchFlights(request);

        assertFalse(results.isEmpty());
        FlightSearchResponse response = results.get(0);
        assertEquals("John F Kennedy Intl", response.getDepartureAirportName());
        assertEquals("Los Angeles Intl", response.getArrivalAirportName());
        assertEquals("American Airlines", response.getAirlineName());
        assertEquals("300.00", response.getPricePerTraveler());


    }

}
