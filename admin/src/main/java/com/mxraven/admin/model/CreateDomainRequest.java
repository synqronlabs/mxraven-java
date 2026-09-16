package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for <code>POST /v2/tenants/{slug}/domains</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateDomainRequest {
    private final String domainName;
    private final String dmarcReportAddress;

    /** domain to onboard for outbound sending */
    public String domainName() {
        return domainName;
    }

    /** optional DMARC aggregate report mailbox */
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
        CreateDomainRequest that = (CreateDomainRequest) o;
        return Objects.equals(this.domainName, that.domainName)
                && Objects.equals(this.dmarcReportAddress, that.dmarcReportAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.domainName, this.dmarcReportAddress);
    }

    @Override
    public String toString() {
        return "CreateDomainRequest[" + "domainName=" + this.domainName + ", " + "dmarcReportAddress=" + this.dmarcReportAddress + "]";
    }

    /**
     * Creates a new CreateDomainRequest.
     *
     * @param domainName domain to onboard for outbound sending
     * @param dmarcReportAddress optional DMARC aggregate report mailbox
     */
    @JsonCreator
    public CreateDomainRequest(String domainName, String dmarcReportAddress) {
        this.domainName = domainName;
        this.dmarcReportAddress = dmarcReportAddress;
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
         */
        public CreateDomainRequest build() {
            return new CreateDomainRequest(domainName, dmarcReportAddress);
        }
    }
}
