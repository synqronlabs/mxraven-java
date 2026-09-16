package com.mxraven.mail.model;

/**
 * SMTP service extensions advertised in an EHLO response (RFC 5321 and the
 * associated extension specifications).
 */
public enum Extension {
    /** 8-bit MIME transport (RFC 6152). */
    BIT8MIME("8BITMIME"),
    /** Command pipelining (RFC 2920). */
    PIPELINING("PIPELINING"),
    /** Internationalized email addresses (RFC 6531). */
    SMTPUTF8("SMTPUTF8"),
    /** Transport layer security upgrade (RFC 3207). */
    STARTTLS("STARTTLS"),
    /** Message size declaration (RFC 1870). */
    SIZE("SIZE"),
    /** Delivery status notifications (RFC 3461). */
    DSN("DSN"),
    /** Authentication (RFC 4954). */
    AUTH("AUTH"),
    /** Message chunking (RFC 3030). */
    CHUNKING("CHUNKING"),
    /** Binary MIME transport (RFC 3030). */
    BINARYMIME("BINARYMIME"),
    /** Enhanced status codes (RFC 2034). */
    ENHANCEDSTATUSCODES("ENHANCEDSTATUSCODES"),
    /** Requirement to use TLS (RFC 8689). */
    REQUIRETLS("REQUIRETLS"),
    /** Delivery-by parameters (RFC 2852). */
    DELIVERBY("DELIVERBY");

    private final String wire;

    Extension(String wire) {
        this.wire = wire;
    }

    /**
     * The keyword advertised for this extension.
     *
     * @return the wire keyword
     */
    public String wire() {
        return wire;
    }

    /**
     * Resolves an extension from its advertised keyword, ignoring case.
     *
     * @param wire the advertised keyword
     * @return the matching extension
     * @throws IllegalArgumentException if no extension matches {@code wire}
     */
    public static Extension fromWire(String wire) {
        for (Extension extension : values()) {
            if (extension.wire.equalsIgnoreCase(wire)) {
                return extension;
            }
        }
        throw new IllegalArgumentException("unknown SMTP extension: " + wire);
    }
}
