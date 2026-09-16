package com.mxraven.mail.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A single header field, consisting of a name and a value.
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
        this.name = name;
        this.value = value;
    }
}
