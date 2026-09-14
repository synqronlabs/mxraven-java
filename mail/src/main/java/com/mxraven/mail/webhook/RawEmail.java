package com.mxraven.mail.webhook;

import com.mxraven.mail.mime.ParsedEmail;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;

/**
 * Time-limited access to the raw RFC 822 message of a {@code DELIVER_WEBHOOK}
 * delivery.
 *
 * <p>Download the content with {@link #fetch()}, which verifies the downloaded
 * bytes against the declared size and SHA-256 digest. The embedded
 * {@link #accessToken() access token} is a secret: treat it as one and do not
 * log it. It is short-lived (15 minutes by default), so fetch promptly after
 * receiving the webhook.
 *
 * @param url          the message download URL
 * @param tokenType    the authorization scheme, normally {@code "Bearer"}
 * @param accessToken  the bearer token for the download URL
 * @param expiresAtUtc the Unix time, in seconds, when the token expires
 * @param sizeBytes    the raw message size
 * @param sha256Hex    the lowercase hex SHA-256 of the raw message bytes
 * @param contentType  the parsed message media type
 */
public record RawEmail(
        String url,
        String tokenType,
        String accessToken,
        long expiresAtUtc,
        long sizeBytes,
        String sha256Hex,
        String contentType) {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(60);
    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(30);
    private static final long MAX_BYTES = 64L << 20;

    /** Downloads the message with a default client and a 60-second timeout. */
    public byte[] fetch() throws IOException {
        return fetch(null);
    }

    /** Downloads the message and parses it into a {@link ParsedEmail}. */
    public ParsedEmail parse() throws IOException {
        return ParsedEmail.parse(fetch());
    }

    /** Downloads with {@code client} and parses it into a {@link ParsedEmail}. */
    public ParsedEmail parse(HttpClient client) throws IOException {
        return ParsedEmail.parse(fetch(client));
    }

    /**
     * Downloads the message, verifying the declared size and SHA-256 digest.
     * A {@code null} client uses a default client with a 60-second timeout.
     */
    public byte[] fetch(HttpClient client) throws IOException {
        if (url == null || url.isBlank()) {
            throw new WebhookException("raw email URL is empty");
        }
        if (accessToken == null || accessToken.isBlank()) {
            throw new WebhookException("raw email access token is empty");
        }
        HttpClient http = client != null
                ? client
                : HttpClient.newBuilder().connectTimeout(DEFAULT_CONNECT_TIMEOUT).build();
        String scheme = tokenType == null || tokenType.isBlank() ? "Bearer" : tokenType.trim();

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(DEFAULT_TIMEOUT)
                .header("Authorization", scheme + " " + accessToken)
                .GET()
                .build();

        HttpResponse<byte[]> response = send(http, request);
        if (response.statusCode() != 200) {
            throw new WebhookException("fetch raw email: unexpected status " + response.statusCode());
        }

        byte[] body = response.body();
        long limit = sizeBytes > 0 ? sizeBytes + 1 : MAX_BYTES;
        if (body.length > limit) {
            throw new WebhookException("raw email exceeds " + limit + " bytes");
        }
        if (sizeBytes > 0 && body.length != sizeBytes) {
            throw new WebhookException("raw email size = " + body.length + " bytes, want " + sizeBytes);
        }
        if (sha256Hex != null && !sha256Hex.isBlank() && !sha256Hex(body).equalsIgnoreCase(sha256Hex.trim())) {
            throw new WebhookException("raw email SHA-256 mismatch");
        }
        return body;
    }

    private static HttpResponse<byte[]> send(HttpClient http, HttpRequest request) throws IOException {
        try {
            return http.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("interrupted while fetching the raw email", e);
        }
    }

    private static String sha256Hex(byte[] data) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(data));
        } catch (NoSuchAlgorithmException e) {
            throw new WebhookException("SHA-256 is not available", e);
        }
    }
}
