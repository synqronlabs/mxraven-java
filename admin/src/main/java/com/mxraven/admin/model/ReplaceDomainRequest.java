package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request body for <code>PUT /v2/tenants/{slug}/domains/{domain_id}</code>. This is a
 * full replacement of the writable domain configuration; pass {@code null} for
 * {@code dmarcReportAddress} to clear it.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class ReplaceDomainRequest {
    private final String dmarcReportAddress;

    /** DMARC report address, or {@code null} to clear it */
    public String dmarcReportAddress() {
        return dmarcReportAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReplaceDomainRequest that = (ReplaceDomainRequest) o;
        return Objects.equals(this.dmarcReportAddress, that.dmarcReportAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dmarcReportAddress);
    }

    @Override
    public String toString() {
        return "ReplaceDomainRequest[" + "dmarcReportAddress=" + this.dmarcReportAddress + "]";
    }

    /**
     * Creates a new ReplaceDomainRequest.
     *
     * @param dmarcReportAddress DMARC report address, or {@code null} to clear it
     */
    @JsonCreator
    public ReplaceDomainRequest(String dmarcReportAddress) {
        this.dmarcReportAddress = dmarcReportAddress;
    }

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link ReplaceDomainRequest}. */
    public static final class Builder {
        private String dmarcReportAddress;

        /**
         * Sets the DMARC report address.
         *
         * @param dmarcReportAddress DMARC report address, or {@code null} to clear it
         * @return this builder
         */
        public Builder dmarcReportAddress(String dmarcReportAddress) {
            this.dmarcReportAddress = dmarcReportAddress;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return a new request
         */
        public ReplaceDomainRequest build() {
            return new ReplaceDomainRequest(dmarcReportAddress);
        }
    }
}
