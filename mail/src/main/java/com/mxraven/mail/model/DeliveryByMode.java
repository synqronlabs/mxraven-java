package com.mxraven.mail.model;

public enum DeliveryByMode {
    NOTIFY("N"),
    RETURN("R");

    private final String wire;

    DeliveryByMode(String wire) {
        this.wire = wire;
    }

    public String wire() {
        return wire;
    }
}
