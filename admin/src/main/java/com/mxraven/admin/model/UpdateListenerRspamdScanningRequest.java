package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for
 * <code>PUT /v2/tenants/{slug}/listeners/{listener_id}/rspamd-scanning</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UpdateListenerRspamdScanningRequest {
    private final boolean enabled;

    /** whether inbound Rspamd scanning is enabled */
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
        UpdateListenerRspamdScanningRequest that = (UpdateListenerRspamdScanningRequest) o;
        return this.enabled == that.enabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.enabled);
    }

    @Override
    public String toString() {
        return "UpdateListenerRspamdScanningRequest[" + "enabled=" + this.enabled + "]";
    }

    /**
     * Creates a new UpdateListenerRspamdScanningRequest.
     *
     * @param enabled whether inbound Rspamd scanning is enabled
     */
    @JsonCreator
    public UpdateListenerRspamdScanningRequest(boolean enabled) {
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

    /** Builds {@link UpdateListenerRspamdScanningRequest} instances. */
    public static final class Builder {
        private boolean enabled;

        /**
         * Sets whether inbound Rspamd scanning is enabled.
         *
         * @param enabled whether inbound scanning is enabled
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
        public UpdateListenerRspamdScanningRequest build() {
            return new UpdateListenerRspamdScanningRequest(enabled);
        }
    }
}
