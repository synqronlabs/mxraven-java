package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for <code>PUT /v2/tenants/{slug}/listeners/{listener_id}</code>.
 * Only the display name is writable through this operation.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UpdateListenerRequest {
    private final String displayName;

    /** new human-readable listener name */
    public String displayName() {
        return displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateListenerRequest that = (UpdateListenerRequest) o;
        return Objects.equals(this.displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.displayName);
    }

    @Override
    public String toString() {
        return "UpdateListenerRequest[" + "displayName=" + this.displayName + "]";
    }

    /**
     * Creates a new UpdateListenerRequest.
     *
     * @param displayName new human-readable listener name
     */
    @JsonCreator
    public UpdateListenerRequest(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateListenerRequest} instances. */
    public static final class Builder {
        private String displayName;

        /**
         * Sets the new human-readable listener name.
         *
         * @param displayName new human-readable listener name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         */
        public UpdateListenerRequest build() {
            return new UpdateListenerRequest(displayName);
        }
    }
}
