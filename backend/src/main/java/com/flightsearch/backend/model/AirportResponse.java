package com.flightsearch.backend.model;

public class AirportResponse {
    private String name;
    private String iataCode;
    private String city;
    private String country;

    public AirportResponse(String name, String iataCode, String city, String country) {
        this.name = name;
        this.iataCode = iataCode;
        this.city = city;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getIataCode() {
        return iataCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
