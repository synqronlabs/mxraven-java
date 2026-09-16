package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import java.util.List;
import java.util.regex.Pattern;

/** Shared validation helpers for request records. */
final class RequestSupport {
    private static final Pattern REFERENCE = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*$");

    private RequestSupport() {
    }

    static String requireString(String value, String field, int maxLength) {
        if (value == null || Java8.isBlank(value)) {
            throw new IllegalArgumentException(field + " is required");
        }
        return optionalString(value, field, maxLength);
    }

    static String optionalString(String value, String field, int maxLength) {
        if (value != null && value.codePointCount(0, value.length()) > maxLength) {
            throw new IllegalArgumentException(field + " must be " + maxLength + " characters or fewer");
        }
        return value;
    }

    static String requireRef(String value, String field, int maxLength) {
        String trimmed = requireString(value, field, maxLength);
        if (!REFERENCE.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(field + " must start with a letter or digit and may contain "
                    + "letters, digits, dots, underscores, or hyphens");
        }
        return trimmed;
    }

    static <T> List<T> requireList(List<T> value, String field, int maxItems) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(field + " must not be empty");
        }
        return optionalList(value, field, maxItems);
    }

    static <T> List<T> optionalList(List<T> value, String field, int maxItems) {
        if (value != null && value.size() > maxItems) {
            throw new IllegalArgumentException(field + " must contain " + maxItems + " entries or fewer");
        }
        return value;
    }

    static <T> T require(T value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value;
    }
}
