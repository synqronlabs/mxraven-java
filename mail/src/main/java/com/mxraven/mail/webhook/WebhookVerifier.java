package com.mxraven.mail.webhook;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Verifies the HMAC-SHA256 signature of an mxRaven endpoint call.
 *
 * <p>Routing scripts call tenant-registered endpoints with {@code mx.http}, and
 * mxRaven signs each call with the endpoint's signing secret. The signature
 * scheme is the one mxRaven used for webhook deliveries, so this package is
 * still published as {@code mail/webhook}. The script decides the request body,
 * so verification stops at the signature: parse the body your endpoint defines.
 *
 * <pre>{@code
 * WebhookVerifier verifier = WebhookVerifier.builder()
 *         .secret(signingSecret)
 *         .build();
 *
 * verifier.verify(method, requestUri, headers, body);
 * }</pre>
 *
 * <p>Pass the public, absolute request URL, including any proxy-forwarded
 * scheme and host: the signature covers the host the sender called.
 *
 * <p>The signing secret is shown only once, when the endpoint is created or its
 * secret is rotated. It is used as literal key bytes; do not base64-decode it.
 *
 * <p>The {@value #HEADER_LISTENER} and {@value #HEADER_REVISION} headers
 * identify the script call but are not part of the signed canonical request.
 * Treat them as context, not as an authorization decision.
 *
 * <p>An endpoint configured with a stored credential is called with that
 * credential header and no {@code X-MxRaven-*} signature headers. Those calls
 * are not verifiable with this class; validate the credential with your own
 * authentication middleware.
 *
 * <p>A configured {@code WebhookVerifier} is immutable and safe for concurrent
 * use.
 */
public final class WebhookVerifier {
    /** Carries the delivery ID, which is the delivery task ID. */
    public static final String HEADER_WEBHOOK_ID = "X-MxRaven-Webhook-ID";
    /** Carries the Unix signing time in seconds. */
    public static final String HEADER_TIMESTAMP = "X-MxRaven-Timestamp";
    /** Carries the {@code sha256=<hex>} HMAC. */
    public static final String HEADER_SIGNATURE = "X-MxRaven-Signature";
    /** Carries the signing key ID. */
    public static final String HEADER_SIGNATURE_KID = "X-MxRaven-Signature-Kid";
    /** Identifies the listener whose routing script made the call; not signed. */
    public static final String HEADER_LISTENER = "X-MxRaven-Listener";
    /** Identifies the script revision that made the call; not signed. */
    public static final String HEADER_REVISION = "X-MxRaven-Revision";

    private static final Duration DEFAULT_TOLERANCE = Duration.ofMinutes(5);
    private static final long DEFAULT_MAX_BODY_BYTES = 1L << 20;
    private static final String SIGNATURE_SCHEME = "sha256=";

    private final String secret;
    private final Map<String, String> keys;
    private final Duration tolerance;
    private final long maxBodyBytes;
    private final Clock clock;

    private WebhookVerifier(Builder builder) {
        this.secret = builder.secret;
        this.keys = Map.copyOf(builder.keys);
        this.tolerance = builder.tolerance;
        this.maxBodyBytes = builder.maxBodyBytes;
        this.clock = builder.clock;
    }

    /**
     * Creates a verifier builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Verifies the signature of a request.
     *
     * <p>The signature is HMAC-SHA256 over a canonical string whose fields are
     * joined by newlines: the timestamp, the webhook ID, the method, the
     * lowercase host (including the port when present), the escaped path plus
     * raw query (or {@code "/"}), and the lowercase hex SHA-256 of the raw body.
     *
     * @param method  the HTTP method, for example {@code "POST"}
     * @param uri     the full request URI, including scheme, host, path, and query
     * @param headers the request headers; looked up case-insensitively
     * @param body    the exact raw request body bytes
     * @throws InvalidSignatureException when the signature does not match
     * @throws PayloadTooLargeException   when the body exceeds the configured limit
     * @throws WebhookException           when required headers are missing or the
     *                                    request is otherwise unusable
     */
    public void verify(String method, URI uri, Map<String, String> headers, byte[] body) {
        if (body == null) {
            throw new WebhookException("request body is missing");
        }
        if (body.length > maxBodyBytes) {
            throw new PayloadTooLargeException("request body exceeds " + maxBodyBytes + " bytes");
        }
        if (uri == null) {
            throw new WebhookException("request URL is missing");
        }

        Map<String, String> lookup = normalize(headers);
        String webhookId = header(lookup, HEADER_WEBHOOK_ID);
        if (webhookId.isEmpty()) {
            throw new WebhookException("missing webhook ID header");
        }
        String timestamp = header(lookup, HEADER_TIMESTAMP);
        if (timestamp.isEmpty()) {
            throw new WebhookException("missing timestamp header");
        }
        String signature = header(lookup, HEADER_SIGNATURE);
        if (signature.isEmpty()) {
            throw new WebhookException("missing signature header");
        }

        if (!tolerance.isZero()) {
            long seconds;
            try {
                seconds = Long.parseLong(timestamp);
            } catch (NumberFormatException e) {
                throw new WebhookException("invalid timestamp \"" + timestamp + "\"");
            }
            long skew = Math.abs(clock.instant().getEpochSecond() - seconds);
            if (Duration.ofSeconds(skew).compareTo(tolerance) > 0) {
                throw new WebhookException("timestamp is outside the accepted clock skew");
            }
        }

        String key = secretFor(header(lookup, HEADER_SIGNATURE_KID));
        if (!signature.startsWith(SIGNATURE_SCHEME)) {
            throw new WebhookException("unsupported signature algorithm");
        }
        byte[] provided;
        try {
            provided = HexFormat.of().parseHex(signature.substring(SIGNATURE_SCHEME.length()));
        } catch (IllegalArgumentException e) {
            throw new WebhookException("malformed signature");
        }

        byte[] expected = hmac(key, canonical(method, uri, timestamp, webhookId, body));
        if (!MessageDigest.isEqual(provided, expected)) {
            throw new InvalidSignatureException("method=" + (method == null ? "" : method)
                    + " host=" + host(uri) + " target=" + requestTarget(uri));
        }
    }

    private String secretFor(String kid) {
        if (!keys.isEmpty()) {
            if (kid.isEmpty()) {
                throw new WebhookException("missing signature key ID");
            }
            String key = keys.get(kid);
            if (key == null) {
                throw new WebhookException("unknown signature key ID \"" + kid + "\"");
            }
            return key;
        }
        return secret;
    }

    private static String canonical(String method, URI uri, String timestamp, String webhookId, byte[] body) {
        return String.join("\n",
                timestamp,
                webhookId,
                method == null ? "" : method,
                host(uri),
                requestTarget(uri),
                HexFormat.of().formatHex(sha256(body)));
    }

    private static String host(URI uri) {
        String authority = uri.getRawAuthority();
        if (authority != null && !authority.isEmpty()) {
            return authority.toLowerCase(Locale.ROOT);
        }
        String host = uri.getHost();
        return host == null ? "" : host.toLowerCase(Locale.ROOT);
    }

    private static String requestTarget(URI uri) {
        String path = uri.getRawPath();
        if (path == null || path.isEmpty()) {
            path = "/";
        }
        String query = uri.getRawQuery();
        return query == null || query.isEmpty() ? path : path + "?" + query;
    }

    private static byte[] hmac(String secret, String message) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new WebhookException("compute webhook signature", e);
        }
    }

    private static byte[] sha256(byte[] data) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new WebhookException("SHA-256 is not available", e);
        }
    }

    private static Map<String, String> normalize(Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return Map.of();
        }
        Map<String, String> normalized = new HashMap<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            normalized.put(entry.getKey().toLowerCase(Locale.ROOT), entry.getValue() == null ? "" : entry.getValue());
        }
        return normalized;
    }

    private static String header(Map<String, String> headers, String name) {
        return headers.getOrDefault(name.toLowerCase(Locale.ROOT), "").trim();
    }

    /** Builds a {@link WebhookVerifier}. */
    public static final class Builder {
        private String secret;
        private final Map<String, String> keys = new LinkedHashMap<>();
        private Duration tolerance = DEFAULT_TOLERANCE;
        private long maxBodyBytes = DEFAULT_MAX_BODY_BYTES;
        private Clock clock = Clock.systemUTC();

        private Builder() {
        }

        /**
         * Sets the signing secret used to verify requests. The secret is used as
         * literal key bytes; do not decode it. It is used when no key-specific
         * secret matches.
         *
         * @param secret the signing secret
         * @return this builder
         * @throws IllegalArgumentException when {@code secret} is {@code null} or empty
         */
        public Builder secret(String secret) {
            if (secret == null || secret.isEmpty()) {
                throw new IllegalArgumentException("signing secret must not be empty");
            }
            this.secret = secret;
            return this;
        }

        /**
         * Registers a signing secret for a specific signature key ID. Use it to
         * accept calls signed by more than one key, for example during rotation.
         *
         * @param kid    the signature key ID
         * @param secret the signing secret for the key
         * @return this builder
         * @throws IllegalArgumentException when {@code kid} is blank or {@code secret} is empty
         */
        public Builder key(String kid, String secret) {
            if (kid == null || kid.isBlank()) {
                throw new IllegalArgumentException("signing key ID must not be empty");
            }
            if (secret == null || secret.isEmpty()) {
                throw new IllegalArgumentException("signing secret must not be empty");
            }
            keys.put(kid, secret);
            return this;
        }

        /**
         * Sets the maximum accepted difference between the request timestamp and
         * the current time. {@link Duration#ZERO} disables timestamp checking.
         * Defaults to 5 minutes.
         *
         * @param tolerance the accepted clock skew
         * @return this builder
         * @throws IllegalArgumentException when {@code tolerance} is {@code null} or negative
         */
        public Builder tolerance(Duration tolerance) {
            if (tolerance == null) {
                throw new IllegalArgumentException("tolerance must not be null");
            }
            if (tolerance.isNegative()) {
                throw new IllegalArgumentException("tolerance must not be negative");
            }
            this.tolerance = tolerance;
            return this;
        }

        /**
         * Sets the maximum body size accepted by verification. Defaults to 1 MiB.
         *
         * @param maxBodyBytes the maximum accepted body size in bytes
         * @return this builder
         * @throws IllegalArgumentException when {@code maxBodyBytes} is not positive
         */
        public Builder maxBodyBytes(long maxBodyBytes) {
            if (maxBodyBytes <= 0) {
                throw new IllegalArgumentException("maximum body size must be positive");
            }
            this.maxBodyBytes = maxBodyBytes;
            return this;
        }

        /**
         * Sets the clock used for timestamp checking. Defaults to UTC.
         *
         * @param clock the clock
         * @return this builder
         * @throws IllegalArgumentException when {@code clock} is {@code null}
         */
        public Builder clock(Clock clock) {
            if (clock == null) {
                throw new IllegalArgumentException("clock must not be null");
            }
            this.clock = clock;
            return this;
        }

        /**
         * Builds the verifier. At least one secret is required.
         *
         * @return the configured verifier
         * @throws IllegalStateException when neither a secret nor a key has been set
         */
        public WebhookVerifier build() {
            if ((secret == null || secret.isEmpty()) && keys.isEmpty()) {
                throw new IllegalStateException("a signing secret is required (call secret(...) or key(...))");
            }
            return new WebhookVerifier(this);
        }
    }
}
