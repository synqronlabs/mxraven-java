package com.mxraven.mail.mime;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Decodes RFC 2047 encoded-words ({@code =?charset?B?...?=}, {@code =?charset?Q?...?=})
 * used in header values such as {@code Subject} and display names.
 */
final class EncodedWords {
    private static final Pattern ENCODED_WORD =
            Pattern.compile("=\\?([^?\\s]+)\\?([bBqQ])\\?([^?\\s]*)\\?=");

    private EncodedWords() {
    }

    /** Decodes every encoded-word in {@code value}, leaving other text intact. */
    static String decode(String value) {
        if (value == null || value.indexOf("=?") < 0) {
            return value;
        }
        Matcher matcher = ENCODED_WORD.matcher(value);
        StringBuilder out = new StringBuilder(value.length());
        int last = 0;
        boolean found = false;
        while (matcher.find()) {
            found = true;
            out.append(value, last, matcher.start());
            out.append(decodeWord(matcher.group(1), matcher.group(2), matcher.group(3)));
            last = matcher.end();
        }
        if (!found) {
            return value;
        }
        out.append(value, last, value.length());
        return out.toString();
    }

    private static String decodeWord(String charset, String encoding, String data) {
        byte[] bytes = encoding.equalsIgnoreCase("B")
                ? Base64.getMimeDecoder().decode(data)
                : decodeQ(data);
        return decodeCharset(bytes, charset);
    }

    /** Decodes Q-encoding: {@code _} is a space and {@code =XX} is a byte. */
    private static byte[] decodeQ(String data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length());
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c == '_') {
                out.write(' ');
            } else if (c == '=' && i + 2 < data.length()) {
                int hi = Character.digit(data.charAt(i + 1), 16);
                int lo = Character.digit(data.charAt(i + 2), 16);
                if (hi >= 0 && lo >= 0) {
                    out.write((hi << 4) | lo);
                    i += 2;
                } else {
                    out.write(c);
                }
            } else {
                out.write(c & 0xff);
            }
        }
        return out.toByteArray();
    }

    static String decodeCharset(byte[] bytes, String charset) {
        Charset resolved;
        if (charset == null || charset.isBlank()) {
            resolved = StandardCharsets.UTF_8;
        } else {
            try {
                resolved = Charset.forName(charset.trim());
            } catch (RuntimeException e) {
                resolved = StandardCharsets.UTF_8;
            }
        }
        return new String(bytes, resolved);
    }
}
