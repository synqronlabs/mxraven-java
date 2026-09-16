package com.mxraven.mail.model;

/**
 * The SMTP body type requested with the {@code BODY} parameter, as defined by the
 * {@code 8BITMIME} (RFC 6152) and {@code BINARYMIME} (RFC 3030) extensions.
 */
public enum BodyType {
    /** 7-bit body transfer ({@code 7BIT}). */
    SEVEN_BIT("7BIT"),
    /** 8-bit MIME body transfer ({@code 8BITMIME}). */
    EIGHT_BIT_MIME("8BITMIME"),
    /** Binary MIME body transfer ({@code BINARYMIME}). */
    BINARY_MIME("BINARYMIME");

    private final String wire;

    BodyType(String wire) {
        this.wire = wire;
    }

    /**
     * The keyword used in the SMTP {@code BODY} parameter.
     *
     * @return the wire keyword
     */
    public String wire() {
        return wire;
    }
}
