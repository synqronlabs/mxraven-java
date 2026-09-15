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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private SuppressionReason reason;

        public Builder reason(SuppressionReason reason) {
            this.reason = reason;
            return this;
        }

        public UpdateTenantSuppressionRequest build() {
            return new UpdateTenantSuppressionRequest(reason);
        }
    }
}
