package com.flightsearch.backend.model;

import java.util.List;

public class RoundTripFlightSearchResponse {
    private List<FlightSearchResponse> outbound;
    private List<FlightSearchResponse> returnFlight;

    public RoundTripFlightSearchResponse() {}

    public RoundTripFlightSearchResponse(List<FlightSearchResponse> outbound, List<FlightSearchResponse> returnFlight) {
        this.outbound = outbound;
        this.returnFlight = returnFlight;
    }

    public List<FlightSearchResponse> getOutbound() {
        return outbound;
    }

    public void setOutbound(List<FlightSearchResponse> outbound) {
        this.outbound = outbound;
    }

    public List<FlightSearchResponse> getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(List<FlightSearchResponse> returnFlight) {
        this.returnFlight = returnFlight;
    }
}
