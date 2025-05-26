package com.flightsearch.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightsearch.backend.model.AirportResponse;
import com.flightsearch.backend.model.FlightDetailsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlightDetailServiceTest {

    private AirportService airportService;
    private AirlineService airlineService;
    private FlightDetailService flightDetailService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        airportService = mock(AirportService.class);
        airlineService = mock(AirlineService.class);
        flightDetailService = new FlightDetailService(airportService, airlineService);
        objectMapper = new ObjectMapper();
    }

    @Test
    void FlightDetailsResponse_MapBasicDataCorrectly() throws Exception {
        String offerJson = """
        {
          "itineraries": [
            {
              "segments": [
                {
                  "departure": {
                    "iataCode": "MEX",
                    "at": "2025-05-26T07:20:00"
                  },
                  "arrival": {
                    "iataCode": "JFK",
                    "at": "2025-05-26T14:05:00"
                  },
                  "carrierCode": "VB",
                  "number": "100",
                  "aircraft": {
                    "code": "320"
                  },
                  "operating": {
                    "carrierCode": null
                  },
                  "duration": "PT5H45M",
                  "id": "1",
                  "numberOfStops": 0
                }
              ]
            }
          ],
          "price": {
            "base": "188.00",
            "total": "430.24",
            "fees": [
              {
                "amount": "0.00",
                "type": "SUPPLIER"
              },
              {
                "amount": "0.00",
                "type": "TICKETING"
              }
            ]
          },
          "travelerPricings": [
            {
              "travelerId": "1",
              "price": {
                "total": "215.12"
              },
              "fareDetailsBySegment": [
                {
                  "cabin": "ECONOMY",
                  "class": "Z",
                  "segmentId": "1"
                }
              ]
            }
          ]
        }
        """;

        JsonNode offerNode = objectMapper.readTree(offerJson);

        when(airportService.getAirportByIATACode("MEX"))
                .thenReturn(Optional.of(new AirportResponse("Benito Juarez Intl", "MEX", "Mexico City", "Mexico")));

        when(airportService.getAirportByIATACode("JFK"))
                .thenReturn(Optional.of(new AirportResponse("John F Kennedy Intl", "JFK", "New York", "USA")));

        when(airlineService.getAirlineName("VB"))
                .thenReturn(Optional.of("Viva Aerobus"));


        FlightDetailsResponse result = flightDetailService.mapToFlightDetailsResponse(offerNode);

        assertNotNull(result);
        assertEquals(1, result.getSegments().size());

        var segment = result.getSegments().get(0);
        assertEquals("MEX", segment.getDepartureAirportCode());
        assertEquals("JFK", segment.getArrivalAirportCode());
        assertEquals("Benito Juarez Intl", segment.getDepartureAirportName());
        assertEquals("John F Kennedy Intl", segment.getArrivalAirportName());
        assertEquals("Viva Aerobus", segment.getAirlineName());
        assertEquals("320", segment.getAircraftType());
        assertEquals("ECONOMY", segment.getCabin());
        assertEquals("Z", segment.getTravelClass());

        assertTrue(result.getLayovers().isEmpty());

        assertEquals("188.00", result.getPriceBreakdown().getBasePrice());
        assertEquals("430.24", result.getPriceBreakdown().getTotalPrice());
        assertEquals(2, result.getPriceBreakdown().getFees().size());
    }
}
