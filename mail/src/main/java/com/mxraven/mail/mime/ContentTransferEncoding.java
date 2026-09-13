package com.mxraven.mail.mime;

/**
 * MIME Content-Transfer-Encoding values (RFC 2045 §6).
 */
public enum ContentTransferEncoding {
    SEVEN_BIT("7bit"),
    EIGHT_BIT("8bit"),
    BINARY("binary"),
    QUOTED_PRINTABLE("quoted-printable"),
    BASE64("base64");

    private final String wire;

    ContentTransferEncoding(String wire) {
        this.wire = wire;
    }

    /** The value used in the {@code Content-Transfer-Encoding} header. */
    public String wire() {
        return wire;
    }
}
