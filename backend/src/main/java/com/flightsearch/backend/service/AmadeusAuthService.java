package com.flightsearch.backend.service;

import com.flightsearch.backend.model.TokenResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Instant;
import java.util.Map;

@Service
public class AmadeusAuthService {

    @Value("${amadeus.client.id}")
    private String clientId;

    @Value("${amadeus.client.secret}")
    private String clientSecret;

    @Value("${amadeus.auth.url}")
    private String authUrl;


    private final RestTemplate restTemplate = new RestTemplate();

    private String accessToken;
    private Instant tokenExpiration;

    @PostConstruct
    public void init() {
        getAccessToken();
    }

    public synchronized String getAccessToken() {
        if (accessToken == null || tokenExpired()) {
            requestNewToken();
        }
        return accessToken;
    }

    private boolean tokenExpired() {
        return tokenExpiration == null || Instant.now().isAfter(tokenExpiration);
    }

    private void requestNewToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=client_credentials"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                authUrl,
                HttpMethod.POST,
                entity,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            this.accessToken = (String) responseBody.get("access_token");
            Integer expiresIn = (Integer) responseBody.get("expires_in");
            this.tokenExpiration = Instant.now().plusSeconds(expiresIn - 60);
        } else {
            throw new RuntimeException("Failed to obtain access token from Amadeus");
        }
    }
}
