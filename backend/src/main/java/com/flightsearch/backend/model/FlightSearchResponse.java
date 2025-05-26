package com.flightsearch.backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightSearchResponse {
    private String departureTime;
    private String arrivalTime;
    private String departureAirportName;
    private String departureAirportCode;
    private String arrivalAirportName;
    private String arrivalAirportCode;
    private String airlineName;
    private String airlineCode;
    private String operatingAirlineName;
    private String operatingAirlineCode;
    private String duration;
    private int stops;
    private String totalPrice;
    private String pricePerTraveler;
    private String uuid;

    // Return flight fields (for round-trip support)
    private String returnDepartureTime;
    private String returnArrivalTime;
    private String returnDuration;
    private Integer returnStops;

    public FlightSearchResponse() {}

    public FlightSearchResponse(String departureTime, String arrivalTime,
                                String departureAirportName, String departureAirportCode,
                                String arrivalAirportName, String arrivalAirportCode,
                                String airlineName, String airlineCode,
                                String operatingAirlineName, String operatingAirlineCode,
                                String duration, int stops,
                                String totalPrice, String pricePerTraveler, String uuid,
                                String returnDepartureTime, String returnArrivalTime,
                                String returnDuration, Integer returnStops) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureAirportName = departureAirportName;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportName = arrivalAirportName;
        this.arrivalAirportCode = arrivalAirportCode;
        this.airlineName = airlineName;
        this.airlineCode = airlineCode;
        this.operatingAirlineName = operatingAirlineName;
        this.operatingAirlineCode = operatingAirlineCode;
        this.duration = duration;
        this.stops = stops;
        this.totalPrice = totalPrice;
        this.pricePerTraveler = pricePerTraveler;
        this.uuid = uuid;
        this.returnDepartureTime = returnDepartureTime;
        this.returnArrivalTime = returnArrivalTime;
        this.returnDuration = returnDuration;
        this.returnStops = returnStops;
    }

    // Getters and Setters for all fields (including return flight fields)

    public String getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }
    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }
    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }
    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }
    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public String getAirlineName() {
        return airlineName;
    }
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getAirlineCode() {
        return airlineCode;
    }
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getOperatingAirlineName() {
        return operatingAirlineName;
    }
    public void setOperatingAirlineName(String operatingAirlineName) {
        this.operatingAirlineName = operatingAirlineName;
    }

    public String getOperatingAirlineCode() {
        return operatingAirlineCode;
    }
    public void setOperatingAirlineCode(String operatingAirlineCode) {
        this.operatingAirlineCode = operatingAirlineCode;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getStops() {
        return stops;
    }
    public void setStops(int stops) {
        this.stops = stops;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPricePerTraveler() {
        return pricePerTraveler;
    }
    public void setPricePerTraveler(String pricePerTraveler) {
        this.pricePerTraveler = pricePerTraveler;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getReturnDepartureTime() {
        return returnDepartureTime;
    }
    public void setReturnDepartureTime(String returnDepartureTime) {
        this.returnDepartureTime = returnDepartureTime;
    }

    public String getReturnArrivalTime() {
        return returnArrivalTime;
    }
    public void setReturnArrivalTime(String returnArrivalTime) {
        this.returnArrivalTime = returnArrivalTime;
    }

    public String getReturnDuration() {
        return returnDuration;
    }
    public void setReturnDuration(String returnDuration) {
        this.returnDuration = returnDuration;
    }

    public Integer getReturnStops() {
        return returnStops;
    }
    public void setReturnStops(Integer returnStops) {
        this.returnStops = returnStops;
    }
}
