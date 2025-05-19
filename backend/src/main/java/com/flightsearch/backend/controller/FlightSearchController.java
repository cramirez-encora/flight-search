package com.flightsearch.backend.controller;

import com.flightsearch.backend.model.FlightSearchRequest;
import com.flightsearch.backend.model.FlightSearchResponse;
import com.flightsearch.backend.service.FlightSearchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightSearchController {

    private final FlightSearchService flightSearchService;

    public FlightSearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    @PostMapping("/search")
    public ResponseEntity<List<FlightSearchResponse>> searchFlights(@Valid @RequestBody FlightSearchRequest request) {
        if (request.getReturnDate() != null && request.getReturnDate().isBefore(request.getDepartureDate())) {
            return ResponseEntity.badRequest().build();
        }
        List<FlightSearchResponse> results = flightSearchService.searchFlights(request);
        return ResponseEntity.ok(results);
    }
}
