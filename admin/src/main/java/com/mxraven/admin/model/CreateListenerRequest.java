package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/listeners}.
 *
 * <p>A new listener has no routing script and uses the listener-type default:
 * submission listeners deliver through the shared pool, and MTA listeners drop
 * after acceptance. Publish a script through
 * {@link com.mxraven.admin.client.RoutingScriptsClient} to replace the default.
 *
 * @param displayName human-readable listener name
 * @param listenerType {@code submission} or {@code mta}
 * @param streamType {@code transactional} or {@code marketing}
 * @param rspamdScanningEnabled enables inbound scanning; defaults by listener type
 */
public record CreateListenerRequest(
        String displayName,
        ListenerType listenerType,
        StreamType streamType,
        Boolean rspamdScanningEnabled) {

    /**
     * Creates a request without a Rspamd override.
     *
     * @param displayName human-readable listener name
     * @param listenerType {@code submission} or {@code mta}
     * @param streamType {@code transactional} or {@code marketing}
     */
    public CreateListenerRequest(String displayName, ListenerType listenerType, StreamType streamType) {
        this(displayName, listenerType, streamType, null);
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link CreateListenerRequest}. */
    public static final class Builder {
        private String displayName;
        private ListenerType listenerType;
        private StreamType streamType;
        private Boolean rspamdScanningEnabled;

        private Builder() {
        }

        /**
         * Sets the human-readable listener name.
         *
         * @param displayName listener name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the listener type.
         *
         * @param listenerType {@code submission} or {@code mta}
         * @return this builder
         */
        public Builder listenerType(ListenerType listenerType) {
            this.listenerType = listenerType;
            return this;
        }

        /**
         * Sets the stream type.
         *
         * @param streamType {@code transactional} or {@code marketing}
         * @return this builder
         */
        public Builder streamType(StreamType streamType) {
            this.streamType = streamType;
            return this;
        }

        /**
         * Sets whether Rspamd scanning is enabled.
         *
         * @param rspamdScanningEnabled Rspamd scanning flag
         * @return this builder
         */
        public Builder rspamdScanningEnabled(Boolean rspamdScanningEnabled) {
            this.rspamdScanningEnabled = rspamdScanningEnabled;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return new request
         * @throws IllegalArgumentException when a field is missing or invalid
         */
        public CreateListenerRequest build() {
            validate();
            return new CreateListenerRequest(displayName, listenerType, streamType, rspamdScanningEnabled);
        }

        private void validate() {
            if (displayName == null || displayName.isBlank()) {
                throw new IllegalArgumentException("display_name is required");
            }
            if (displayName.codePointCount(0, displayName.length()) > 255) {
                throw new IllegalArgumentException("display_name must be 255 characters or fewer");
            }
            if (listenerType == null) {
                throw new IllegalArgumentException("listener_type is required");
            }
            if (streamType == null) {
                throw new IllegalArgumentException("stream_type is required");
            }
            if (streamType == StreamType.SYSTEM) {
                throw new IllegalArgumentException("stream_type 'system' cannot be used to create a listener");
            }
            if (Boolean.TRUE.equals(rspamdScanningEnabled) && listenerType != ListenerType.MTA) {
                throw new IllegalArgumentException("rspamd_scanning_enabled is only valid for mta listeners");
            }
        }
    }
}
