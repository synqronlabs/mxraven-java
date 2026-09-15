package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import com.mxraven.admin.Paged;

import java.io.IOException;
import java.util.List;

/**
 * A tenant domain and its DNS verification state.
 *
 * <p>A domain must be verified and then granted to a submission listener through
 * its sending-domain policy before it can send. Reads return a snapshot; call
 * {@link #reload()} for fresh state.
 *
 * <pre>{@code
 * Domain d = ws.domains().create("example.com");
 * if (!d.dkimVerified()) {
 *     d.replace("dmarc@example.com");
 * }
 * for (DomainListenerGrant grant : d.listenerGrants()) {
 *     // ...
 * }
 * d.delete();
 * }</pre>
 */
public final class Domain extends Entity<DomainData> {
    public Domain(AdminClient client, String path, DomainData data) {
        super(client, path, data);
    }

    private Domain(AdminClient client, String path, DomainData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    // --- data ----------------------------------------------------------------

    public String id() {
        return data.id();
    }

    public String tenantId() {
        return data.tenantId();
    }

    public String domainName() {
        return data.domainName();
    }

    public String verificationToken() {
        return data.verificationToken();
    }

    public boolean sendingEnabled() {
        return data.sendingEnabled();
    }

    public String dkimActiveSelector() {
        return data.dkimActiveSelector();
    }

    public boolean spfVerified() {
        return data.spfVerified();
    }

    public boolean dkimVerified() {
        return data.dkimVerified();
    }

    public boolean dmarcVerified() {
        return data.dmarcVerified();
    }

    public String dmarcReportAddress() {
        return data.dmarcReportAddress();
    }

    public String dnsLastCheckedAt() {
        return data.dnsLastCheckedAt();
    }

    public DomainStatus status() {
        return data.status();
    }

    public List<DnsInstructionRecord> requiredCustomerRecords() {
        return data.requiredCustomerRecords();
    }

    // --- operations ----------------------------------------------------------

    /** Re-fetch this domain and return a fresh snapshot. */
    public Domain reload() throws IOException {
        return new Domain(client, path, client.get(path).as(DomainData.class), isDeleted());
    }

    /** Replace the writable domain configuration; {@code null} clears the DMARC report address. */
    public Domain replace(String dmarcReportAddress) throws IOException {
        DomainData updated = client.put(path, new ReplaceDomainRequest(dmarcReportAddress))
                .as(DomainData.class);
        return new Domain(client, path, updated, isDeleted());
    }

    /** Listeners authorized to send from this domain. */
    public Paged<DomainListenerGrant> listenerGrants() throws IOException {
        return client.paged(path + "/listener-grants", null, DomainListenerGrant.class);
    }
}
