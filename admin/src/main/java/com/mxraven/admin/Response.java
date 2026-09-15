package com.mxraven.admin;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxraven.admin.exception.ApiException;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Optional;

/**
 * A successful control-plane HTTP response with its status, headers, and decoded
 * JSON body. Error responses are turned into {@link ApiException} by
 * {@link AdminClient} and never surface here.
 */
public final class Response {
    private final int status;
    private final HttpHeaders headers;
    private final JsonNode body;
    private final ObjectMapper json;

    Response(int status, HttpHeaders headers, JsonNode body, ObjectMapper json) {
        this.status = status;
        this.headers = headers;
        this.body = body;
        this.json = json;
    }

    public int status() {
        return status;
    }

    public HttpHeaders headers() {
        return headers;
    }

    /** First value of a response header, case-insensitive. */
    public Optional<String> header(String name) {
        return headers.firstValue(name);
    }

    /** Decoded JSON body, or {@code null} when the response had no body. */
    public JsonNode body() {
        return body;
    }

    /** Convert this response body to a single object. */
    public <T> T as(Class<T> type) {
        if (body == null || body.isNull()) {
            return null;
        }
        return json.convertValue(body, type);
    }

    /** Convert this response body to a list of objects. */
    public <T> List<T> listOf(Class<T> elementType) {
        if (body == null || body.isNull()) {
            return List.of();
        }
        JavaType listType = json.getTypeFactory().constructCollectionType(List.class, elementType);
        return json.convertValue(body, listType);
    }

    /**
     * Build a page from this response using the standard {@code X-Next-Page-Token},
     * {@code X-Previous-Page-Token}, and {@code X-Last-Page-Token} headers.
     */
    public <T> Page<T> pageOf(Class<T> elementType) {
        return new Page<>(
                listOf(elementType),
                header("X-Next-Page-Token").orElse(null),
                header("X-Previous-Page-Token").orElse(null),
                header("X-Last-Page-Token").orElse(null));
    }
}
