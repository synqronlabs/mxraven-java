package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** SAML signature algorithm. */
public enum SamlSignatureAlgorithm {
    UNSPECIFIED("unspecified"),
    RSA_SHA1("rsa_sha1"),
    RSA_SHA256("rsa_sha256"),
    RSA_SHA512("rsa_sha512");

    private final String wire;

    SamlSignatureAlgorithm(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static SamlSignatureAlgorithm fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (SamlSignatureAlgorithm candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown SamlSignatureAlgorithm value: " + value);
    }
}
