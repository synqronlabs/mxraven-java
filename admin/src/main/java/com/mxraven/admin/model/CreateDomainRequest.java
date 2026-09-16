package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/domains}.
 *
 * @param domainName          domain to onboard for outbound sending
 * @param dmarcReportAddress  optional DMARC aggregate report mailbox
 */
public record CreateDomainRequest(String domainName, String dmarcReportAddress) {

    /**
     * Creates a request with no DMARC report address.
     *
     * @param domainName domain to onboard for outbound sending
     */
    public CreateDomainRequest(String domainName) {
        this(domainName, null);
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds a {@link CreateDomainRequest}. */
    public static final class Builder {
        private String domainName;
        private String dmarcReportAddress;

        /**
         * Sets the domain to onboard for outbound sending.
         *
         * @param domainName domain name
         * @return this builder
         */
        public Builder domainName(String domainName) {
            this.domainName = domainName;
            return this;
        }

        /**
         * Sets the DMARC aggregate report mailbox.
         *
         * @param dmarcReportAddress DMARC report mailbox
         * @return this builder
         */
        public Builder dmarcReportAddress(String dmarcReportAddress) {
            this.dmarcReportAddress = dmarcReportAddress;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return new request
         */
        public CreateDomainRequest build() {
            return new CreateDomainRequest(domainName, dmarcReportAddress);
        }
    }
}
