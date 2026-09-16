package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for
 * <code>PUT /v2/tenants/{slug}/listeners/{listener_id}/default-terminal-action</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UpdateListenerDefaultTerminalActionRequest {
    private final TerminalActionType actionType;
    private final TerminalActionPayload actionPayload;

    /** terminal action type; must be valid for the listener type */
    public TerminalActionType actionType() {
        return actionType;
    }

    /** action-specific payload; use {@link TerminalActionPayload} factories, or {@code null} when the action takes none */
    public TerminalActionPayload actionPayload() {
        return actionPayload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateListenerDefaultTerminalActionRequest that = (UpdateListenerDefaultTerminalActionRequest) o;
        return Objects.equals(this.actionType, that.actionType)
                && Objects.equals(this.actionPayload, that.actionPayload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.actionType, this.actionPayload);
    }

    @Override
    public String toString() {
        return "UpdateListenerDefaultTerminalActionRequest[" + "actionType=" + this.actionType + ", " + "actionPayload=" + this.actionPayload + "]";
    }

    /**
     * Creates a new UpdateListenerDefaultTerminalActionRequest.
     *
     * @param actionType terminal action type; must be valid for the listener type
     * @param actionPayload action-specific payload; use {@link TerminalActionPayload} factories, or {@code null} when the action takes none
     */
    @JsonCreator
    public UpdateListenerDefaultTerminalActionRequest(TerminalActionType actionType, TerminalActionPayload actionPayload) {
        this.actionType = actionType;
        this.actionPayload = actionPayload;
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateListenerDefaultTerminalActionRequest} instances. */
    public static final class Builder {
        private TerminalActionType actionType;
        private TerminalActionPayload actionPayload;

        /**
         * Sets the terminal action type.
         *
         * @param actionType terminal action type
         * @return this builder
         */
        public Builder actionType(TerminalActionType actionType) {
            this.actionType = actionType;
            return this;
        }

        /**
         * Set the terminal action and an empty payload, for actions that take none.
         *
         * @param actionType terminal action type
         * @return this builder
         */
        public Builder action(TerminalActionType actionType) {
            this.actionType = actionType;
            this.actionPayload = TerminalActionPayload.empty();
            return this;
        }

        /**
         * Set the terminal action and its action-specific payload together.
         *
         * @param actionType    terminal action type
         * @param actionPayload action-specific payload
         * @return this builder
         */
        public Builder action(TerminalActionType actionType, TerminalActionPayload actionPayload) {
            this.actionType = actionType;
            this.actionPayload = actionPayload;
            return this;
        }

        /**
         * Sets the action-specific payload.
         *
         * @param actionPayload action-specific payload; may be null
         * @return this builder
         */
        public Builder actionPayload(TerminalActionPayload actionPayload) {
            this.actionPayload = actionPayload;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         */
        public UpdateListenerDefaultTerminalActionRequest build() {
            return new UpdateListenerDefaultTerminalActionRequest(actionType, actionPayload);
        }
    }
}
