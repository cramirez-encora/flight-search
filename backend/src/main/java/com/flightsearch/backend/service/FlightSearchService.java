package com.flightsearch.backend.service;

import com.flightsearch.backend.config.AmadeusConfig;
import com.flightsearch.backend.model.AirportResponse;
import com.flightsearch.backend.model.FlightDetailsResponse;
import com.flightsearch.backend.model.FlightSearchRequest;
import com.flightsearch.backend.model.FlightSearchResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class FlightSearchService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AmadeusConfig amadeusConfig;
    private final AmadeusAuthService authService;
    private final AirportService airportService;
    private final AirlineService airlineService;

    private final Map<UUID, JsonNode> flightDetailsCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, AirportResponse> airportCache = new ConcurrentHashMap<>();

    public FlightSearchService(RestTemplate restTemplate, ObjectMapper objectMapper,
                               AmadeusConfig amadeusConfig, AmadeusAuthService authService,
                               AirportService airportService, AirlineService airlineService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.amadeusConfig = amadeusConfig;
        this.authService = authService;
        this.airportService = airportService;
        this.airlineService = airlineService;
    }

    @Cacheable(value = "flightSearchCache", key = "#request.toString()")
    public List<FlightSearchResponse> searchFlights(FlightSearchRequest request) {
        String token = authService.getAccessToken();
        String url = buildFlightSearchUrl(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            return parseFlightResults(root, request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse flight search response: " + e.getMessage(), e);
        }
    }

    private String buildFlightSearchUrl(FlightSearchRequest request) {
        StringBuilder sb = new StringBuilder(amadeusConfig.getFlightSearchUrl());
        sb.append("?originLocationCode=").append(request.getOriginLocationCode());
        sb.append("&destinationLocationCode=").append(request.getDestinationLocationCode());
        sb.append("&departureDate=").append(request.getDepartureDate());
        if (request.getReturnDate() != null) {
            sb.append("&returnDate=").append(request.getReturnDate());
        }
        sb.append("&adults=").append(request.getAdults());
        sb.append("&nonStop=").append(request.isNonStop());
        sb.append("&currencyCode=").append(request.getCurrencyCode());
        sb.append("&max=10");
        return sb.toString();
    }

    private List<FlightSearchResponse> parseFlightResults(JsonNode root, FlightSearchRequest request) {
        List<FlightSearchResponse> results = new ArrayList<>();
        JsonNode data = root.get("data");
        if (data == null || !data.isArray()) return results;

        for (JsonNode offer : data) {
            try {
                JsonNode itineraries = offer.get("itineraries");
                if (request.getReturnDate() == null && itineraries.size() > 1) {
                    continue;
                }

                JsonNode outboundItinerary = itineraries.get(0);
                JsonNode outboundSegments = outboundItinerary.get("segments");
                JsonNode outboundFirstSegment = outboundSegments.get(0);
                JsonNode outboundLastSegment = outboundSegments.get(outboundSegments.size() - 1);

                String departureTime = outboundFirstSegment.get("departure").get("at").asText();
                String arrivalTime = outboundLastSegment.get("arrival").get("at").asText();

                String departureAirportCode = outboundFirstSegment.get("departure").get("iataCode").asText();
                String arrivalAirportCode = outboundLastSegment.get("arrival").get("iataCode").asText();

                String departureName = getAirportInfo(departureAirportCode).getName();
                String arrivalName = getAirportInfo(arrivalAirportCode).getName();

                String airlineCode = outboundFirstSegment.get("carrierCode").asText();
                String airlineName = airlineService.getAirlineName(airlineCode).orElse(airlineCode);

                String operatingCode = outboundFirstSegment.has("operating") ?
                        outboundFirstSegment.get("operating").get("carrierCode").asText() : airlineCode;
                String operatingName = airlineService.getAirlineName(operatingCode).orElse(operatingCode);

                String formattedDuration = formatDuration(outboundItinerary.get("duration").asText());
                int outboundStops = outboundSegments.size() - 1;

                String returnDepartureTime = null;
                String returnArrivalTime = null;
                String returnDuration = null;
                int returnStops = 0;

                if (request.getReturnDate() != null
                        && itineraries.size() > 1) {
                    JsonNode returnItinerary = itineraries.get(1);
                    JsonNode returnSegments = returnItinerary.get("segments");
                    JsonNode returnFirstSegment = returnSegments.get(0);
                    JsonNode returnLastSegment = returnSegments.get(returnSegments.size() - 1);

                    returnDepartureTime = returnFirstSegment.get("departure").get("at").asText();
                    returnArrivalTime = returnLastSegment.get("arrival").get("at").asText();
                    returnDuration = formatDuration(returnItinerary.get("duration").asText());
                    returnStops = returnSegments.size() - 1;
                }

                String pricePerTraveler = offer.get("price").get("total").asText();
                double total = Double.parseDouble(pricePerTraveler) * request.getAdults();
                String totalPrice = String.format("%.2f", total);

                UUID uuid = UUID.randomUUID();
                flightDetailsCache.put(uuid, offer);

                FlightSearchResponse flight = new FlightSearchResponse(
                        departureTime, arrivalTime,
                        departureName, departureAirportCode,
                        arrivalName, arrivalAirportCode,
                        airlineName, airlineCode,
                        operatingCode.equals(airlineCode) ? null : operatingName,
                        operatingCode.equals(airlineCode) ? null : operatingCode,
                        formattedDuration, outboundStops,
                        totalPrice, pricePerTraveler,
                        uuid.toString(),
                        returnDepartureTime, returnArrivalTime,
                        returnDuration, returnStops
                );

                results.add(flight);
            } catch (Exception e) {
                // Skip malformed entries
            }
        }
        return results;
    }

    private AirportResponse getAirportInfo(String iataCode) {
        return airportCache.computeIfAbsent(iataCode, code ->
                airportService.getAirportByIATACode(code)
                        .orElse(new AirportResponse(null, code, null, null))
        );
    }


    private String formatDuration(String isoDuration) {
        Duration duration = Duration.parse(isoDuration);
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return (hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m" : "").trim();
    }

    private long parseDurationToMinutes(String durationStr) {
        long minutes = 0;
        String[] parts = durationStr.split(" ");
        for (String part : parts) {
            if (part.endsWith("h")) {
                minutes += Integer.parseInt(part.replace("h", "")) * 60;
            } else if (part.endsWith("m")) {
                minutes += Integer.parseInt(part.replace("m", ""));
            }
        }
        return minutes;
    }

    public Optional<FlightDetailsResponse> getFlightDetailsByUUID(UUID uuid) {
        JsonNode offer = flightDetailsCache.get(uuid);
        if (offer == null) {
            return Optional.empty();
        }
        FlightDetailService mapper = new FlightDetailService(airportService, airlineService);
        return Optional.of(mapper.mapToFlightDetailsResponse(offer));
    }
}
