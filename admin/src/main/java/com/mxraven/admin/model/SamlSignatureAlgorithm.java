package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** SAML signature algorithm. */
public enum SamlSignatureAlgorithm {
    /** Unspecified signature algorithm. */
    UNSPECIFIED("unspecified"),
    /** RSA with SHA-1. */
    RSA_SHA1("rsa_sha1"),
    /** RSA with SHA-256. */
    RSA_SHA256("rsa_sha256"),
    /** RSA with SHA-512. */
    RSA_SHA512("rsa_sha512");

    private final String wire;

    SamlSignatureAlgorithm(String wire) {
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
     * Resolves the constant for a wire value.
     *
     * @param value wire value, or {@code null}
     * @return the matching constant, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException if the value is unknown
     */
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
