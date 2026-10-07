package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/domains}.
 *
 * @param domainName          domain to onboard for outbound sending
 * @param dmarcReportAddress  optional DMARC aggregate report mailbox
 */
public record CreateDomainRequest(String domainName, String dmarcReportAddress) {

    /**
     * Validates and creates a domain creation request.
     *
     * @param domainName         domain to onboard for outbound sending
     * @param dmarcReportAddress optional DMARC aggregate report mailbox
     * @throws IllegalArgumentException if the domain name is missing, shorter than
     *         3 characters, or longer than 253 characters, or the DMARC report
     *         address is longer than 320 characters
     */
    public CreateDomainRequest {
        RequestSupport.requireString(domainName, "domain_name", 253);
        if (domainName.codePointCount(0, domainName.length()) < 3) {
            throw new IllegalArgumentException("domain_name must be at least 3 characters");
        }
        RequestSupport.optionalString(dmarcReportAddress, "dmarc_report_address", 320);
    }

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
         * @throws IllegalArgumentException when a field is missing or invalid
         */
        public CreateDomainRequest build() {
            return new CreateDomainRequest(domainName, dmarcReportAddress);
        }
    }
}
