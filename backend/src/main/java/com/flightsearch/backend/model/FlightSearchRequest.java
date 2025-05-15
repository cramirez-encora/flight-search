package com.flightsearch.backend.model;

public class FlightSearchRequest {
    private String originLocationCode;
    private String destinationLocationCode;
    private String departureDate;
    private int adults;
    private String currencyCode;
    private boolean nonStop;

    public FlightSearchRequest() {}

    public FlightSearchRequest(String originLocationCode, String destinationLocationCode, String departureDate,
                                  int adults, String currencyCode, boolean nonStop) {
        this.originLocationCode = originLocationCode;
        this.destinationLocationCode = destinationLocationCode;
        this.departureDate = departureDate;
        this.adults = adults;
        this.currencyCode = currencyCode;
        this.nonStop = nonStop;
    }

    public String getOriginLocationCode() {
        return originLocationCode;
    }
    public void setOriginLocationCode(String originLocationCode) {
        this.originLocationCode = originLocationCode;
    }


    public String getDestinationLocationCode() {
        return destinationLocationCode;
    }
    public void setDestinationLocationCode(String destinationLocationCode) {
        this.destinationLocationCode = destinationLocationCode;
    }


    public String getDepartureDate() {
        return departureDate;
    }
    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }


    public int getAdults() {
        return adults;
    }
    public void setAdults(int adults) {
        this.adults = adults;
    }


    public String getCurrencyCode() {
        return currencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }


    public boolean isNonStop() {
        return nonStop;
    }
    public void setNonStop(boolean nonStop) {
        this.nonStop = nonStop;
    }
}
