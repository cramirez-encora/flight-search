package com.flightsearch.backend.controller;

import com.flightsearch.backend.service.AirportService;
import com.flightsearch.backend.model.AirportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private final AirportService airportService;

    @Autowired
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<AirportResponse>> searchAirports(@RequestParam String keyword) {
        List<AirportResponse> response = airportService.searchAirports(keyword);
        return ResponseEntity.ok(response);
    }
}
