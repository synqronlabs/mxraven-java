package com.mxraven.mail.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A single header field, consisting of a name and a value.
 *
 * <p>A header can never carry a CR, LF, or NUL character. Accepting those would
 * allow a caller (or a parsed message) to inject additional header fields or a
 * body when the field is serialized, so they are rejected at construction.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Header {
    private final String name;
    private final String value;

    /** the field name */
    public String name() {
        return name;
    }

    /** the field value */
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Header that = (Header) o;
        return Objects.equals(this.name, that.name)
                && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }

    @Override
    public String toString() {
        return "Header[" + "name=" + this.name + ", " + "value=" + this.value + "]";
    }

    /**
     * Creates a new Header.
     *
     * @param name the field name
     * @param value the field value
     */
    @JsonCreator
    public Header(String name, String value) {
        name = name == null ? "" : name;
        value = value == null ? "" : value;
        if (name.isEmpty()) {
            throw new IllegalArgumentException("header name must not be empty");
        }
        rejectInjection(name, "header name");
        rejectInjection(value, "header value");
        this.name = name;
        this.value = value;
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
