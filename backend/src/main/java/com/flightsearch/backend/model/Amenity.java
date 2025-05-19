package com.flightsearch.backend.model;

public class Amenity {
    private String name;
    private boolean chargeable;

    public Amenity(String name, boolean chargeable) {
        this.name = name;
        this.chargeable = chargeable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChargeable() {
        return chargeable;
    }

    public void setChargeable(boolean chargeable) {
        this.chargeable = chargeable;
    }
}
