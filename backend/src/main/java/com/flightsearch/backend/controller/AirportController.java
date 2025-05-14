package com.flightsearch.backend.controller;

import com.flightsearch.backend.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    @Autowired
    private AirportService airportService;

    @GetMapping("/search")
    public String searchAirports(@RequestParam String keyword) {
        return airportService.searchAirports(keyword);
    }
}
