package com.flightsearch.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@ConfigurationProperties(prefix = "amadeus")
public class AmadeusConfig {
    private String clientId;
    private String clientSecret;

    // getters and setters
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Value("${amadeus.api.airport-search-url}")
    private String airportSearchUrl;

    public String getAirportSearchUrl() {
        return airportSearchUrl;
    }
}