package com.mxraven.mail;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * RFC 3461 xtext encoding used by the DSN {@code ENVID} and {@code ORCPT} SMTP
 * parameters.
 *
 * <p>Printable US-ASCII except {@code +} and {@code =} passes through; any other
 * byte is encoded as {@code +XX} (uppercase hex).
 */
public final class DsnXText {
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    private DsnXText() {
    }

    /** Encodes a value for use in an xtext parameter. */
    public static String encode(String decoded) {
        if (decoded == null) {
            return null;
        }
        StringBuilder out = new StringBuilder(decoded.length());
        for (byte raw : decoded.getBytes(StandardCharsets.UTF_8)) {
            int c = raw & 0xff;
            if (c >= '!' && c <= '~' && c != '+' && c != '=') {
                out.append((char) c);
            } else {
                out.append('+').append(HEX[(c >> 4) & 0xf]).append(HEX[c & 0xf]);
            }
        }
        return out.toString();
    }

    /**
     * Decodes an xtext parameter.
     *
     * @throws IllegalArgumentException when a {@code +} escape is malformed
     */
    public static String decode(String wire) {
        if (wire == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(wire.length());
        for (int i = 0; i < wire.length(); i++) {
            char c = wire.charAt(i);
            if (c == '+') {
                if (i + 2 >= wire.length()) {
                    throw new IllegalArgumentException("truncated xtext escape");
                }
                int hi = Character.digit(wire.charAt(i + 1), 16);
                int lo = Character.digit(wire.charAt(i + 2), 16);
                if (hi < 0 || lo < 0) {
                    throw new IllegalArgumentException("invalid xtext escape");
                }
                out.write((hi << 4) | lo);
                i += 2;
            } else {
                out.write(c & 0xff);
            }
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
}
