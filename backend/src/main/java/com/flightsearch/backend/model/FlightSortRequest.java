package com.flightsearch.backend.model;

import java.util.List;

public class FlightSortRequest {
    private String sortBy;
    private String sortOrder;
    private List<FlightSearchResponse> results;


    public String getSortBy() {
        return sortBy;
    }
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
    public List<FlightSearchResponse> getResults() {
        return results;
    }
    public void setResults(List<FlightSearchResponse> results) {
        this.results = results;
    }
}
