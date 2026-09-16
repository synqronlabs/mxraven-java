package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for <code>PUT /v2/tenants/{slug}/smtp-relays/{relay_id}</code>. This
 * is a full replacement of the relay configuration and credentials.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UpdateSmtpRelayRequest {
    private final String displayName;
    private final String host;
    private final int port;
    private final String username;
    private final String password;

    /** human-readable relay name */
    public String displayName() {
        return displayName;
    }

    /** SMTP relay host */
    public String host() {
        return host;
    }

    /** SMTP relay port */
    public int port() {
        return port;
    }

    /** relay username */
    public String username() {
        return username;
    }

    /** relay password */
    public String password() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateSmtpRelayRequest that = (UpdateSmtpRelayRequest) o;
        return Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.host, that.host)
                && this.port == that.port
                && Objects.equals(this.username, that.username)
                && Objects.equals(this.password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.displayName, this.host, this.port, this.username, this.password);
    }

    @Override
    public String toString() {
        return "UpdateSmtpRelayRequest[" + "displayName=" + this.displayName + ", " + "host=" + this.host + ", " + "port=" + this.port + ", " + "username=" + this.username + ", " + "password=" + this.password + "]";
    }

    /**
     * Creates a new UpdateSmtpRelayRequest.
     *
     * @param displayName human-readable relay name
     * @param host SMTP relay host
     * @param port SMTP relay port
     * @param username relay username
     * @param password relay password
     */
    @JsonCreator
    public UpdateSmtpRelayRequest(String displayName, String host, int port, String username, String password) {
        this.displayName = displayName;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateSmtpRelayRequest} instances. */
    public static final class Builder {
        private String displayName;
        private String host;
        private int port;
        private String username;
        private String password;

        /**
         * Sets the human-readable relay name.
         *
         * @param displayName human-readable relay name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the SMTP relay host.
         *
         * @param host SMTP relay host
         * @return this builder
         */
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * Sets the SMTP relay port.
         *
         * @param port SMTP relay port
         * @return this builder
         */
        public Builder port(int port) {
            this.port = port;
            return this;
        }

        /**
         * Sets the relay username.
         *
         * @param username relay username
         * @return this builder
         */
        public Builder username(String username) {
            this.username = username;
            return this;
        }

        /**
         * Sets the relay password.
         *
         * @param password relay password
         * @return this builder
         */
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         * @throws IllegalArgumentException if the connection details fail validation
         */
        public UpdateSmtpRelayRequest build() {
            validateConnection(displayName, host, port, username, password);
            return new UpdateSmtpRelayRequest(displayName, host, port, username, password);
        }

        private static void validateConnection(String displayName, String host, int port,
                                              String username, String password) {
            if (displayName == null || Java8.isBlank(displayName)) {
                throw new IllegalArgumentException("display_name is required");
            }
            if (displayName.codePointCount(0, displayName.length()) > 255) {
                throw new IllegalArgumentException("display_name must be 255 characters or fewer");
            }
            if (host == null || Java8.isBlank(host)) {
                throw new IllegalArgumentException("host is required");
            }
            if (host.codePointCount(0, host.length()) > 253) {
                throw new IllegalArgumentException("host must be 253 characters or fewer");
            }
            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException("port must be between 1 and 65535");
            }
            if (username == null || Java8.isBlank(username)) {
                throw new IllegalArgumentException("username is required");
            }
            if (username.codePointCount(0, username.length()) > 4096) {
                throw new IllegalArgumentException("username must be 4096 characters or fewer");
            }
            if (password == null || Java8.isBlank(password)) {
                throw new IllegalArgumentException("password is required");
            }
            if (password.codePointCount(0, password.length()) > 4096) {
                throw new IllegalArgumentException("password must be 4096 characters or fewer");
            }
        }

    }
}
