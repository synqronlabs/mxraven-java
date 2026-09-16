package com.mxraven.mail.webhook;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import com.mxraven.mail.mime.ParsedEmail;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Time-limited access to the raw RFC 822 message of a {@code DELIVER_WEBHOOK}
 * delivery.
 *
 * <p>Download the content with {@link #fetch()}, which verifies the downloaded
 * bytes against the declared size and SHA-256 digest. The embedded
 * {@link #accessToken() access token} is a secret: treat it as one and do not
 * log it. It is short-lived (15 minutes by default), so fetch promptly after
 * receiving the webhook.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RawEmail {
    private final String url;
    private final String tokenType;
    private final String accessToken;
    private final long expiresAtUtc;
    private final long sizeBytes;
    private final String sha256Hex;
    private final String contentType;

    /** the message download URL */
    public String url() {
        return url;
    }

    /** the authorization scheme, normally {@code "Bearer"} */
    public String tokenType() {
        return tokenType;
    }

    /** the bearer token for the download URL */
    public String accessToken() {
        return accessToken;
    }

    /** the Unix time, in seconds, when the token expires */
    public long expiresAtUtc() {
        return expiresAtUtc;
    }

    /** the raw message size */
    public long sizeBytes() {
        return sizeBytes;
    }

    /** the lowercase hex SHA-256 of the raw message bytes */
    public String sha256Hex() {
        return sha256Hex;
    }

    /** the parsed message media type */
    public String contentType() {
        return contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RawEmail that = (RawEmail) o;
        return Objects.equals(this.url, that.url)
                && Objects.equals(this.tokenType, that.tokenType)
                && Objects.equals(this.accessToken, that.accessToken)
                && this.expiresAtUtc == that.expiresAtUtc
                && this.sizeBytes == that.sizeBytes
                && Objects.equals(this.sha256Hex, that.sha256Hex)
                && Objects.equals(this.contentType, that.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.url, this.tokenType, this.accessToken, this.expiresAtUtc, this.sizeBytes, this.sha256Hex, this.contentType);
    }

    @Override
    public String toString() {
        return "RawEmail[" + "url=" + this.url + ", " + "tokenType=" + this.tokenType + ", " + "accessToken=" + this.accessToken + ", " + "expiresAtUtc=" + this.expiresAtUtc + ", " + "sizeBytes=" + this.sizeBytes + ", " + "sha256Hex=" + this.sha256Hex + ", " + "contentType=" + this.contentType + "]";
    }

    /**
     * Creates a new RawEmail.
     *
     * @param url the message download URL
     * @param tokenType the authorization scheme, normally {@code "Bearer"}
     * @param accessToken the bearer token for the download URL
     * @param expiresAtUtc the Unix time, in seconds, when the token expires
     * @param sizeBytes the raw message size
     * @param sha256Hex the lowercase hex SHA-256 of the raw message bytes
     * @param contentType the parsed message media type
     */
    @JsonCreator
    public RawEmail(String url, String tokenType, String accessToken, long expiresAtUtc, long sizeBytes, String sha256Hex, String contentType) {
        this.url = url;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresAtUtc = expiresAtUtc;
        this.sizeBytes = sizeBytes;
        this.sha256Hex = sha256Hex;
        this.contentType = contentType;
    }

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(60);
    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(30);
    private static final long MAX_BYTES = 64L << 20;

    /**
     * Downloads the message with a default client and a 60-second timeout.
     *
     * @return the raw message bytes
     * @throws IOException      when the download fails
     * @throws WebhookException when the URL or access token is empty, the response
     *                          is not {@code 200}, or the size or digest does not match
     */
    public byte[] fetch() throws IOException {
        return fetch(null);
    }

    /**
     * Downloads the message and parses it into a {@link ParsedEmail}.
     *
     * @return the parsed message
     * @throws IOException      when the download fails
     * @throws WebhookException when the fetch fails
     */
    public ParsedEmail parse() throws IOException {
        return ParsedEmail.parse(fetch());
    }

    /**
     * Downloads with {@code client} and parses it into a {@link ParsedEmail}.
     *
     * @param client the HTTP client to download with, or {@code null} for a default client
     * @return the parsed message
     * @throws IOException      when the download fails
     * @throws WebhookException when the fetch fails
     */
    public ParsedEmail parse(OkHttpClient client) throws IOException {
        return ParsedEmail.parse(fetch(client));
    }

    /**
     * Downloads the message, verifying the declared size and SHA-256 digest.
     * A {@code null} client uses a default client with a 60-second timeout.
     *
     * @param client the HTTP client to download with, or {@code null} for a default client
     * @return the raw message bytes
     * @throws IOException      when the download fails
     * @throws WebhookException when the URL or access token is empty, the response
     *                          is not {@code 200}, or the size or digest does not match
     */
    public byte[] fetch(OkHttpClient client) throws IOException {
        if (url == null || Java8.isBlank(url)) {
            throw new WebhookException("raw email URL is empty");
        }
        if (accessToken == null || Java8.isBlank(accessToken)) {
            throw new WebhookException("raw email access token is empty");
        }
        OkHttpClient http = client != null
                ? client
                : new OkHttpClient.Builder()
                        .connectTimeout(DEFAULT_CONNECT_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
                        .callTimeout(DEFAULT_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
                        .build();
        String scheme = tokenType == null || Java8.isBlank(tokenType) ? "Bearer" : tokenType.trim();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", scheme + " " + accessToken)
                .get()
                .build();

        okhttp3.Response response = send(http, request);
        try {
            if (response.code() != 200) {
                throw new WebhookException("fetch raw email: unexpected status " + response.code());
            }
            byte[] body = response.body() == null ? new byte[0] : response.body().bytes();
            long limit = sizeBytes > 0 ? sizeBytes + 1 : MAX_BYTES;
            if (body.length > limit) {
                throw new WebhookException("raw email exceeds " + limit + " bytes");
            }
            if (sizeBytes > 0 && body.length != sizeBytes) {
                throw new WebhookException("raw email size = " + body.length + " bytes, want " + sizeBytes);
            }
            if (sha256Hex != null && !Java8.isBlank(sha256Hex)
                    && !sha256Hex(body).equalsIgnoreCase(sha256Hex.trim())) {
                throw new WebhookException("raw email SHA-256 mismatch");
            }
            return body;
        } finally {
            response.close();
        }
    }

    private static okhttp3.Response send(OkHttpClient http, Request request) throws IOException {
        return http.newCall(request).execute();
    }

    private static String sha256Hex(byte[] data) {
        try {
            return Java8.toHex(MessageDigest.getInstance("SHA-256").digest(data));
        } catch (NoSuchAlgorithmException e) {
            throw new WebhookException("SHA-256 is not available", e);
        }
    }
}
