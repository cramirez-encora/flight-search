package com.flightsearch.backend.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

public class FlightSearchRequest {
    @NotBlank
    private String originLocationCode;

    @NotBlank
    private String destinationLocationCode;

    @NotNull
    @FutureOrPresent(message = "Departure date must be today or in the future")
    private LocalDate departureDate;

    @Future(message = "Return date must be in the future")
    private LocalDate returnDate;

    @Min(value = 1, message = "There must be at least one adult")
    private int adults;

    @Pattern(regexp = "USD|MXN|EUR", message = "Currency must be USD, MXN or EUR")
    private String currencyCode;

    private boolean nonStop;

    private String sortBy;

    private String sortOrder;

    // Constructors, Getters, Setters
    public FlightSearchRequest() {}

    public FlightSearchRequest(String originLocationCode, String destinationLocationCode, LocalDate departureDate,
                               LocalDate returnDate, int adults, String currencyCode, boolean nonStop) {
        this.originLocationCode = originLocationCode;
        this.destinationLocationCode = destinationLocationCode;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.adults = adults;
        this.currencyCode = currencyCode;
        this.nonStop = nonStop;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return originLocationCode + "-" +
                destinationLocationCode + "-" +
                departureDate + "-" +
                (returnDate != null ? returnDate : "null") + "-" +
                adults + "-" +
                nonStop + "-" +
                currencyCode + "-" +
                sortBy + "-" +
                sortOrder;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightSearchRequest that = (FlightSearchRequest) o;
        return adults == that.adults &&
                nonStop == that.nonStop &&
                Objects.equals(originLocationCode, that.originLocationCode) &&
                Objects.equals(destinationLocationCode, that.destinationLocationCode) &&
                Objects.equals(departureDate, that.departureDate) &&
                Objects.equals(returnDate, that.returnDate) &&
                Objects.equals(currencyCode, that.currencyCode) &&
                Objects.equals(sortBy, that.sortBy) &&
                Objects.equals(sortOrder, that.sortOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originLocationCode, destinationLocationCode, departureDate, returnDate,
                adults, nonStop, currencyCode, sortBy, sortOrder);
    }


    // Getters and setters

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

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
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

    public String getSortBy() {
        return sortBy;
    }
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }
}
