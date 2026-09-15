package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/suppressions}.
 *
 * @param reason optional suppression reason; one of {@code unsubscribe} or
 *               {@code bounce}
 */
public record CreateTenantSuppressionRequest(
        String emailAddress,
        SuppressionReason reason) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String emailAddress;
        private SuppressionReason reason;

        public Builder emailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public Builder reason(SuppressionReason reason) {
            this.reason = reason;
            return this;
        }

        public CreateTenantSuppressionRequest build() {
            return new CreateTenantSuppressionRequest(emailAddress, reason);
        }
    }
}
