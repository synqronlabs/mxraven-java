package com.mxraven.mail.feedback;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Client for the mxRaven feedback service.
 *
 * <p>Tenants can teach the mxRaven spam filter by submitting messages that were
 * misclassified. A message is matched to its stored evidence by hash, so the
 * exact raw RFC 822 bytes mxRaven processed must be submitted.
 *
 * <pre>
 * FeedbackClient feedback = FeedbackClient.builder()
 *         .baseUrl("https://feedback.mxraven.com")
 *         .credentials("mxr_tx_ab12cd34ef56", apiKeySecret)
 *         .build();
 *
 * LearningResult result = feedback.learnSpam(rawMessage);
 * System.out.println(result.disposition() + " matched " + result.matchedHashKind());
 * </pre>
 *
 * <p>The credentials are the submission API key: the username is the key's
 * username and the secret is the key's secret. Only the learning methods require
 * credentials; {@link #unsubscribe(String)} is unauthenticated.
 *
 * <p>A configured {@code FeedbackClient} is safe for concurrent use. The
 * {@code *Async} methods return a {@link CompletableFuture}; cancelling it (with
 * {@link CompletableFuture#cancel(boolean)}) cancels that request without
 * affecting others.
 */
public final class FeedbackClient {
    /** Default request timeout for non-streaming calls. */
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    /** Maximum number of response bytes accepted from the service. */
    private static final int MAX_RESPONSE_BYTES = 64 * 1024;

    private static final String LEARN_PATH = "/v1/feedback/learn/";
    private static final String UNSUBSCRIBE_PATH = "/v1/feedback/unsubscribe/";

    private final String baseUrl;
    private final String username;
    private final String secret;
    private final OkHttpClient http;
    private final Duration requestTimeout;

    private FeedbackClient(Builder builder) {
        this.baseUrl = builder.baseUrl.replaceAll("/+$", "");
        this.username = builder.username;
        this.secret = builder.secret;
        this.http = builder.httpClient.newBuilder()
                .callTimeout(builder.requestTimeout.toMillis(), TimeUnit.MILLISECONDS)
                .build();
        this.requestTimeout = builder.requestTimeout;
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
        return execute(learnRequest(disposition, rawMime));
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
        return learn(disposition, Java8.readAllBytes(rawMime));
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
        return executeAsync(learnRequest(disposition, rawMime), new ResponseMapper<LearningResult>() {
            @Override
            public LearningResult map(byte[] body) throws IOException {
                return decode(body);
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
        execute(unsubscribeRequest(token));
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
        return executeAsync(unsubscribeRequest(token), new ResponseMapper<Void>() {
            @Override
            public Void map(byte[] body) {
                return null;
            }
        });
    }

    private Request learnRequest(Disposition disposition, byte[] rawMime) {
        if (disposition == null) {
            throw new IllegalArgumentException("disposition is required");
        }
        if (rawMime == null) {
            throw new IllegalArgumentException("rawMime is required");
        }
        requireLearningCredentials();
        return baseRequest(baseUrl + LEARN_PATH + disposition.wire())
                .header("Content-Type", "message/rfc822")
                .header("Authorization", basicAuth())
                .post(RequestBody.create(rawMime, MediaType.get("message/rfc822")))
                .build();
    }

    private Request unsubscribeRequest(String token) {
        if (token == null || Java8.isBlank(token)) {
            throw new IllegalArgumentException("unsubscribe token is required");
        }
        return baseRequest(baseUrl + UNSUBSCRIBE_PATH + encodePathSegment(token.trim()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(RequestBody.create("List-Unsubscribe=One-Click".getBytes(StandardCharsets.UTF_8),
                        MediaType.get("application/x-www-form-urlencoded")))
                .build();
    }

    private Request.Builder baseRequest(String url) {
        return new Request.Builder()
                .url(url)
                .header("Accept", "application/json");
    }

    private LearningResult execute(Request request) throws IOException {
        okhttp3.Response response = send(request);
        try {
            int code = response.code();
            byte[] body = readBody(response.body());
            if (code != 200) {
                throw httpError(code, body);
            }
            return decode(body);
        } finally {
            response.close();
        }
    }

    private <T> CompletableFuture<T> executeAsync(Request request, final ResponseMapper<T> mapper) {
        final Call call = http.newCall(request);
        final CompletableFuture<T> future = new CancellableFuture<T>(call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call failedCall, IOException e) {
                future.completeExceptionally(new IOException(
                        request.method() + " " + request.url() + " failed: " + e, e));
            }

            @Override
            public void onResponse(Call successfulCall, okhttp3.Response response) {
                try {
                    int code = response.code();
                    byte[] body = readBody(response.body());
                    if (code != 200) {
                        future.completeExceptionally(httpError(code, body));
                    } else {
                        future.complete(mapper.map(body));
                    }
                } catch (Throwable t) {
                    future.completeExceptionally(t);
                } finally {
                    response.close();
                }
            }
        });
        return future;
    }

    /** A future whose cancellation also cancels the underlying HTTP call. */
    private static final class CancellableFuture<T> extends CompletableFuture<T> {
        private final Call call;

        CancellableFuture(Call call) {
            this.call = call;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            call.cancel();
            return super.cancel(mayInterruptIfRunning);
        }
    }

    /** Maps a successful response body to a result. */
    private interface ResponseMapper<T> {
        T map(byte[] body) throws IOException;
    }

    private void requireLearningCredentials() {
        if (username == null || Java8.isBlank(username) || secret == null || secret.isEmpty()) {
            throw new IllegalStateException(
                    "credentials are required for learning (configure them with credentials(...))");
        }
    }

    private String basicAuth() {
        String token = username + ":" + secret;
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    private okhttp3.Response send(Request request) throws IOException {
        try {
            return http.newCall(request).execute();
        } catch (IOException e) {
            throw new IOException(request.method() + " " + request.url() + " failed: " + e, e);
        }
    }

    private static byte[] readBody(ResponseBody body) throws IOException {
        if (body == null) {
            return new byte[0];
        }
        InputStream stream = body.byteStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int total = 0;
        int read;
        while ((read = stream.read(buffer)) != -1) {
            total += read;
            if (total > MAX_RESPONSE_BYTES) {
                throw new IOException("response exceeds the " + MAX_RESPONSE_BYTES + "-byte limit");
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

    private static FeedbackException httpError(int status, byte[] body) {
        String detail = null;
        if (body != null && body.length > 0) {
            try {
                JsonNode payload = FeedbackJson.MAPPER.readTree(body);
                JsonNode error = payload == null ? null : payload.get("error");
                if (error != null && error.isTextual() && !Java8.isBlank(error.asText())) {
                    detail = error.asText().trim();
                }
            } catch (IOException ignored) {
                // Fall back to a message without the service detail.
            }
        }
        return new FeedbackException(status, detail);
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
        private OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        private Duration requestTimeout = DEFAULT_TIMEOUT;

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
            if (baseUrl == null || Java8.isBlank(baseUrl)) {
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
            if (username == null || Java8.isBlank(username)) {
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
        public Builder httpClient(OkHttpClient httpClient) {
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
         * Builds the client. A base URL is required.
         *
         * @return the configured client
         * @throws IllegalStateException when the base URL has not been set
         */
        public FeedbackClient build() {
            if (baseUrl == null || Java8.isBlank(baseUrl)) {
                throw new IllegalStateException("base URL is required (call baseUrl(...))");
            }
            return new FeedbackClient(this);
        }
    }
}
