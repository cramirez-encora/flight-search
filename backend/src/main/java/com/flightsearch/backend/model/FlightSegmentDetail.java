package com.flightsearch.backend.model;

import java.util.List;

public class FlightSegmentDetail {
    private String departureTime;
    private String arrivalTime;

    private String departureAirportCode;
    private String departureAirportName;
    private String arrivalAirportCode;
    private String arrivalAirportName;

    private String airlineCode;
    private String airlineName;
    private String flightNumber;
    private String operatingAirlineCode;
    private String operatingAirlineName;
    private String aircraftType;
    private String cabin;
    private String travelClass;
    private List<Amenity> amenities;

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

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }
    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }
    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }
    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }
    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public String getAirlineCode() {
        return airlineCode;
    }
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getAirlineName() {
        return airlineName;
    }
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getFlightNumber() {
        return flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getOperatingAirlineCode() {
        return operatingAirlineCode;
    }
    public void setOperatingAirlineCode(String operatingAirlineCode) {
        this.operatingAirlineCode = operatingAirlineCode;
    }

    public String getOperatingAirlineName() {
        return operatingAirlineName;
    }
    public void setOperatingAirlineName(String operatingAirlineName) {
        this.operatingAirlineName = operatingAirlineName;
    }

    public String getAircraftType() {
        return aircraftType;
    }
    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getCabin() {
        return cabin;
    }
    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getTravelClass() {
        return travelClass;
    }
    public void setTravelClass(String travelClass) {
        this.travelClass = travelClass;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }
    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }
}
