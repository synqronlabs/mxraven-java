package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Request body for
 * <code>PUT /v2/tenants/{slug}/listeners/{listener_id}/sending-domain-policy</code>.
 * This atomically replaces the listener's complete set of domain grants.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ReplaceSendingDomainPolicyRequest {
    private final List<SendingDomainPolicyGrantRequest> grants;

    /** complete set of domain grants */
    public List<SendingDomainPolicyGrantRequest> grants() {
        return grants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReplaceSendingDomainPolicyRequest that = (ReplaceSendingDomainPolicyRequest) o;
        return Objects.equals(this.grants, that.grants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.grants);
    }

    @Override
    public String toString() {
        return "ReplaceSendingDomainPolicyRequest[" + "grants=" + this.grants + "]";
    }

    /**
     * Creates a new ReplaceSendingDomainPolicyRequest.
     *
     * @param grants complete set of domain grants
     */
    @JsonCreator
    public ReplaceSendingDomainPolicyRequest(List<SendingDomainPolicyGrantRequest> grants) {
        this.grants = grants;
    }

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link ReplaceSendingDomainPolicyRequest}. */
    public static final class Builder {
        private List<SendingDomainPolicyGrantRequest> grants;

        /**
         * Sets the complete set of domain grants.
         *
         * @param grants complete set of domain grants
         * @return this builder
         */
        public Builder grants(List<SendingDomainPolicyGrantRequest> grants) {
            this.grants = grants;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return a new request
         */
        public ReplaceSendingDomainPolicyRequest build() {
            return new ReplaceSendingDomainPolicyRequest(grants);
        }
    }
}
