package com.flightsearch.backend.model;

import jakarta.validation.constraints.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class FlightSearchRequest {
    @NotBlank
    private String originLocationCode;

    @NotBlank
    private String destinationLocationCode;

    @NotNull
    @FutureOrPresent(message = "Departure date must be today or in the future")
    private LocalDate departureDate;

    @Min(value = 1, message = "There must be at least one adult")
    private int adults;

    @Pattern(regexp = "USD|MXN|EUR", message = "Currency must be USD, MXN or EUR")
    private String currencyCode;

    private boolean nonStop;

    public FlightSearchRequest() {}

    public FlightSearchRequest(String originLocationCode, String destinationLocationCode, LocalDate departureDate,
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


    public LocalDate getDepartureDate() {
        return departureDate;
    }
    public void setDepartureDate(LocalDate departureDate) {
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
