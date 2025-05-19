package com.flightsearch.backend.controller;

import com.flightsearch.backend.model.FlightDetailsResponse;
import com.flightsearch.backend.service.FlightSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/flights")
public class FlightDetailsController {

    private final FlightSearchService flightSearchService;

    public FlightDetailsController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    @GetMapping("/details/{uuid}")
    public ResponseEntity<FlightDetailsResponse> getFlightDetails(@PathVariable String uuid) {
        try {
            UUID parsedUUID = UUID.fromString(uuid);
            Optional<FlightDetailsResponse> result = flightSearchService.getFlightDetailsByUUID(parsedUUID);

            return result.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
