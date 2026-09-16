package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request body for {@code PUT /v2/tenants/{slug}/suppressions/{email_address}}.
 *
 * @param reason replacement suppression reason; null clears it
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public record UpdateTenantSuppressionRequest(
        SuppressionReason reason) {

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
