package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for {@code POST /v2/tenants/{slug}/listeners}.
 *
 * <p>A new listener has no routing script and uses the listener-type default:
 * submission listeners deliver through the shared pool, and MTA listeners drop
 * after acceptance. Publish a script through
 * {@link com.mxraven.admin.client.RoutingScriptsClient} to replace the default.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateListenerRequest {
    private final String displayName;
    private final ListenerType listenerType;
    private final StreamType streamType;
    private final Boolean rspamdScanningEnabled;

    /** human-readable listener name */
    public String displayName() {
        return displayName;
    }

    /** {@code submission} or {@code mta} */
    public ListenerType listenerType() {
        return listenerType;
    }

    /** {@code transactional} or {@code marketing} */
    public StreamType streamType() {
        return streamType;
    }

    public Boolean rspamdScanningEnabled() {
        return rspamdScanningEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateListenerRequest that = (CreateListenerRequest) o;
        return Objects.equals(this.displayName, that.displayName) && Objects.equals(this.listenerType, that.listenerType) && Objects.equals(this.streamType, that.streamType) && Objects.equals(this.rspamdScanningEnabled, that.rspamdScanningEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.displayName, this.listenerType, this.streamType, this.rspamdScanningEnabled);
    }

    @Override
    public String toString() {
        return "CreateListenerRequest[" + "displayName=" + this.displayName + ", " + "listenerType=" + this.listenerType + ", " + "streamType=" + this.streamType + ", " + "rspamdScanningEnabled=" + this.rspamdScanningEnabled + "]";
    }

    /**
     * Creates a new CreateListenerRequest.
     *
     * @param displayName human-readable listener name
     * @param listenerType {@code submission} or {@code mta}
     * @param streamType {@code transactional} or {@code marketing}
     * @param rspamdScanningEnabled rspamdScanningEnabled
     */
    @JsonCreator
    public CreateListenerRequest(String displayName, ListenerType listenerType, StreamType streamType, Boolean rspamdScanningEnabled) {
        this.displayName = displayName;
        this.listenerType = listenerType;
        this.streamType = streamType;
        this.rspamdScanningEnabled = rspamdScanningEnabled;
    }

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
                if (displayName == null || Java8.isBlank(displayName)) {
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
