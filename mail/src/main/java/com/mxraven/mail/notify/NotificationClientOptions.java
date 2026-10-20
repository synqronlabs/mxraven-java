package com.mxraven.mail.notify;

import javax.net.ssl.SSLContext;
import java.time.Duration;
import java.util.function.Consumer;

/**
 * Options for {@link NotificationClient#connect}.
 *
 * <p>Build one with {@link #builder()}; validation happens when the client
 * connects.
 */
public final class NotificationClientOptions {
    private final String url;
    private final String username;
    private final String secret;
    private final String listener;
    private final String consumer;
    private final Duration connectTimeout;
    private final SSLContext sslContext;
    private final Consumer<NotificationException> onError;

    private NotificationClientOptions(Builder builder) {
        this.url = builder.url;
        this.username = builder.username;
        this.secret = builder.secret;
        this.listener = builder.listener;
        this.consumer = builder.consumer;
        this.connectTimeout = builder.connectTimeout;
        this.sslContext = builder.sslContext;
        this.onError = builder.onError;
    }

    /**
     * Creates a new options builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * The client notification tier WebSocket URL, for example
     * {@code wss://notifications.mxraven.com}. Only the {@code ws:} and {@code wss:}
     * schemes are accepted. A missing port defaults to {@code 443} for
     * {@code wss:} and {@code 80} for {@code ws:}.
     *
     * <p>When unset, {@link NotificationClient#DEFAULT_URL} is used.
     *
     * @return the URL, or {@code null} for the default
     */
    public String url() {
        return url;
    }

    /**
     * The listener API key prefix, for example {@code mxr_tx_ab12cd34ef56}. The
     * notification tier authenticates with the same API key used for SMTP
     * submission.
     *
     * @return the API key prefix
     */
    public String username() {
        return username;
    }

    /**
     * The listener API key secret. Treat it as a secret and do not log it.
     *
     * @return the API key secret
     */
    public String secret() {
        return secret;
    }

    /**
     * The listener whose events are consumed. It is used to derive the durable
     * consumer name and the per-listener inbox prefix, so it must not contain
     * {@code .}, {@code *}, {@code >}, {@code /}, {@code \}, or whitespace.
     *
     * @return the listener ID
     */
    public String listener() {
        return listener;
    }

    /**
     * Overrides the durable consumer name.
     *
     * <p>Defaults to {@code notify-<listener>}, which is the name the control
     * plane creates when notifications are enabled. Set it only when binding to
     * a consumer name reported by the control plane.
     *
     * @return the consumer name, or {@code null} for the default
     */
    public String consumer() {
        return consumer;
    }

    /**
     * The deadline for establishing the connection. Defaults to ten seconds.
     *
     * @return the connect timeout
     */
    public Duration connectTimeout() {
        return connectTimeout;
    }

    /**
     * TLS configuration used for a {@code wss://} connection. Mirrors
     * {@code SmtpConfig.sslContext()}.
     *
     * @return the SSL context, or {@code null} for the default
     */
    public SSLContext sslContext() {
        return sslContext;
    }

    /**
     * Receives non-fatal connection and decode errors.
     *
     * <p>Decode errors normally stop consumption because the same event would
     * be redelivered forever. When this handler is set, it takes responsibility
     * for the undecodable event and consumption continues. Connection errors
     * are reported here while the transport reconnects automatically.
     *
     * <p>The handler runs on the consuming thread and must return promptly. Do
     * not throw from it; exceptions are ignored to keep the connection usable.
     *
     * @return the error handler, or {@code null} when none is configured
     */
    public Consumer<NotificationException> onError() {
        return onError;
    }

    /** Builds {@link NotificationClientOptions} instances. */
    public static final class Builder {
        private String url;
        private String username;
        private String secret;
        private String listener;
        private String consumer;
        private Duration connectTimeout;
        private SSLContext sslContext;
        private Consumer<NotificationException> onError;

        private Builder() {
        }

        /**
         * Overrides the notification tier WebSocket URL.
         *
         * <p>Defaults to {@link NotificationClient#DEFAULT_URL}.
         *
         * @param url the {@code ws://} or {@code wss://} URL
         * @return this builder
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * Sets the listener API key prefix.
         *
         * @param username the API key prefix
         * @return this builder
         */
        public Builder username(String username) {
            this.username = username;
            return this;
        }

        /**
         * Sets the listener API key secret.
         *
         * @param secret the API key secret
         * @return this builder
         */
        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        /**
         * Sets the listener whose events are consumed.
         *
         * @param listener the listener ID
         * @return this builder
         */
        public Builder listener(String listener) {
            this.listener = listener;
            return this;
        }

        /**
         * Overrides the durable consumer name.
         *
         * @param consumer the consumer name
         * @return this builder
         */
        public Builder consumer(String consumer) {
            this.consumer = consumer;
            return this;
        }

        /**
         * Sets the deadline for establishing the connection.
         *
         * @param connectTimeout the connect timeout
         * @return this builder
         */
        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        /**
         * Sets the TLS configuration for a {@code wss://} connection.
         *
         * @param sslContext the SSL context
         * @return this builder
         */
        public Builder sslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        /**
         * Sets the handler for non-fatal connection and decode errors.
         *
         * @param onError the error handler
         * @return this builder
         */
        public Builder onError(Consumer<NotificationException> onError) {
            this.onError = onError;
            return this;
        }

        /**
         * Builds the options.
         *
         * @return the notification client options
         */
        public NotificationClientOptions build() {
            return new NotificationClientOptions(this);
        }
    }
}
