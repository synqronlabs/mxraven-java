package com.mxraven.mail.feedback;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

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
 * <p>A configured {@code FeedbackClient} is safe for concurrent use.
 */
public final class FeedbackClient {
    /** Default request timeout for non-streaming calls. */
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    private static final String LEARN_PATH = "/v1/feedback/learn/";
    private static final String UNSUBSCRIBE_PATH = "/v1/feedback/unsubscribe/";

    private final String baseUrl;
    private final String username;
    private final String secret;
    private final HttpClient http;
    private final Duration requestTimeout;

    private FeedbackClient(Builder builder) {
        this.baseUrl = builder.baseUrl.replaceAll("/+$", "");
        this.username = builder.username;
        this.secret = builder.secret;
        this.http = builder.httpClient;
        this.requestTimeout = builder.requestTimeout;
    }

    /** Creates a feedback client builder. */
    public static Builder builder() {
        return new Builder();
    }

    /** The configured feedback service base URL. */
    public String baseUrl() {
        return baseUrl;
    }

    /** Teaches the spam filter that {@code rawMime} is spam. */
    public LearningResult learnSpam(byte[] rawMime) throws IOException {
        return learn(Disposition.SPAM, rawMime);
    }

    /** Teaches the spam filter that {@code rawMime} is not spam. */
    public LearningResult learnHam(byte[] rawMime) throws IOException {
        return learn(Disposition.HAM, rawMime);
    }

    /** Teaches the spam filter that {@code rawMime} is spam. */
    public LearningResult learnSpam(InputStream rawMime) throws IOException {
        return learn(Disposition.SPAM, rawMime);
    }

    /** Teaches the spam filter that {@code rawMime} is not spam. */
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
     */
    public LearningResult learn(Disposition disposition, byte[] rawMime) throws IOException {
        if (disposition == null) {
            throw new IllegalArgumentException("disposition is required");
        }
        if (rawMime == null) {
            throw new IllegalArgumentException("rawMime is required");
        }
        requireLearningCredentials();

        HttpRequest request = baseRequest(URI.create(baseUrl + LEARN_PATH + disposition.wire()))
                .header("Content-Type", "message/rfc822")
                .header("Authorization", basicAuth())
                .POST(HttpRequest.BodyPublishers.ofByteArray(rawMime))
                .build();

        HttpResponse<byte[]> response = send(request);
        if (response.statusCode() != 200) {
            throw httpError(response);
        }
        return decode(response.body());
    }

    /** Submits one training example read fully from {@code rawMime}. */
    public LearningResult learn(Disposition disposition, InputStream rawMime) throws IOException {
        if (rawMime == null) {
            throw new IllegalArgumentException("rawMime is required");
        }
        return learn(disposition, rawMime.readAllBytes());
    }

    /**
     * Performs an RFC 8058 one-click unsubscribe for a token. This is the
     * operation a recipient mail client performs against the
     * {@code List-Unsubscribe} URL; applications rarely call it directly.
     */
    public void unsubscribe(String token) throws IOException {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("unsubscribe token is required");
        }
        URI uri = URI.create(baseUrl + UNSUBSCRIBE_PATH + encodePathSegment(token.trim()));
        HttpRequest request = baseRequest(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("List-Unsubscribe=One-Click"))
                .build();

        HttpResponse<byte[]> response = send(request);
        if (response.statusCode() != 200) {
            throw httpError(response);
        }
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

    private HttpResponse<byte[]> send(HttpRequest request) throws IOException {
        try {
            return http.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("interrupted while calling " + request.method() + " " + request.uri(), e);
        } catch (IOException e) {
            throw new IOException(request.method() + " " + request.uri() + " failed: " + e, e);
        }
    }

    private static LearningResult decode(byte[] body) throws IOException {
        if (body == null || body.length == 0) {
            throw new FeedbackException(200, "empty learning response");
        }
        return FeedbackJson.MAPPER.readValue(body, LearningResult.class);
    }

    private static FeedbackException httpError(HttpResponse<byte[]> response) {
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
        return new FeedbackException(response.statusCode(), detail);
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

    /** Builds a {@link FeedbackClient}. */
    public static final class Builder {
        private String baseUrl;
        private String username;
        private String secret;
        private HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        private Duration requestTimeout = DEFAULT_TIMEOUT;

        private Builder() {
        }

        /** Sets the feedback service base URL, for example {@code "https://feedback.mxraven.com"}. */
        public Builder baseUrl(String baseUrl) {
            if (baseUrl == null || baseUrl.isBlank()) {
                throw new IllegalArgumentException("base URL must not be empty");
            }
            this.baseUrl = baseUrl;
            return this;
        }

        /** Sets the submission API key used to authenticate learning requests. */
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

        /** Sets the HTTP client used for requests. */
        public Builder httpClient(HttpClient httpClient) {
            if (httpClient == null) {
                throw new IllegalArgumentException("HTTP client must not be null");
            }
            this.httpClient = httpClient;
            return this;
        }

        /** Sets the per-request timeout. Defaults to 30 seconds. */
        public Builder requestTimeout(Duration requestTimeout) {
            if (requestTimeout == null || requestTimeout.isZero() || requestTimeout.isNegative()) {
                throw new IllegalArgumentException("request timeout must be positive");
            }
            this.requestTimeout = requestTimeout;
            return this;
        }

        /** Builds the client. A base URL is required. */
        public FeedbackClient build() {
            if (baseUrl == null || baseUrl.isBlank()) {
                throw new IllegalStateException("base URL is required (call baseUrl(...))");
            }
            return new FeedbackClient(this);
        }
    }
}
