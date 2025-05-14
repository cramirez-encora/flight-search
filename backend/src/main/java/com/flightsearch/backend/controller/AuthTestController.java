package com.flightsearch.backend.controller;

import com.flightsearch.backend.service.AmadeusAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthTestController {

    private final AmadeusAuthService authService;

    public AuthTestController(AmadeusAuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/api/token")
    public String getToken() {
        return authService.getAccessToken();
    }
}
