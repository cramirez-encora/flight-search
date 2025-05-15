// com.flightsearch.backend.controller.FlightSearchController.java
package com.flightsearch.backend.controller;

import com.flightsearch.backend.model.FlightSearchRequest;
import com.flightsearch.backend.service.FlightSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
public class FlightSearchController {

    @Autowired
    private FlightSearchService flightSearchService;

    @GetMapping("/search")
    public String searchFlights(@ModelAttribute FlightSearchRequest request) {
        return flightSearchService.searchFlights(request);
    }
}
