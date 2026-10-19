package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for
 * <code>PUT /v2/tenants/{slug}/listeners/{listener_id}/notifications</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UpdateListenerNotificationsRequest {
    private final boolean enabled;

    /** whether the listener publishes client notification events */
    public boolean enabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateListenerNotificationsRequest that = (UpdateListenerNotificationsRequest) o;
        return this.enabled == that.enabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.enabled);
    }

    @Override
    public String toString() {
        return "UpdateListenerNotificationsRequest[" + "enabled=" + this.enabled + "]";
    }

    /**
     * Creates a new UpdateListenerNotificationsRequest.
     *
     * @param enabled whether the listener publishes client notification events
     */
    @JsonCreator
    public UpdateListenerNotificationsRequest(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateListenerNotificationsRequest} instances. */
    public static final class Builder {
        private boolean enabled;

        /**
         * Sets whether the listener publishes client notification events.
         *
         * @param enabled whether client notifications are enabled
         * @return this builder
         */
        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         */
        public UpdateListenerNotificationsRequest build() {
            return new UpdateListenerNotificationsRequest(enabled);
        }
    }
}
