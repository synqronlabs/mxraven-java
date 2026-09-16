package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request body for <code>PUT /v2/tenants/{slug}/suppressions/{email_address}</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class UpdateTenantSuppressionRequest {
    private final SuppressionReason reason;

    /** replacement suppression reason; null clears it */
    public SuppressionReason reason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateTenantSuppressionRequest that = (UpdateTenantSuppressionRequest) o;
        return Objects.equals(this.reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.reason);
    }

    @Override
    public String toString() {
        return "UpdateTenantSuppressionRequest[" + "reason=" + this.reason + "]";
    }

    /**
     * Creates a new UpdateTenantSuppressionRequest.
     *
     * @param reason replacement suppression reason; null clears it
     */
    @JsonCreator
    public UpdateTenantSuppressionRequest(SuppressionReason reason) {
        this.reason = reason;
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateTenantSuppressionRequest} instances. */
    public static final class Builder {
        private SuppressionReason reason;

        /**
         * Sets the replacement suppression reason.
         *
         * @param reason replacement suppression reason; may be null
         * @return this builder
         */
        public Builder reason(SuppressionReason reason) {
            this.reason = reason;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         */
        public UpdateTenantSuppressionRequest build() {
            return new UpdateTenantSuppressionRequest(reason);
        }
    }
}
