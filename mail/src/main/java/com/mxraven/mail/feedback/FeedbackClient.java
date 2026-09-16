package com.mxraven.mail.feedback;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Client for the mxRaven feedback service.
 *
 * <p>Tenants can teach the mxRaven spam filter by submitting messages that were
 * misclassified. A message is matched to its stored evidence by hash, so the
 * exact raw RFC 822 bytes mxRaven processed must be submitted.
 *
 * <pre>{@code
 * FeedbackClient feedback = FeedbackClient.builder()
 *         .baseUrl("https://feedback.mxraven.com")
 *         .credentials("mxr_tx_ab12cd34ef56", apiKeySecret)
 *         .build();
 *
 * LearningResult result = feedback.learnSpam(rawMessage);
 * System.out.println(result.disposition() + " matched " + result.matchedHashKind());
 * }</pre>
 *
 * <p>The credentials are the submission API key: the username is the key's
 * username and the secret is the key's secret. Only the learning methods require
 * credentials; {@link #unsubscribe(String)} is unauthenticated.
 *
 * <p>A configured {@code FeedbackClient} is safe for concurrent use. The
 * {@code *Async} methods return a {@link CompletableFuture}; cancelling it (with
 * {@link CompletableFuture#cancel(boolean)}) aborts that request without
 * affecting others.
 */
public final class FeedbackClient {
    /** Default request timeout for non-streaming calls. */
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    /** Default maximum number of request bytes read from an input stream. */
    public static final long DEFAULT_MAX_REQUEST_BYTES = 64L * 1024 * 1024;

    /** Maximum number of response bytes accepted from the service. */
    private static final int MAX_RESPONSE_BYTES = 64 * 1024;

    private static final String LEARN_PATH = "/v1/feedback/learn/";
    private static final String UNSUBSCRIBE_PATH = "/v1/feedback/unsubscribe/";

    private final String baseUrl;
    private final String username;
    private final String secret;
    private final HttpClient http;
    private final Duration requestTimeout;
    private final long maxRequestBytes;

    private FeedbackClient(Builder builder) {
        this.baseUrl = builder.baseUrl.replaceAll("/+$", "");
        this.username = builder.username;
        this.secret = builder.secret;
        this.http = builder.httpClient;
        this.requestTimeout = builder.requestTimeout;
        this.maxRequestBytes = builder.maxRequestBytes;
    }

    /**
     * Creates a feedback client builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the configured feedback service base URL.
     *
     * @return the base URL
     */
    public String baseUrl() {
        return baseUrl;
    }

    /**
     * Teaches the spam filter that {@code rawMime} is spam.
     *
     * @param rawMime the exact raw RFC 822 message bytes mxRaven processed
     * @return the learning result
     * @throws IOException when the request fails
     */
    public LearningResult learnSpam(byte[] rawMime) throws IOException {
        return learn(Disposition.SPAM, rawMime);
    }

    /**
     * Teaches the spam filter that {@code rawMime} is not spam.
     *
     * @param rawMime the exact raw RFC 822 message bytes mxRaven processed
     * @return the learning result
     * @throws IOException when the request fails
     */
    public LearningResult learnHam(byte[] rawMime) throws IOException {
        return learn(Disposition.HAM, rawMime);
    }

    /**
     * Teaches the spam filter that {@code rawMime} is spam.
     *
     * @param rawMime the raw RFC 822 message stream, read fully
     * @return the learning result
     * @throws IOException when the request or the read fails
     */
    public LearningResult learnSpam(InputStream rawMime) throws IOException {
        return learn(Disposition.SPAM, rawMime);
    }

    /**
     * Teaches the spam filter that {@code rawMime} is not spam.
     *
     * @param rawMime the raw RFC 822 message stream, read fully
     * @return the learning result
     * @throws IOException when the request or the read fails
     */
    public LearningResult learnHam(InputStream rawMime) throws IOException {
        return learn(Disposition.HAM, rawMime);
    }

    /**
     * Submits one training example with the given disposition.
     *
     * <p>{@code rawMime} must be the exact raw RFC 822 bytes mxRaven processed,
     * including the original line endings. A message with no matching evidence
     * fails with a {@link FeedbackException} whose {@link FeedbackException#statusCode()
     * status code} is {@code 404}.
     *
     * @param disposition the training label to apply
     * @param rawMime     the exact raw RFC 822 message bytes mxRaven processed
     * @return the learning result
     * @throws IOException              when the request fails
     * @throws IllegalArgumentException when the disposition or raw message is {@code null}
     * @throws IllegalStateException    when learning credentials are not configured
     * @throws FeedbackException        when the service returns a non-success status
     */
    public LearningResult learn(Disposition disposition, byte[] rawMime) throws IOException {
        HttpRequest request = learnRequest(disposition, rawMime);
        Response response = send(request);
        if (response.status() != 200) {
            throw httpError(response);
        }
        return decode(response.body());
    }

    /**
     * Submits one training example read fully from {@code rawMime}.
     *
     * @param disposition the training label to apply
     * @param rawMime     the raw RFC 822 message stream, read fully
     * @return the learning result
     * @throws IOException              when the request or the read fails
     * @throws IllegalArgumentException when the raw message is {@code null}
     * @throws IllegalStateException    when learning credentials are not configured
     * @throws FeedbackException        when the service returns a non-success status
     */
    public LearningResult learn(Disposition disposition, InputStream rawMime) throws IOException {
        if (rawMime == null) {
            throw new IllegalArgumentException("rawMime is required");
        }
        return learn(disposition, readCapped(rawMime, maxRequestBytes));
    }

    /**
     * Teaches the spam filter that {@code rawMime} is spam without blocking.
     *
     * <p>Cancelling the returned future aborts this request only.
     *
     * @param rawMime the exact raw RFC 822 message bytes mxRaven processed
     * @return a future completing with the learning result
     * @throws IllegalStateException when learning credentials are not configured
     */
    public CompletableFuture<LearningResult> learnSpamAsync(byte[] rawMime) {
        return learnAsync(Disposition.SPAM, rawMime);
    }

    /**
     * Teaches the spam filter that {@code rawMime} is not spam without blocking.
     *
     * <p>Cancelling the returned future aborts this request only.
     *
     * @param rawMime the exact raw RFC 822 message bytes mxRaven processed
     * @return a future completing with the learning result
     * @throws IllegalStateException when learning credentials are not configured
     */
    public CompletableFuture<LearningResult> learnHamAsync(byte[] rawMime) {
        return learnAsync(Disposition.HAM, rawMime);
    }

    /**
     * Submits one training example without blocking.
     *
     * <p>Cancelling the returned future aborts this request only.
     *
     * @param disposition the training label to apply
     * @param rawMime     the exact raw RFC 822 message bytes mxRaven processed
     * @return a future completing with the learning result
     * @throws IllegalArgumentException when the disposition or raw message is {@code null}
     * @throws IllegalStateException    when learning credentials are not configured
     */
    public CompletableFuture<LearningResult> learnAsync(Disposition disposition, byte[] rawMime) {
        HttpRequest request = learnRequest(disposition, rawMime);
        return sendAsync(request).thenApply(response -> {
            if (response.status() != 200) {
                throw new CompletionException(httpError(response));
            }
            try {
                return decode(response.body());
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Performs an RFC 8058 one-click unsubscribe for a token. This is the
     * operation a recipient mail client performs against the
     * {@code List-Unsubscribe} URL; applications rarely call it directly.
     *
     * @param token the unsubscribe token
     * @throws IOException              when the request fails
     * @throws IllegalArgumentException when the token is {@code null} or blank
     * @throws FeedbackException        when the service returns a non-success status
     */
    public void unsubscribe(String token) throws IOException {
        Response response = send(unsubscribeRequest(token));
        if (response.status() != 200) {
            throw httpError(response);
        }
    }

    /**
     * Performs a one-click unsubscribe without blocking.
     *
     * <p>Cancelling the returned future aborts this request only.
     *
     * @param token the unsubscribe token
     * @return a future completing when the request finishes
     * @throws IllegalArgumentException when the token is {@code null} or blank
     */
    public CompletableFuture<Void> unsubscribeAsync(String token) {
        return sendAsync(unsubscribeRequest(token)).thenAccept(response -> {
            if (response.status() != 200) {
                throw new CompletionException(httpError(response));
            }
        });
    }

    private HttpRequest learnRequest(Disposition disposition, byte[] rawMime) {
        if (disposition == null) {
            throw new IllegalArgumentException("disposition is required");
        }
        if (rawMime == null) {
            throw new IllegalArgumentException("rawMime is required");
        }
        requireLearningCredentials();
        return baseRequest(URI.create(baseUrl + LEARN_PATH + disposition.wire()))
                .header("Content-Type", "message/rfc822")
                .header("Authorization", basicAuth())
                .POST(HttpRequest.BodyPublishers.ofByteArray(rawMime))
                .build();
    }

    private HttpRequest unsubscribeRequest(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("unsubscribe token is required");
        }
        URI uri = URI.create(baseUrl + UNSUBSCRIBE_PATH + encodePathSegment(token.trim()));
        return baseRequest(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("List-Unsubscribe=One-Click"))
                .build();
    }

    private HttpRequest.Builder baseRequest(URI uri) {
        return HttpRequest.newBuilder(uri)
                .timeout(requestTimeout)
                .header("Accept", "application/json");
    }

    private void requireLearningCredentials() {
        if (username == null || username.isBlank() || secret == null || secret.isEmpty()) {
            throw new IllegalStateException(
                    "credentials are required for learning (configure them with credentials(...))");
        }
    }

    private String basicAuth() {
        String token = username + ":" + secret;
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    private Response send(HttpRequest request) throws IOException {
        try {
            HttpResponse<InputStream> response =
                    http.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return read(response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("interrupted while calling " + request.method() + " " + request.uri(), e);
        } catch (IOException e) {
            throw new IOException(request.method() + " " + request.uri() + " failed: " + e, e);
        }
    }

    private CompletableFuture<Response> sendAsync(HttpRequest request) {
        return http.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(response -> {
                    try {
                        return read(response);
                    } catch (IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    private Response read(HttpResponse<InputStream> response) throws IOException {
        try (InputStream stream = response.body()) {
            return new Response(response.statusCode(), readCapped(stream, MAX_RESPONSE_BYTES));
        }
    }

    private static byte[] readCapped(InputStream stream, long maxBytes) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        long total = 0;
        int read;
        while ((read = stream.read(buffer)) != -1) {
            total += read;
            if (total > maxBytes) {
                throw new IOException("body exceeds the " + maxBytes + "-byte limit");
            }
            out.write(buffer, 0, read);
        }
        return out.toByteArray();
    }

    private static LearningResult decode(byte[] body) throws IOException {
        if (body == null || body.length == 0) {
            throw new FeedbackException(200, "empty learning response");
        }
        return FeedbackJson.MAPPER.readValue(body, LearningResult.class);
    }

    private static FeedbackException httpError(Response response) {
        String detail = null;
        byte[] body = response.body();
        if (body != null && body.length > 0) {
            try {
                JsonNode payload = FeedbackJson.MAPPER.readTree(body);
                JsonNode error = payload == null ? null : payload.get("error");
                if (error != null && error.isTextual() && !error.asText().isBlank()) {
                    detail = error.asText().trim();
                }
            } catch (IOException ignored) {
                // Fall back to a message without the service detail.
            }
        }
        return new FeedbackException(response.status(), detail);
    }

    private static String encodePathSegment(String value) {
        StringBuilder encoded = new StringBuilder(value.length());
        for (byte b : value.getBytes(StandardCharsets.UTF_8)) {
            int c = b & 0xff;
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')
                    || c == '-' || c == '_' || c == '.' || c == '~') {
                encoded.append((char) c);
            } else {
                encoded.append('%')
                        .append(Character.toUpperCase(Character.forDigit((c >> 4) & 0xf, 16)))
                        .append(Character.toUpperCase(Character.forDigit(c & 0xf, 16)));
            }
        }
        return encoded.toString();
    }

    /** A status code and bounded body captured from a response. */
    private record Response(int status, byte[] body) {
    }

    /** Builds a {@link FeedbackClient}. */
    public static final class Builder {
        private String baseUrl;
        private String username;
        private String secret;
        private HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        private Duration requestTimeout = DEFAULT_TIMEOUT;
        private long maxRequestBytes = DEFAULT_MAX_REQUEST_BYTES;

        private Builder() {
        }

        /**
         * Sets the feedback service base URL, for example {@code "https://feedback.mxraven.com"}.
         *
         * @param baseUrl the base URL
         * @return this builder
         * @throws IllegalArgumentException when {@code baseUrl} is {@code null} or blank
         */
        public Builder baseUrl(String baseUrl) {
            if (baseUrl == null || baseUrl.isBlank()) {
                throw new IllegalArgumentException("base URL must not be empty");
            }
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * Sets the submission API key used to authenticate learning requests.
         *
         * @param username the key's username
         * @param secret   the key's secret
         * @return this builder
         * @throws IllegalArgumentException when {@code username} is blank or
         *                                  {@code secret} is empty
         */
        public Builder credentials(String username, String secret) {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("username must not be empty");
            }
            if (secret == null || secret.isEmpty()) {
                throw new IllegalArgumentException("secret must not be empty");
            }
            this.username = username;
            this.secret = secret;
            return this;
        }

        /**
         * Sets the HTTP client used for requests.
         *
         * @param httpClient the HTTP client
         * @return this builder
         * @throws IllegalArgumentException when {@code httpClient} is {@code null}
         */
        public Builder httpClient(HttpClient httpClient) {
            if (httpClient == null) {
                throw new IllegalArgumentException("HTTP client must not be null");
            }
            this.httpClient = httpClient;
            return this;
        }

        /**
         * Sets the per-request timeout. Defaults to 30 seconds.
         *
         * @param requestTimeout the request timeout
         * @return this builder
         * @throws IllegalArgumentException when {@code requestTimeout} is {@code null} or
         *                                  not positive
         */
        public Builder requestTimeout(Duration requestTimeout) {
            if (requestTimeout == null || requestTimeout.isZero() || requestTimeout.isNegative()) {
                throw new IllegalArgumentException("request timeout must be positive");
            }
            this.requestTimeout = requestTimeout;
            return this;
        }

        /**
         * Sets the maximum number of bytes read from a raw-message input stream.
         * Defaults to {@link FeedbackClient#DEFAULT_MAX_REQUEST_BYTES}.
         *
         * @param maxRequestBytes the limit in bytes; must be positive
         * @return this builder
         * @throws IllegalArgumentException when {@code maxRequestBytes} is not positive
         */
        public Builder maxRequestBytes(long maxRequestBytes) {
            if (maxRequestBytes <= 0) {
                throw new IllegalArgumentException("maxRequestBytes must be positive");
            }
            this.maxRequestBytes = maxRequestBytes;
            return this;
        }

        /**
         * Builds the client. A base URL is required.
         *
         * @return the configured client
         * @throws IllegalStateException when the base URL has not been set
         */
        public FeedbackClient build() {
            if (baseUrl == null || baseUrl.isBlank()) {
                throw new IllegalStateException("base URL is required (call baseUrl(...))");
            }
            return new FeedbackClient(this);
        }
    }
}
