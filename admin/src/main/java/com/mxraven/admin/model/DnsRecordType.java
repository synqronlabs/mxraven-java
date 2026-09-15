package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** DNS record type. */
public enum DnsRecordType {
    CNAME("CNAME"),
    TXT("TXT"),
    MX("MX");

    private final String wire;

    DnsRecordType(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
