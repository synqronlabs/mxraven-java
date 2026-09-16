package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request body for {@code PUT /v2/tenants/{slug}/domains/{domain_id}}. This is a
 * full replacement of the writable domain configuration; pass {@code null} for
 * {@code dmarcReportAddress} to clear it.
 *
 * @param dmarcReportAddress DMARC report address, or {@code null} to clear it
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public record ReplaceDomainRequest(String dmarcReportAddress) {

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
