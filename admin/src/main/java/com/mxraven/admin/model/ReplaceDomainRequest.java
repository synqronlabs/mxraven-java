package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request body for {@code PUT /v2/tenants/{slug}/domains/{domain_id}}. This is a
 * full replacement of the writable domain configuration; pass {@code null} for
 * {@code dmarcReportAddress} to clear it.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public record ReplaceDomainRequest(String dmarcReportAddress) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String dmarcReportAddress;

        public Builder dmarcReportAddress(String dmarcReportAddress) {
            this.dmarcReportAddress = dmarcReportAddress;
            return this;
        }

        public ReplaceDomainRequest build() {
            return new ReplaceDomainRequest(dmarcReportAddress);
        }
    }
}
