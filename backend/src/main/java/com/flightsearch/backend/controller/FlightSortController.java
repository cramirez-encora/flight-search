package com.flightsearch.backend.controller;

import com.flightsearch.backend.model.FlightSearchResponse;
import com.flightsearch.backend.service.FlightSortService;
import com.flightsearch.backend.model.FlightSortRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightSortController {

    private final FlightSortService sortService;

    public FlightSortController(FlightSortService sortService) {
        this.sortService = sortService;
    }

    @PostMapping("/sort")
    public ResponseEntity<List<FlightSearchResponse>> sortFlights(@RequestBody FlightSortRequest request) {
        List<FlightSearchResponse> results = request.getResults();
        sortService.sort(results, request.getSortBy(), request.getSortOrder());
        return ResponseEntity.ok(results);
    }
}
