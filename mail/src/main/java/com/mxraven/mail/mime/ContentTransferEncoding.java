package com.mxraven.mail.mime;

/**
 * MIME Content-Transfer-Encoding values (RFC 2045 §6).
 */
public enum ContentTransferEncoding {
    /** 7-bit text ({@code 7bit}). */
    SEVEN_BIT("7bit"),
    /** 8-bit text ({@code 8bit}). */
    EIGHT_BIT("8bit"),
    /** Arbitrary binary data ({@code binary}). */
    BINARY("binary"),
    /** Quoted-printable text ({@code quoted-printable}). */
    QUOTED_PRINTABLE("quoted-printable"),
    /** Base64-encoded data ({@code base64}). */
    BASE64("base64");

    private final String wire;

    ContentTransferEncoding(String wire) {
        this.wire = wire;
    }

    /**
     * The value used in the {@code Content-Transfer-Encoding} header.
     *
     * @return the wire value
     */
    public String wire() {
        return wire;
    }
}
