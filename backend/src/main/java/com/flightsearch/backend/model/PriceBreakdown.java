package com.flightsearch.backend.model;

import java.util.List;

public class PriceBreakdown {
    private String basePrice;
    private String totalPrice;
    private List<Fee> fees;
    private String pricePerTraveler;

    public String getBasePrice() {
        return basePrice;
    }
    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Fee> getFees() {
        return fees;
    }
    public void setFees(List<Fee> fees) {
        this.fees = fees;
    }

    public String getPricePerTraveler() {
        return pricePerTraveler;
    }
    public void setPricePerTraveler(String pricePerTraveler) {
        this.pricePerTraveler = pricePerTraveler;
    }
}
