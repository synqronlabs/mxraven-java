package com.mxraven.admin;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fluent builder for v2 query parameters. Null values are ignored, so optional
 * filters can be chained unconditionally.
 */
public final class QueryParams {
    /** Minimum length the control plane accepts for the {@code q} search parameter. */
    public static final int MIN_SEARCH_LENGTH = 3;

    private final Map<String, String> values = new LinkedHashMap<>();

    private QueryParams() {
    }

    public static QueryParams create() {
        return new QueryParams();
    }

    public QueryParams put(String key, Object value) {
        if (value != null) {
            values.put(key, String.valueOf(value));
        }
        return this;
    }

    /** Sets {@code page_size}; the control plane accepts 1–500. */
    public QueryParams pageSize(int pageSize) {
        if (pageSize < 1 || pageSize > 500) {
            throw new IllegalArgumentException("page_size must be between 1 and 500");
        }
        return put("page_size", pageSize);
    }

    public QueryParams pageToken(String pageToken) {
        return put("page_token", pageToken);
    }

    /**
     * Sets the {@code q} search filter. The control plane requires at least
     * {@value #MIN_SEARCH_LENGTH} characters and rejects shorter values with an
     * opaque {@code 400 invalid_request}. A {@code null} or blank value is
     * ignored; a shorter non-blank value is rejected locally.
     */
    public QueryParams q(String query) {
        if (query == null || query.isBlank()) {
            return this;
        }
        String value = query.trim();
        if (value.length() < MIN_SEARCH_LENGTH) {
            throw new IllegalArgumentException(
                    "search must be at least " + MIN_SEARCH_LENGTH + " characters (got \"" + value + "\")");
        }
        return put("q", value);
    }

    public Map<String, String> toMap() {
        return values;
    }
}
