package com.flightsearch.backend.model;

import java.util.List;

public class FlightDetailsResponse {
    private List<FlightSegmentDetail> segments;
    private List<LayoverDetail> layovers;
    private PriceBreakdown priceBreakdown;

    public List<FlightSegmentDetail> getSegments() {
        return segments;
    }
    public void setSegments(List<FlightSegmentDetail> segments) {
        this.segments = segments;
    }

    public List<LayoverDetail> getLayovers() {
        return layovers;
    }
    public void setLayovers(List<LayoverDetail> layovers) {
        this.layovers = layovers;
    }

    public PriceBreakdown getPriceBreakdown() {
        return priceBreakdown;
    }
    public void setPriceBreakdown(PriceBreakdown priceBreakdown) {
        this.priceBreakdown = priceBreakdown;
    }
}
