package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for <code>POST /v2/tenants/{slug}/suppressions</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateTenantSuppressionRequest {
    private final String emailAddress;
    private final SuppressionReason reason;

    /** email address to suppress */
    public String emailAddress() {
        return emailAddress;
    }

    /** optional suppression reason; one of {@code unsubscribe} or {@code bounce} */
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
        CreateTenantSuppressionRequest that = (CreateTenantSuppressionRequest) o;
        return Objects.equals(this.emailAddress, that.emailAddress)
                && Objects.equals(this.reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.emailAddress, this.reason);
    }

    @Override
    public String toString() {
        return "CreateTenantSuppressionRequest[" + "emailAddress=" + this.emailAddress + ", " + "reason=" + this.reason + "]";
    }

    /**
     * Creates a new CreateTenantSuppressionRequest.
     *
     * @param emailAddress email address to suppress
     * @param reason optional suppression reason; one of {@code unsubscribe} or {@code bounce}
     */
    @JsonCreator
    public CreateTenantSuppressionRequest(String emailAddress, SuppressionReason reason) {
        this.emailAddress = emailAddress;
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

    /** Builds a {@link CreateTenantSuppressionRequest}. */
    public static final class Builder {
        private String emailAddress;
        private SuppressionReason reason;

        /**
         * Sets the email address to suppress.
         *
         * @param emailAddress email address
         * @return this builder
         */
        public Builder emailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        /**
         * Sets the suppression reason.
         *
         * @param reason suppression reason
         * @return this builder
         */
        public Builder reason(SuppressionReason reason) {
            this.reason = reason;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return new request
         */
        public CreateTenantSuppressionRequest build() {
            return new CreateTenantSuppressionRequest(emailAddress, reason);
        }
    }
}
