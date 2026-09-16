package com.mxraven.admin.model;

/**
 * Request body for {@code PUT /v2/tenants/{slug}/smtp-relays/{relay_id}}. This
 * is a full replacement of the relay configuration and credentials.
 *
 * @param displayName human-readable relay name
 * @param host        SMTP relay host
 * @param port        SMTP relay port
 * @param username    relay username
 * @param password    relay password
 */
public record UpdateSmtpRelayRequest(
        String displayName,
        String host,
        int port,
        String username,
        String password) {

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
            if (displayName == null || displayName.isBlank()) {
                throw new IllegalArgumentException("display_name is required");
            }
            if (displayName.codePointCount(0, displayName.length()) > 255) {
                throw new IllegalArgumentException("display_name must be 255 characters or fewer");
            }
            if (host == null || host.isBlank()) {
                throw new IllegalArgumentException("host is required");
            }
            if (host.codePointCount(0, host.length()) > 253) {
                throw new IllegalArgumentException("host must be 253 characters or fewer");
            }
            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException("port must be between 1 and 65535");
            }
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("username is required");
            }
            if (username.codePointCount(0, username.length()) > 4096) {
                throw new IllegalArgumentException("username must be 4096 characters or fewer");
            }
            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("password is required");
            }
            if (password.codePointCount(0, password.length()) > 4096) {
                throw new IllegalArgumentException("password must be 4096 characters or fewer");
            }
        }

    }
}
