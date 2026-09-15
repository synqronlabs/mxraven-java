package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/listeners}.
 *
 * @param displayName                human-readable listener name
 * @param listenerType               {@code submission} or {@code mta}
 * @param streamType                 {@code transactional} or {@code marketing}
 * @param defaultTerminalActionType  terminal action type; must be valid for the listener type
 * @param defaultTerminalActionPayload action-specific payload; use {@link TerminalActionPayload}
 *                                     factories, or {@code null} when the action takes none
 * @param rspamdScanningEnabled      enables inbound scanning; defaults by listener type
 */
public record CreateListenerRequest(
        String displayName,
        ListenerType listenerType,
        StreamType streamType,
        TerminalActionType defaultTerminalActionType,
        TerminalActionPayload defaultTerminalActionPayload,
        Boolean rspamdScanningEnabled) {

    public CreateListenerRequest(String displayName, ListenerType listenerType, StreamType streamType,
                                 TerminalActionType defaultTerminalActionType) {
        this(displayName, listenerType, streamType, defaultTerminalActionType, null, null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String displayName;
        private ListenerType listenerType;
        private StreamType streamType;
        private TerminalActionType defaultTerminalActionType;
        private TerminalActionPayload defaultTerminalActionPayload;
        private Boolean rspamdScanningEnabled;

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder listenerType(ListenerType listenerType) {
            this.listenerType = listenerType;
            return this;
        }

        public Builder streamType(StreamType streamType) {
            this.streamType = streamType;
            return this;
        }

        public Builder defaultTerminalActionType(TerminalActionType defaultTerminalActionType) {
            this.defaultTerminalActionType = defaultTerminalActionType;
            return this;
        }

        /** Set the terminal action and an empty payload, for actions that take none. */
        public Builder defaultTerminalAction(TerminalActionType defaultTerminalActionType) {
            this.defaultTerminalActionType = defaultTerminalActionType;
            this.defaultTerminalActionPayload = TerminalActionPayload.empty();
            return this;
        }

        /** Set the terminal action and its action-specific payload together. */
        public Builder defaultTerminalAction(TerminalActionType defaultTerminalActionType,
                                             TerminalActionPayload defaultTerminalActionPayload) {
            this.defaultTerminalActionType = defaultTerminalActionType;
            this.defaultTerminalActionPayload = defaultTerminalActionPayload;
            return this;
        }

        public Builder defaultTerminalActionPayload(TerminalActionPayload defaultTerminalActionPayload) {
            this.defaultTerminalActionPayload = defaultTerminalActionPayload;
            return this;
        }

        public Builder rspamdScanningEnabled(Boolean rspamdScanningEnabled) {
            this.rspamdScanningEnabled = rspamdScanningEnabled;
            return this;
        }

        public CreateListenerRequest build() {
            validate();
            return new CreateListenerRequest(displayName, listenerType, streamType, defaultTerminalActionType, defaultTerminalActionPayload, rspamdScanningEnabled);
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
            return switch (listenerType) {
                case SUBMISSION -> action == TerminalActionType.DELIVER_DEDICATED
                        || action == TerminalActionType.DELIVER
                        || action == TerminalActionType.SMARTHOST_RELAY
                        || action == TerminalActionType.DROP
                        || action == TerminalActionType.REJECT;
                case MTA -> action == TerminalActionType.RELAY
                        || action == TerminalActionType.AUTO_REPLY
                        || action == TerminalActionType.DROP
                        || action == TerminalActionType.REJECT;
            };
        }
    }
}
