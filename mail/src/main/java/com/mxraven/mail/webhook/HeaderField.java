package com.mxraven.mail.webhook;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One message header occurrence, in the order received.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class HeaderField {
    private final String name;
    private final String value;

    /** the header field name */
    public String name() {
        return name;
    }

    /** the header field value */
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
        HeaderField that = (HeaderField) o;
        return Objects.equals(this.name, that.name)
                && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }

    @Override
    public String toString() {
        return "HeaderField[" + "name=" + this.name + ", " + "value=" + this.value + "]";
    }

    /**
     * Creates a new HeaderField.
     *
     * @param name the header field name
     * @param value the header field value
     */
    @JsonCreator
    public HeaderField(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
