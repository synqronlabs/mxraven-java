package com.mxraven.mail.model;

public enum Extension {
    BIT8MIME("8BITMIME"),
    PIPELINING("PIPELINING"),
    SMTPUTF8("SMTPUTF8"),
    STARTTLS("STARTTLS"),
    SIZE("SIZE"),
    DSN("DSN"),
    AUTH("AUTH"),
    CHUNKING("CHUNKING"),
    BINARYMIME("BINARYMIME"),
    ENHANCEDSTATUSCODES("ENHANCEDSTATUSCODES"),
    REQUIRETLS("REQUIRETLS"),
    DELIVERBY("DELIVERBY");

    private final String wire;

    Extension(String wire) {
        this.wire = wire;
    }

    public String wire() {
        return wire;
    }

    public static Extension fromWire(String wire) {
        for (Extension extension : values()) {
            if (extension.wire.equalsIgnoreCase(wire)) {
                return extension;
            }
        }
        throw new IllegalArgumentException("unknown SMTP extension: " + wire);
    }
}
