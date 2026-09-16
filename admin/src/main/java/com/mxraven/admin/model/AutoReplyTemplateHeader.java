package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * A custom header stored on an auto-reply template.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class AutoReplyTemplateHeader {
    private final String name;
    private final String value;

    /** header name */
    public String name() {
        return name;
    }

    /** header value */
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
        AutoReplyTemplateHeader that = (AutoReplyTemplateHeader) o;
        return Objects.equals(this.name, that.name)
                && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }

    @Override
    public String toString() {
        return "AutoReplyTemplateHeader[" + "name=" + this.name + ", " + "value=" + this.value + "]";
    }

    /**
     * Creates a new AutoReplyTemplateHeader.
     *
     * @param name header name
     * @param value header value
     */
    @JsonCreator
    public AutoReplyTemplateHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    static void validateList(List<AutoReplyTemplateHeader> headers, boolean required) {
        if (headers == null) {
            if (required) {
                throw new IllegalArgumentException("headers is required");
            }
            return;
        }
        if (headers.size() > 10000) {
            throw new IllegalArgumentException("headers must contain 10000 entries or fewer");
        }
        for (int index = 0; index < headers.size(); index++) {
            AutoReplyTemplateHeader header = headers.get(index);
            if (header == null || header.name() == null || Java8.isBlank(header.name())) {
                throw new IllegalArgumentException("headers[" + index + "].name is required");
            }
            if (header.name().length() > 255) {
                throw new IllegalArgumentException("headers[" + index + "].name must be 255 characters or fewer");
            }
            for (int position = 0; position < header.name().length(); position++) {
                char character = header.name().charAt(position);
                if (character <= 32 || character >= 127 || character == ':') {
                    throw new IllegalArgumentException("headers[" + index + "].name is invalid");
                }
            }
            if (header.value() != null && header.value().length() > 1_048_576) {
                throw new IllegalArgumentException(
                        "headers[" + index + "].value must be 1048576 characters or fewer");
            }
        }
    }
}
