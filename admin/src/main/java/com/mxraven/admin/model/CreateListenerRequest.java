package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for <code>POST /v2/tenants/{slug}/listeners</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateListenerRequest {
    private final String displayName;
    private final ListenerType listenerType;
    private final StreamType streamType;
    private final TerminalActionType defaultTerminalActionType;
    private final TerminalActionPayload defaultTerminalActionPayload;
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

    /** terminal action type; must be valid for the listener type */
    public TerminalActionType defaultTerminalActionType() {
        return defaultTerminalActionType;
    }

    /** action-specific payload; use {@link TerminalActionPayload} factories, or {@code null} when the action takes none */
    public TerminalActionPayload defaultTerminalActionPayload() {
        return defaultTerminalActionPayload;
    }

    /** enables inbound scanning; defaults by listener type */
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
        return Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.listenerType, that.listenerType)
                && Objects.equals(this.streamType, that.streamType)
                && Objects.equals(this.defaultTerminalActionType, that.defaultTerminalActionType)
                && Objects.equals(this.defaultTerminalActionPayload, that.defaultTerminalActionPayload)
                && Objects.equals(this.rspamdScanningEnabled, that.rspamdScanningEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.displayName, this.listenerType, this.streamType, this.defaultTerminalActionType, this.defaultTerminalActionPayload, this.rspamdScanningEnabled);
    }

    @Override
    public String toString() {
        return "CreateListenerRequest[" + "displayName=" + this.displayName + ", " + "listenerType=" + this.listenerType + ", " + "streamType=" + this.streamType + ", " + "defaultTerminalActionType=" + this.defaultTerminalActionType + ", " + "defaultTerminalActionPayload=" + this.defaultTerminalActionPayload + ", " + "rspamdScanningEnabled=" + this.rspamdScanningEnabled + "]";
    }

    /**
     * Creates a new CreateListenerRequest.
     *
     * @param displayName human-readable listener name
     * @param listenerType {@code submission} or {@code mta}
     * @param streamType {@code transactional} or {@code marketing}
     * @param defaultTerminalActionType terminal action type; must be valid for the listener type
     * @param defaultTerminalActionPayload action-specific payload; use {@link TerminalActionPayload} factories, or {@code null} when the action takes none
     * @param rspamdScanningEnabled enables inbound scanning; defaults by listener type
     */
    @JsonCreator
    public CreateListenerRequest(String displayName, ListenerType listenerType, StreamType streamType, TerminalActionType defaultTerminalActionType, TerminalActionPayload defaultTerminalActionPayload, Boolean rspamdScanningEnabled) {
        this.displayName = displayName;
        this.listenerType = listenerType;
        this.streamType = streamType;
        this.defaultTerminalActionType = defaultTerminalActionType;
        this.defaultTerminalActionPayload = defaultTerminalActionPayload;
        this.rspamdScanningEnabled = rspamdScanningEnabled;
    }

    /**
     * Creates a request without a terminal-action payload or Rspamd override.
     *
     * @param displayName human-readable listener name
     * @param listenerType {@code submission} or {@code mta}
     * @param streamType {@code transactional} or {@code marketing}
     * @param defaultTerminalActionType terminal action type valid for the listener type
     */
    public CreateListenerRequest(String displayName, ListenerType listenerType, StreamType streamType,
                                 TerminalActionType defaultTerminalActionType) {
        this(displayName, listenerType, streamType, defaultTerminalActionType, null, null);
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
        private TerminalActionType defaultTerminalActionType;
        private TerminalActionPayload defaultTerminalActionPayload;
        private Boolean rspamdScanningEnabled;

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
         * Sets the default terminal action type.
         *
         * @param defaultTerminalActionType terminal action type
         * @return this builder
         */
        public Builder defaultTerminalActionType(TerminalActionType defaultTerminalActionType) {
            this.defaultTerminalActionType = defaultTerminalActionType;
            return this;
        }

        /**
         * Set the terminal action and an empty payload, for actions that take none.
         *
         * @param defaultTerminalActionType terminal action type
         * @return this builder
         */
        public Builder defaultTerminalAction(TerminalActionType defaultTerminalActionType) {
            this.defaultTerminalActionType = defaultTerminalActionType;
            this.defaultTerminalActionPayload = TerminalActionPayload.empty();
            return this;
        }

        /**
         * Set the terminal action and its action-specific payload together.
         *
         * @param defaultTerminalActionType terminal action type
         * @param defaultTerminalActionPayload action-specific payload
         * @return this builder
         */
        public Builder defaultTerminalAction(TerminalActionType defaultTerminalActionType,
                                             TerminalActionPayload defaultTerminalActionPayload) {
            this.defaultTerminalActionType = defaultTerminalActionType;
            this.defaultTerminalActionPayload = defaultTerminalActionPayload;
            return this;
        }

        /**
         * Sets the terminal-action payload.
         *
         * @param defaultTerminalActionPayload action-specific payload
         * @return this builder
         */
        public Builder defaultTerminalActionPayload(TerminalActionPayload defaultTerminalActionPayload) {
            this.defaultTerminalActionPayload = defaultTerminalActionPayload;
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
            return new CreateListenerRequest(displayName, listenerType, streamType, defaultTerminalActionType, defaultTerminalActionPayload, rspamdScanningEnabled);
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
            if (defaultTerminalActionType == null) {
                throw new IllegalArgumentException("default_terminal_action_type is required");
            }
            if (!validTerminalAction(listenerType, defaultTerminalActionType)) {
                throw new IllegalArgumentException("default_terminal_action_type " + defaultTerminalActionType
                        + " is not valid for listener_type " + listenerType);
            }
            if (Boolean.TRUE.equals(rspamdScanningEnabled) && listenerType != ListenerType.MTA) {
                throw new IllegalArgumentException("rspamd_scanning_enabled is only valid for mta listeners");
            }
        }

        private static boolean validTerminalAction(ListenerType listenerType, TerminalActionType action) {
            if (listenerType == ListenerType.SUBMISSION) {
                return action == TerminalActionType.DELIVER_DEDICATED
                        || action == TerminalActionType.DELIVER
                        || action == TerminalActionType.SMARTHOST_RELAY
                        || action == TerminalActionType.DROP
                        || action == TerminalActionType.REJECT;
            }
            return action == TerminalActionType.RELAY
                    || action == TerminalActionType.AUTO_REPLY
                    || action == TerminalActionType.DROP
                    || action == TerminalActionType.REJECT;
        }
    }
}
