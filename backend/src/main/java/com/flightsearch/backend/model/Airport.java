package com.flightsearch.backend.model;

public class Airport {
    private String name;
    private String aitacode;
    private String city;
    private String country;

    public Airport(String name, String aitacode, String city, String country) {
        this.name = name;
        this.aitacode = aitacode;
        this.city = city;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getAitacode() {
        return aitacode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
