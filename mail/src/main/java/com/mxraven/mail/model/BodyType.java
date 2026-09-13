package com.mxraven.mail.model;

public enum BodyType {
    SEVEN_BIT("7BIT"),
    EIGHT_BIT_MIME("8BITMIME"),
    BINARY_MIME("BINARYMIME");

    private final String wire;

    BodyType(String wire) {
        this.wire = wire;
    }

    public String wire() {
        return wire;
    }
}
