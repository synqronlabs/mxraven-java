package com.mxraven.mail;

import javax.net.ssl.SSLContext;
import java.time.Duration;

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

    public static Builder builder() {
        return new Builder();
    }

    public String host() {
        return host;
    }

    public int port() {
        return port;
    }

    public SecurityMode security() {
        return security;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public SSLContext sslContext() {
        return sslContext;
    }

    public String localName() {
        return localName;
    }

    public Duration connectTimeout() {
        return connectTimeout;
    }

    public Duration readTimeout() {
        return readTimeout;
    }

    public Duration writeTimeout() {
        return writeTimeout;
    }

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

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder security(SecurityMode security) {
            this.security = security;
            return this;
        }

        public Builder startTls() {
            this.security = SecurityMode.STARTTLS;
            return this;
        }

        public Builder implicitTls() {
            this.security = SecurityMode.IMPLICIT_TLS;
            return this;
        }

        public Builder noTls() {
            this.security = SecurityMode.NONE;
            return this;
        }

        public Builder credentials(String username, String password) {
            this.username = username;
            this.password = password;
            return this;
        }

        public Builder sslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        public Builder localName(String localName) {
            this.localName = localName;
            return this;
        }

        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder writeTimeout(Duration writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public SmtpConfig build() {
            if (host == null || host.isBlank()) {
                throw new IllegalStateException("host must not be blank");
            }
            return new SmtpConfig(this);
        }
    }
}
