package com.mxraven.admin;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxraven.admin.exception.ApiException;

import java.util.List;
import java.util.Optional;

import okhttp3.Headers;

/**
 * A successful control-plane HTTP response with its status, headers, and decoded
 * JSON body. Error responses are turned into {@link ApiException} by
 * {@link AdminClient} and never surface here.
 */
public final class Response {
    private final int status;
    private final Headers headers;
    private final JsonNode body;
    private final ObjectMapper json;

    Response(int status, Headers headers, JsonNode body, ObjectMapper json) {
        this.status = status;
        this.headers = headers;
        this.body = body;
        this.json = json;
    }

    /**
     * Returns the HTTP status code.
     *
     * @return the status code
     */
    public int status() {
        return status;
    }

    /**
     * Returns the response headers.
     *
     * @return the response headers
     */
    public Headers headers() {
        return headers;
    }

    /**
     * First value of a response header, case-insensitive.
     *
     * @param name header name
     * @return the first header value, or empty when the header is absent
     */
    public Optional<String> header(String name) {
        return Optional.ofNullable(headers.get(name));
    }

    /**
     * Decoded JSON body, or {@code null} when the response had no body.
     *
     * @return the decoded body, or {@code null} when there was no body
     */
    public JsonNode body() {
        return body;
    }

    /**
     * Convert this response body to a single object.
     *
     * @param type target type
     * @param <T> target type
     * @return the converted object, or {@code null} when the body is absent
     */
    public <T> T as(Class<T> type) {
        if (body == null || body.isNull()) {
            return null;
        }
        return json.convertValue(body, type);
    }

    /**
     * Convert this response body to a list of objects.
     *
     * @param elementType type of each list element
     * @param <T> element type
     * @return the converted list, empty when the body is absent
     */
    public <T> List<T> listOf(Class<T> elementType) {
        if (body == null || body.isNull()) {
            return Java8.list();
        }
        JavaType listType = json.getTypeFactory().constructCollectionType(List.class, elementType);
        return json.convertValue(body, listType);
    }

    /**
     * Build a page from this response using the standard {@code X-Next-Page-Token},
     * {@code X-Previous-Page-Token}, and {@code X-Last-Page-Token} headers.
     *
     * @param elementType type of each page item
     * @param <T> element type
     * @return a page built from this response body and pagination headers
     */
    public <T> Page<T> pageOf(Class<T> elementType) {
        return new Page<>(
                listOf(elementType),
                header("X-Next-Page-Token").orElse(null),
                header("X-Previous-Page-Token").orElse(null),
                header("X-Last-Page-Token").orElse(null));
    }
}
