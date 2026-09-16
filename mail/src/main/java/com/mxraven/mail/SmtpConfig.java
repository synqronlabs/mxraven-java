package com.mxraven.mail;

import javax.net.ssl.SSLContext;
import java.time.Duration;

/**
 * Immutable configuration for an {@link SmtpClient} connection.
 *
 * <p>Create one with {@link #builder()} and override defaults such as the host,
 * port, transport security, credentials, and timeouts.
 */
public final class SmtpConfig {
    /** Default mxRaven submission host, used when {@code host(...)} is not set. */
    public static final String DEFAULT_HOST = "smtp.mxraven.email";

    private final String host;
    private final int port;
    private final SecurityMode security;
    private final String username;
    private final String password;
    private final SSLContext sslContext;
    private final String localName;
    private final Duration connectTimeout;
    private final Duration readTimeout;
    private final Duration writeTimeout;

    private SmtpConfig(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.security = builder.security;
        this.username = builder.username;
        this.password = builder.password;
        this.sslContext = builder.sslContext;
        this.localName = builder.localName;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
    }

    /**
     * Creates a new configuration builder with the documented defaults.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the configured SMTP host.
     *
     * @return the SMTP host
     */
    public String host() {
        return host;
    }

    /**
     * Returns the configured SMTP port.
     *
     * @return the SMTP port
     */
    public int port() {
        return port;
    }

    /**
     * Returns the configured transport security mode.
     *
     * @return the transport security mode
     */
    public SecurityMode security() {
        return security;
    }

    /**
     * Returns the configured authentication username.
     *
     * @return the username, or {@code null} when unset
     */
    public String username() {
        return username;
    }

    /**
     * Returns the configured authentication password.
     *
     * @return the password, or {@code null} when unset
     */
    public String password() {
        return password;
    }

    /**
     * Returns the configured TLS context.
     *
     * @return the TLS context, or {@code null} to use the JVM default
     */
    public SSLContext sslContext() {
        return sslContext;
    }

    /**
     * Returns the name presented in the EHLO or HELO greeting.
     *
     * @return the local name
     */
    public String localName() {
        return localName;
    }

    /**
     * Returns the configured connection timeout.
     *
     * @return the connect timeout
     */
    public Duration connectTimeout() {
        return connectTimeout;
    }

    /**
     * Returns the configured socket read timeout.
     *
     * @return the read timeout
     */
    public Duration readTimeout() {
        return readTimeout;
    }

    /**
     * Returns the configured socket write timeout.
     *
     * @return the write timeout
     */
    public Duration writeTimeout() {
        return writeTimeout;
    }

    /**
     * Builds {@link SmtpConfig} instances.
     */
    public static final class Builder {
        private String host = DEFAULT_HOST;
        private int port = 587;
        private SecurityMode security = SecurityMode.STARTTLS;
        private String username;
        private String password;
        private SSLContext sslContext;
        private String localName = "localhost";
        private Duration connectTimeout = Duration.ofSeconds(30);
        private Duration readTimeout = Duration.ofMinutes(2);
        private Duration writeTimeout = Duration.ofMinutes(2);

        /**
         * Sets the SMTP host.
         *
         * @param host the SMTP host
         * @return this builder
         */
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * Sets the SMTP port.
         *
         * @param port the SMTP port
         * @return this builder
         */
        public Builder port(int port) {
            this.port = port;
            return this;
        }

        /**
         * Sets the transport security mode.
         *
         * @param security the transport security mode
         * @return this builder
         */
        public Builder security(SecurityMode security) {
            this.security = security;
            return this;
        }

        /**
         * Selects the {@link SecurityMode#STARTTLS} security mode.
         *
         * @return this builder
         */
        public Builder startTls() {
            this.security = SecurityMode.STARTTLS;
            return this;
        }

        /**
         * Selects the {@link SecurityMode#IMPLICIT_TLS} security mode.
         *
         * @return this builder
         */
        public Builder implicitTls() {
            this.security = SecurityMode.IMPLICIT_TLS;
            return this;
        }

        /**
         * Selects the {@link SecurityMode#NONE} security mode.
         *
         * @return this builder
         */
        public Builder noTls() {
            this.security = SecurityMode.NONE;
            return this;
        }

        /**
         * Sets the authentication credentials.
         *
         * @param username the authentication username
         * @param password the authentication password
         * @return this builder
         */
        public Builder credentials(String username, String password) {
            this.username = username;
            this.password = password;
            return this;
        }

        /**
         * Sets the TLS context used for secure connections.
         *
         * @param sslContext the TLS context, or {@code null} to use the JVM default
         * @return this builder
         */
        public Builder sslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        /**
         * Sets the name presented in the EHLO or HELO greeting.
         *
         * @param localName the local name
         * @return this builder
         */
        public Builder localName(String localName) {
            this.localName = localName;
            return this;
        }

        /**
         * Sets the connection timeout.
         *
         * @param connectTimeout the connect timeout
         * @return this builder
         */
        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        /**
         * Sets the socket read timeout.
         *
         * @param readTimeout the read timeout
         * @return this builder
         */
        public Builder readTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        /**
         * Sets the socket write timeout.
         *
         * @param writeTimeout the write timeout
         * @return this builder
         */
        public Builder writeTimeout(Duration writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        /**
         * Creates the configuration.
         *
         * @return the configured {@link SmtpConfig}
         * @throws IllegalStateException when the host is {@code null} or blank
         */
        public SmtpConfig build() {
            if (host == null || host.isBlank()) {
                throw new IllegalStateException("host must not be blank");
            }
            return new SmtpConfig(this);
        }
    }
}
