package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/domains}.
 *
 * @param domainName          domain to onboard for outbound sending
 * @param dmarcReportAddress  optional DMARC aggregate report mailbox
 */
public record CreateDomainRequest(String domainName, String dmarcReportAddress) {

    public CreateDomainRequest(String domainName) {
        this(domainName, null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String domainName;
        private String dmarcReportAddress;

        public Builder domainName(String domainName) {
            this.domainName = domainName;
            return this;
        }

        public Builder dmarcReportAddress(String dmarcReportAddress) {
            this.dmarcReportAddress = dmarcReportAddress;
            return this;
        }

        public CreateDomainRequest build() {
            return new CreateDomainRequest(domainName, dmarcReportAddress);
        }
    }
}
