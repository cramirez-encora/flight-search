package com.flightsearch.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.flightsearch.backend.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FlightDetailService {
    private final AirportService airportService;
    private final AirlineService airlineService;

    public FlightDetailService(AirportService airportService, AirlineService airlineService) {
        this.airportService = airportService;
        this.airlineService = airlineService;
    }

    public FlightDetailsResponse mapToFlightDetailsResponse(JsonNode offer) {
        FlightDetailsResponse details = new FlightDetailsResponse();

        List<FlightSegmentDetail> segmentDetails = new ArrayList<>();
        List<LayoverDetail> layoverDetails = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        JsonNode itineraries = offer.get("itineraries");
        for (JsonNode itinerary : itineraries) {
            JsonNode segments = itinerary.get("segments");

            for (int i = 0; i < segments.size(); i++) {
                JsonNode segment = segments.get(i);
                FlightSegmentDetail segmentDetail = new FlightSegmentDetail();

                // Times
                String departureTimeStr = segment.get("departure").get("at").asText();
                String arrivalTimeStr = segment.get("arrival").get("at").asText();
                segmentDetail.setDepartureTime(departureTimeStr);
                segmentDetail.setArrivalTime(arrivalTimeStr);

                // === Departure Airport ===
                String departureAirportCode = segment.get("departure").get("iataCode").asText();
                segmentDetail.setDepartureAirportCode(departureAirportCode);
                segmentDetail.setDepartureAirportName(
                        airportService.getAirportByIATACode(departureAirportCode)
                                .map(AirportResponse::getName)
                                .orElse(null)
                );

                // === Arrival Airport ===
                String arrivalAirportCode = segment.get("arrival").get("iataCode").asText();
                segmentDetail.setArrivalAirportCode(arrivalAirportCode);
                segmentDetail.setArrivalAirportName(
                        airportService.getAirportByIATACode(arrivalAirportCode)
                                .map(AirportResponse::getName)
                                .orElse(null)
                );

                // Airline & Flight Info
                String airlineCode = segment.get("carrierCode").asText();
                segmentDetail.setAirlineCode(airlineCode);
                segmentDetail.setAirlineName(airlineService.getAirlineName(airlineCode).orElse(airlineCode));
                segmentDetail.setFlightNumber(segment.get("number").asText());

                if (segment.has("operating")) {
                    String operatingCode = segment.get("operating").get("carrierCode").asText();
                    if (!operatingCode.equals(airlineCode)) {
                        segmentDetail.setOperatingAirlineCode(operatingCode);
                        segmentDetail.setOperatingAirlineName(
                                airlineService.getAirlineName(operatingCode).orElse(operatingCode)
                        );
                    }
                }

                segmentDetail.setAircraftType(segment.get("aircraft").get("code").asText());

                JsonNode fareDetails = offer.get("travelerPricings").get(0).get("fareDetailsBySegment").get(i);
                segmentDetail.setCabin(fareDetails.get("cabin").asText());
                segmentDetail.setTravelClass(fareDetails.get("class").asText());

                JsonNode amenities = fareDetails.get("amenities");
                if (amenities != null && amenities.isArray()) {
                    List<Amenity> amenityList = new ArrayList<>();
                    for (JsonNode amenityNode : amenities) {
                        String description = amenityNode.get("description").asText();
                        boolean isChargeable = amenityNode.get("isChargeable").asBoolean();
                        amenityList.add(new Amenity(description, isChargeable));
                    }
                    segmentDetail.setAmenities(amenityList);
                }

                segmentDetails.add(segmentDetail);

                // === Layover Calculation ===
                if (i > 0) {
                    JsonNode prevSegment = segments.get(i - 1);
                    String prevArrivalStr = prevSegment.get("arrival").get("at").asText();
                    String currDepartureStr = segment.get("departure").get("at").asText();

                    try {
                        LocalDateTime prevArrivalTime = LocalDateTime.parse(prevArrivalStr, formatter);
                        LocalDateTime currDepartureTime = LocalDateTime.parse(currDepartureStr, formatter);
                        Duration layoverDuration = Duration.between(prevArrivalTime, currDepartureTime);

                        LayoverDetail layoverDetail = new LayoverDetail();
                        layoverDetail.setAirportCode(segment.get("departure").get("iataCode").asText());
                        layoverDetail.setDuration(formatDuration(layoverDuration));

                        layoverDetails.add(layoverDetail);
                    } catch (Exception e) {
                        // If there's a parsing issue, skip the layover
                        System.err.println("Error parsing layover times: " + e.getMessage());
                    }
                }
            }
        }

        details.setSegments(segmentDetails);
        details.setLayovers(layoverDetails);

        // === Price Breakdown ===
        PriceBreakdown priceBreakdown = new PriceBreakdown();
        JsonNode price = offer.get("price");

        priceBreakdown.setBasePrice(price.get("base").asText());
        priceBreakdown.setTotalPrice(price.get("total").asText());

        List<Fee> fees = new ArrayList<>();
        JsonNode feeArray = price.get("fees");
        if (feeArray != null && feeArray.isArray()) {
            for (JsonNode feeNode : feeArray) {
                Fee fee = new Fee();
                fee.setAmount(feeNode.get("amount").asText());
                fee.setType(feeNode.get("type").asText());
                fees.add(fee);
            }
        }
        priceBreakdown.setFees(fees);

        int travelerCount = 1;
        JsonNode travelerPricings = offer.get("travelerPricings");
        if (travelerPricings != null && travelerPricings.isArray()) {
            travelerCount = travelerPricings.size();
        }

        try {
            BigDecimal total = new BigDecimal(price.get("total").asText());
            BigDecimal perTraveler = total.divide(BigDecimal.valueOf(travelerCount), 2, RoundingMode.HALF_UP);
            priceBreakdown.setPricePerTraveler(perTraveler.toPlainString());
        } catch (Exception e) {
            priceBreakdown.setPricePerTraveler(null);
        }

        details.setPriceBreakdown(priceBreakdown);
        return details;
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return (hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m" : "").trim();
    }
}
