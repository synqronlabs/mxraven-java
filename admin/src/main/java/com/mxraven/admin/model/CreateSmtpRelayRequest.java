package com.mxraven.admin.model;

import java.util.regex.Pattern;

/**
 * Request body for {@code POST /v2/tenants/{slug}/smtp-relays}.
 *
 * @param relayRef    immutable human-readable relay reference
 * @param displayName human-readable relay name
 * @param host        SMTP relay host
 * @param port        SMTP relay port
 * @param username    relay username
 * @param password    relay password
 */
public record CreateSmtpRelayRequest(
        String relayRef,
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

    /** Builds a {@link CreateSmtpRelayRequest}. */
    public static final class Builder {
        private String relayRef;
        private String displayName;
        private String host;
        private int port;
        private String username;
        private String password;

        /**
         * Sets the immutable relay reference.
         *
         * @param relayRef relay reference
         * @return this builder
         */
        public Builder relayRef(String relayRef) {
            this.relayRef = relayRef;
            return this;
        }

        /**
         * Sets the human-readable relay name.
         *
         * @param displayName relay name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the SMTP relay host.
         *
         * @param host relay host
         * @return this builder
         */
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * Sets the SMTP relay port.
         *
         * @param port relay port
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

        private static final Pattern REFERENCE = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*$");

        /**
         * Builds the request.
         *
         * @return new request
         * @throws IllegalArgumentException when a field is missing or invalid
         */
        public CreateSmtpRelayRequest build() {
            if (relayRef == null || relayRef.isBlank()) {
                throw new IllegalArgumentException("relay_ref is required");
            }
            if (relayRef.codePointCount(0, relayRef.length()) > 100) {
                throw new IllegalArgumentException("relay_ref must be 100 characters or fewer");
            }
            if (!REFERENCE.matcher(relayRef).matches()) {
                throw new IllegalArgumentException("relay_ref must start with a letter or digit and may contain "
                        + "letters, digits, dots, underscores, or hyphens");
            }
            validateConnection(displayName, host, port, username, password);
            return new CreateSmtpRelayRequest(relayRef, displayName, host, port, username, password);
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
