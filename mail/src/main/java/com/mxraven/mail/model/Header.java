package com.mxraven.mail.model;

/**
 * A single header field, consisting of a name and a value.
 *
 * <p>A header can never carry a CR, LF, or NUL character. Accepting those would
 * allow a caller (or a parsed message) to inject additional header fields or a
 * body when the field is serialized, so they are rejected at construction.
 *
 * @param name  the field name; a {@code null} value is replaced with {@code ""}
 * @param value the field value; a {@code null} value is replaced with {@code ""}
 */
public record Header(String name, String value) {
    /**
     * Creates a header field, rejecting characters that could break the header
     * block apart.
     *
     * @throws IllegalArgumentException when the name is empty or either component
     *                                  contains CR, LF, or NUL
     */
    public Header {
        name = name == null ? "" : name;
        value = value == null ? "" : value;
        if (name.isEmpty()) {
            throw new IllegalArgumentException("header name must not be empty");
        }
        rejectInjection(name, "header name");
        rejectInjection(value, "header value");
    }

    private static void rejectInjection(String text, String what) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\r' || c == '\n' || c == '\0') {
                throw new IllegalArgumentException(
                        what + " must not contain CR, LF, or NUL characters");
            }
        }
    }
}
