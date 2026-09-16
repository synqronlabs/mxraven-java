package com.mxraven.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.mxraven.admin.client.AuthClient;
import com.mxraven.admin.exception.ApiException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Client for the mxRaven control-plane v2 REST API.
 *
 * <p>Construct one with the control-plane base URL and a ZITADEL-issued bearer
 * access token, then reach resource families through the typed accessors:
 *
 * <pre>{@code
 * AdminClient admin = new AdminClient("https://api.mxraven.com", token);
 * for (Domain domain : admin.workspace("my-workspace").domains().list()) {
 *     System.out.println(domain.domainName() + " " + domain.status());
 * }
 * }</pre>
 *
 * <p>All response timestamps are returned as ISO-8601 strings. Pagination is
 * cursor-based: pass {@link Page#nextPageToken()} back into the same list call.
 */
public final class AdminClient implements AutoCloseable {
    /**
     * Default control-plane API version. Every request path is prefixed with
     * {@code "/" + apiVersion} in a single place, so clients never hardcode it.
     */
    public static final String DEFAULT_API_VERSION = "v2";

    /**
     * Default mxRaven control-plane base URL. Used by the constructors that omit
     * a base URL; pass an explicit URL to target another environment.
     */
    public static final String DEFAULT_BASE_URL = "https://api.mxraven.email";

    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .setDefaultPropertyInclusion(JsonInclude.Value.construct(
                    JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final HttpClient http;
    private final ObjectMapper json;
    private final String baseUrl;
    private final String token;
    private final RateLimitConfig rateLimitConfig;
    private final String apiVersion;

    /**
     * Construct a client against {@link #DEFAULT_BASE_URL}.
     *
     * @param token ZITADEL-issued bearer access token
     */
    public AdminClient(String token) {
        this(DEFAULT_BASE_URL, token, RateLimitConfig.defaults(), DEFAULT_API_VERSION);
    }

    /**
     * Construct a client against {@link #DEFAULT_BASE_URL} with explicit
     * rate-limit behaviour.
     *
     * @param token ZITADEL-issued bearer access token
     * @param rateLimitConfig client-wide rate-limit behaviour
     */
    public AdminClient(String token, RateLimitConfig rateLimitConfig) {
        this(DEFAULT_BASE_URL, token, rateLimitConfig, DEFAULT_API_VERSION);
    }

    /**
     * Construct a client against {@link #DEFAULT_BASE_URL} with a custom HTTP
     * client and mapper.
     *
     * @param token ZITADEL-issued bearer access token
     * @param http HTTP client used to send requests
     * @param json mapper used to encode and decode JSON
     */
    public AdminClient(String token, HttpClient http, ObjectMapper json) {
        this(DEFAULT_BASE_URL, token, http, json, RateLimitConfig.defaults(), DEFAULT_API_VERSION);
    }

    /**
     * Construct a client against an explicit control-plane base URL.
     *
     * @param baseUrl control-plane base URL; trailing slashes are stripped
     * @param token ZITADEL-issued bearer access token
     */
    public AdminClient(String baseUrl, String token) {
        this(baseUrl, token, RateLimitConfig.defaults(), DEFAULT_API_VERSION);
    }

    /**
     * Construct a client with explicit rate-limit behaviour.
     *
     * @param baseUrl control-plane base URL; trailing slashes are stripped
     * @param token ZITADEL-issued bearer access token
     * @param rateLimitConfig client-wide rate-limit behaviour
     */
    public AdminClient(String baseUrl, String token, RateLimitConfig rateLimitConfig) {
        this(baseUrl, token, rateLimitConfig, DEFAULT_API_VERSION);
    }

    /**
     * Construct a client targeting a specific control-plane API version. The
     * version is prepended to every request path (for example {@code "v2"}
     * yields {@code /v2/...}). Pass {@link #DEFAULT_API_VERSION} for the default.
     *
     * @param baseUrl control-plane base URL; trailing slashes are stripped
     * @param token ZITADEL-issued bearer access token
     * @param apiVersion API version prefix applied to every request path
     */
    public AdminClient(String baseUrl, String token, String apiVersion) {
        this(baseUrl, token, RateLimitConfig.defaults(), apiVersion);
    }

    /**
     * Construct a client with explicit rate-limit behaviour and API version.
     *
     * @param baseUrl control-plane base URL; trailing slashes are stripped
     * @param token ZITADEL-issued bearer access token
     * @param rateLimitConfig client-wide rate-limit behaviour
     * @param apiVersion API version prefix applied to every request path
     */
    public AdminClient(String baseUrl, String token, RateLimitConfig rateLimitConfig, String apiVersion) {
        this(baseUrl, token, HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build(), DEFAULT_MAPPER, rateLimitConfig, apiVersion);
    }

    /**
     * Construct a client with a custom HTTP client and mapper against an
     * explicit base URL.
     *
     * @param baseUrl control-plane base URL; trailing slashes are stripped
     * @param token ZITADEL-issued bearer access token
     * @param http HTTP client used to send requests
     * @param json mapper used to encode and decode JSON
     */
    public AdminClient(String baseUrl, String token, HttpClient http, ObjectMapper json) {
        this(baseUrl, token, http, json, RateLimitConfig.defaults(), DEFAULT_API_VERSION);
    }

    private AdminClient(String baseUrl, String token, HttpClient http, ObjectMapper json,
                        RateLimitConfig rateLimitConfig, String apiVersion) {
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.token = token;
        this.http = http;
        this.json = json;
        this.rateLimitConfig = rateLimitConfig == null ? RateLimitConfig.defaults() : rateLimitConfig;
        this.apiVersion = normalizeApiVersion(apiVersion);
    }

    /**
     * Returns the control-plane base URL, without a trailing slash.
     *
     * @return the normalized base URL
     */
    public String baseUrl() {
        return baseUrl;
    }

    /**
     * The control-plane API version prefix applied to every request path.
     *
     * @return the API version prefix, for example {@code "v2"}
     */
    public String apiVersion() {
        return apiVersion;
    }

    /**
     * Returns the mapper used to encode request bodies and decode response bodies.
     *
     * @return the JSON mapper
     */
    public ObjectMapper json() {
        return json;
    }

    /**
     * The client-wide rate-limit behaviour.
     *
     * @return the rate-limit configuration
     */
    public RateLimitConfig rateLimitConfig() {
        return rateLimitConfig;
    }

    // --- Tenant-scoped resource families -------------------------------------

    /**
     * Bind a tenant. The returned {@link Workspace} exposes every tenant-scoped
     * resource family without repeating the slug.
     *
     * @param tenantSlug tenant slug to bind
     * @return a workspace bound to the given tenant
     */
    public Workspace workspace(String tenantSlug) {
        return new Workspace(this, tenantSlug);
    }

    // --- Public (unauthenticated) families -----------------------------------

    /**
     * Public tenant login context; works with a blank token.
     *
     * @return a client for the public authentication family
     */
    public AuthClient auth() {
        return new AuthClient(this);
    }

    /**
     * Issues a GET request.
     *
     * @param path client-relative path; the configured API version is prepended
     * @return the decoded response
     * @throws IOException if the request fails or is interrupted
     */
    public Response get(String path) throws IOException {
        return get(path, null);
    }

    /**
     * Issues a GET request with query parameters.
     *
     * @param path client-relative path; the configured API version is prepended
     * @param query query parameters, or {@code null} for none
     * @return the decoded response
     * @throws IOException if the request fails or is interrupted
     */
    public Response get(String path, Map<String, String> query) throws IOException {
        return request("GET", path, query, null);
    }

    /**
     * Issues a POST request with a JSON body.
     *
     * @param path client-relative path; the configured API version is prepended
     * @param body object serialized as the JSON request body
     * @return the decoded response
     * @throws IOException if the request fails or is interrupted
     */
    public Response post(String path, Object body) throws IOException {
        return request("POST", path, null, body);
    }

    /**
     * Issues a POST request with query parameters and a JSON body.
     *
     * @param path client-relative path; the configured API version is prepended
     * @param query query parameters, or {@code null} for none
     * @param body object serialized as the JSON request body
     * @return the decoded response
     * @throws IOException if the request fails or is interrupted
     */
    public Response post(String path, Map<String, String> query, Object body) throws IOException {
        return request("POST", path, query, body);
    }

    /**
     * Issues a PUT request with a JSON body.
     *
     * @param path client-relative path; the configured API version is prepended
     * @param body object serialized as the JSON request body
     * @return the decoded response
     * @throws IOException if the request fails or is interrupted
     */
    public Response put(String path, Object body) throws IOException {
        return request("PUT", path, null, body);
    }

    /**
     * Issues a DELETE request.
     *
     * @param path client-relative path; the configured API version is prepended
     * @return the decoded response
     * @throws IOException if the request fails or is interrupted
     */
    public Response delete(String path) throws IOException {
        return request("DELETE", path, null, null);
    }

    /**
     * Issue a GET and wrap the response in a lazily auto-paginating {@link Paged}.
     * Subsequent pages are fetched by re-issuing the same request with the next
     * {@code page_token}, only as the returned iterator advances.
     *
     * @param path client-relative path; the configured API version is prepended
     * @param query query parameters, or {@code null} for none
     * @param elementType type of each item in the collection
     * @param <T> element type
     * @return a lazily paginating collection over the response items
     * @throws IOException if the first page request fails or is interrupted
     */
    public <T> Paged<T> paged(String path, Map<String, String> query, Class<T> elementType) throws IOException {
        Function<String, Page<T>> fetchNext = pageFetcher(path, query, elementType);
        Page<T> first = get(path, query).pageOf(elementType).withFetcher(fetchNext);
        return new Paged<>(first, fetchNext);
    }

    private <T> Function<String, Page<T>> pageFetcher(String path, Map<String, String> query, Class<T> elementType) {
        return token -> {
            Map<String, String> next = new LinkedHashMap<>();
            if (query != null) {
                next.putAll(query);
            }
            next.put("page_token", token);
            try {
                return get(path, next).pageOf(elementType).withFetcher(pageFetcher(path, query, elementType));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

    /**
     * Send a request to the control plane. A non-success status is decoded as an
     * RFC 9457 problem and thrown as {@link ApiException}.
     *
     * @param method HTTP method to use
     * @param path client-relative path; the configured API version is prepended
     * @param query query parameters, or {@code null} for none
     * @param body request body serialized as JSON, or {@code null} for no body
     * @return the decoded successful response
     * @throws IOException if the request fails or is interrupted
     * @throws ApiException if the control plane returns a non-success status
     */
    public Response request(String method, String path, Map<String, String> query, Object body) throws IOException {
        String resolvedPath = resolvePath(path);
        URI uri = URI.create(baseUrl + resolvedPath + encodeQuery(query));
        HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(60))
                .header("Accept", "application/json");
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }
        if (body != null) {
            builder.header("Content-Type", "application/json")
                    .method(method, HttpRequest.BodyPublishers.ofString(json.writeValueAsString(body)));
        } else {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        }

        HttpRequest httpRequest = builder.build();
        int attempt = 0;
        while (true) {
            HttpResponse<String> response;
            try {
                response = http.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("interrupted while calling " + method + " " + resolvedPath, e);
            }

            if (response.statusCode() == 429
                    && isRetryable(method)
                    && rateLimitConfig.enabled()
                    && attempt < rateLimitConfig.maxRetries()) {
                attempt++;
                sleep(retryDelay(response));
                continue;
            }

            JsonNode decoded = parseBody(response.body());
            if (response.statusCode() >= 400) {
                throw ApiException.from(response.statusCode(), response.body(), decoded, json,
                        parseRetryAfter(response.headers()));
            }
            return new Response(response.statusCode(), response.headers(), decoded, json);
        }
    }

    /**
     * Prepend the configured API version to a client-relative path. Paths that
     * already carry the version prefix are left untouched, so callers may pass
     * either {@code "/tenants/acme"} or {@code "/v2/tenants/acme"}.
     */
    private String resolvePath(String path) {
        String prefix = "/" + apiVersion;
        if (path == null || path.isEmpty() || "/".equals(path)) {
            return prefix;
        }
        if (path.equals(prefix) || path.startsWith(prefix + "/")) {
            return path;
        }
        return prefix + (path.startsWith("/") ? path : "/" + path);
    }

    private static String normalizeApiVersion(String apiVersion) {
        if (apiVersion == null || apiVersion.isBlank()) {
            return DEFAULT_API_VERSION;
        }
        return apiVersion.replaceAll("^/+", "").replaceAll("/+$", "");
    }

    /** Reads and idempotent deletes may be retried safely; mutations may not. */
    private static boolean isRetryable(String method) {
        return "GET".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method);
    }

    private Duration retryDelay(HttpResponse<?> response) {
        Duration retryAfter = parseRetryAfter(response.headers());
        Duration delay = retryAfter != null ? retryAfter : rateLimitConfig.defaultBackoff();
        if (delay.isNegative()) {
            delay = Duration.ZERO;
        }
        if (delay.compareTo(rateLimitConfig.maxBackoff()) > 0) {
            delay = rateLimitConfig.maxBackoff();
        }
        return delay;
    }

    private static void sleep(Duration delay) throws IOException {
        if (delay.isZero()) {
            return;
        }
        try {
            Thread.sleep(delay.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("interrupted while waiting to retry after rate limit", e);
        }
    }

    private static Duration parseRetryAfter(HttpHeaders headers) {
        return headers.firstValue("Retry-After").map(AdminClient::parseRetryAfter).orElse(null);
    }

    static Duration parseRetryAfter(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        try {
            long seconds = Long.parseLong(trimmed);
            return seconds < 0 ? Duration.ZERO : Duration.ofSeconds(seconds);
        } catch (NumberFormatException ignored) {
            // not delta-seconds; fall through to HTTP-date parsing
        }
        try {
            ZonedDateTime retryAt = ZonedDateTime.parse(trimmed, DateTimeFormatter.RFC_1123_DATE_TIME);
            Duration delay = Duration.between(Instant.now(), retryAt.toInstant());
            return delay.isNegative() ? Duration.ZERO : delay;
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    @Override
    public void close() {
        // No-op: java.net.http.HttpClient manages its own resources and does not
        // require explicit closing on Java 17. Retained to satisfy AutoCloseable.
    }

    private JsonNode parseBody(String body) throws IOException {
        if (body == null || body.isBlank()) {
            return null;
        }
        return json.readTree(body);
    }

    private static String encodeQuery(Map<String, String> query) {
        if (query == null || query.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : query.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            sb.append(sb.isEmpty() ? '?' : '&');
            sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append('=')
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return sb.toString();
    }
}
