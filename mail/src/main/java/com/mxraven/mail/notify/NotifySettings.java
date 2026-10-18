package com.mxraven.mail.notify;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * The validated options a transport needs. Validation happens once, when the
 * client connects.
 */
record NotifySettings(
        String url,
        String username,
        String secret,
        String listener,
        String consumer,
        Duration connectTimeout,
        SSLContext sslContext,
        Consumer<NotificationException> onError) {

    /** Deadline for establishing the connection, in seconds. */
    static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(10);

    /** Validates the public options and applies defaults. */
    static NotifySettings normalize(NotificationClientOptions options) {
        if (options == null) {
            throw new NotificationException("notify: options are required");
        }

        String url = options.url() == null || options.url().isBlank()
                ? NotificationClient.DEFAULT_URL
                : options.url().trim();
        if (url.isEmpty()) {
            throw new NotificationException("notify: URL is required");
        }
        try {
            URI parsed = new URI(url);
            String scheme = parsed.getScheme() == null ? "" : parsed.getScheme().toLowerCase(Locale.ROOT);
            if (!scheme.equals("ws") && !scheme.equals("wss")) {
                throw new NotificationException(
                        "notify: URL \"" + url + "\" must use the ws or wss scheme");
            }
            if (parsed.getHost() == null || parsed.getHost().isBlank()) {
                throw new NotificationException("notify: URL \"" + url + "\" must include a host");
            }
            // The NATS client defaults a missing port to 4222, the plain TCP
            // port, so fill in the WebSocket defaults by scheme.
            if (parsed.getPort() == -1) {
                int port = scheme.equals("wss") ? 443 : 80;
                url = new URI(scheme, parsed.getUserInfo(), parsed.getHost(), port,
                        parsed.getPath(), parsed.getQuery(), parsed.getFragment()).toString();
            }
        } catch (URISyntaxException e) {
            throw new NotificationException("notify: invalid URL \"" + url + "\"", e);
        }

        String username = options.username() == null ? "" : options.username().trim();
        if (username.isEmpty()) {
            throw new NotificationException("notify: username is required");
        }
        String secret = options.secret() == null ? "" : options.secret();
        if (secret.isEmpty()) {
            throw new NotificationException("notify: secret is required");
        }

        String listener = options.listener() == null ? "" : options.listener().trim();
        if (listener.isEmpty()) {
            throw new NotificationException("notify: listener ID is required");
        }
        if (!isValidToken(listener)) {
            throw new NotificationException("notify: listener ID \"" + listener
                    + "\" must not contain '.', '*', '>', '/', '\\', or whitespace");
        }

        String consumer = options.consumer() == null ? "notify-" + listener : options.consumer().trim();
        if (!isValidToken(consumer)) {
            throw new NotificationException("notify: consumer name \"" + consumer
                    + "\" must not contain '.', '*', '>', '/', '\\', or whitespace");
        }

        Duration connectTimeout = options.connectTimeout() == null
                ? DEFAULT_CONNECT_TIMEOUT
                : options.connectTimeout();
        if (connectTimeout.isZero() || connectTimeout.isNegative()) {
            throw new NotificationException("notify: invalid connect timeout " + connectTimeout.toMillis());
        }

        return new NotifySettings(url, username, secret, listener, consumer, connectTimeout,
                options.sslContext(), options.onError());
    }

    /** Reports whether a value is safe as a NATS subject token and consumer name. */
    static boolean isValidToken(String value) {
        if (value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '.' || c == '*' || c == '>' || c == '/' || c == '\\' || Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }
}
