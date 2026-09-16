package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** DNS record type. */
public enum DnsRecordType {
    /** Canonical-name alias record. */
    CNAME("CNAME"),
    /** Free-form text record. */
    TXT("TXT"),
    /** Mail-exchange record. */
    MX("MX");

    private final String wire;

    DnsRecordType(String wire) {
        this.wire = wire;
    }

    /**
     * The wire value.
     *
     * @return the wire value
     */
    @JsonValue
    public String wire() {
        return wire;
    }

    /**
     * Resolves the record type from its wire value.
     *
     * @param value wire value
     * @return matching record type, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException when the value is unknown
     */
    @JsonCreator
    public static DnsRecordType fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (DnsRecordType candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown DnsRecordType value: " + value);
    }
}
