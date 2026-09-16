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
    /**
     * Creates a domain entity bound to a client, path, and backing data.
     *
     * @param client admin client used for requests
     * @param path resource path
     * @param data backing domain data
     */
    public Domain(AdminClient client, String path, DomainData data) {
        super(client, path, data);
    }

    private Domain(AdminClient client, String path, DomainData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    // --- data ----------------------------------------------------------------

    /**
     * Returns the domain identifier.
     *
     * @return the domain identifier
     */
    public String id() {
        return data.id();
    }

    /**
     * Returns the owning tenant identifier.
     *
     * @return the owning tenant identifier
     */
    public String tenantId() {
        return data.tenantId();
    }

    /**
     * Returns the domain name.
     *
     * @return the domain name
     */
    public String domainName() {
        return data.domainName();
    }

    /**
     * Returns the DNS ownership verification token.
     *
     * @return the DNS ownership verification token
     */
    public String verificationToken() {
        return data.verificationToken();
    }

    /**
     * Returns whether outbound sending is enabled.
     *
     * @return whether outbound sending is enabled
     */
    public boolean sendingEnabled() {
        return data.sendingEnabled();
    }

    /**
     * Returns the active DKIM selector.
     *
     * @return the active DKIM selector
     */
    public String dkimActiveSelector() {
        return data.dkimActiveSelector();
    }

    /**
     * Returns whether SPF is verified.
     *
     * @return whether SPF is verified
     */
    public boolean spfVerified() {
        return data.spfVerified();
    }

    /**
     * Returns whether DKIM is verified.
     *
     * @return whether DKIM is verified
     */
    public boolean dkimVerified() {
        return data.dkimVerified();
    }

    /**
     * Returns whether DMARC is verified.
     *
     * @return whether DMARC is verified
     */
    public boolean dmarcVerified() {
        return data.dmarcVerified();
    }

    /**
     * Returns the optional DMARC aggregate report mailbox.
     *
     * @return the optional DMARC aggregate report mailbox
     */
    public String dmarcReportAddress() {
        return data.dmarcReportAddress();
    }

    /**
     * Returns the timestamp of the last DNS check.
     *
     * @return the timestamp of the last DNS check
     */
    public String dnsLastCheckedAt() {
        return data.dnsLastCheckedAt();
    }

    /**
     * Returns the lifecycle status.
     *
     * @return the lifecycle status
     */
    public DomainStatus status() {
        return data.status();
    }

    /**
     * Returns the DNS records the customer must create.
     *
     * @return the DNS records the customer must create
     */
    public List<DnsInstructionRecord> requiredCustomerRecords() {
        return data.requiredCustomerRecords();
    }

    // --- operations ----------------------------------------------------------

    /**
     * Re-fetch this domain and return a fresh snapshot.
     *
     * @return a fresh domain snapshot
     * @throws IOException if the request fails or is interrupted
     */
    public Domain reload() throws IOException {
        return new Domain(client, path, client.get(path).as(DomainData.class), isDeleted());
    }

    /**
     * Replace the writable domain configuration; {@code null} clears the DMARC report address.
     *
     * @param dmarcReportAddress DMARC report address, or {@code null} to clear it
     * @return the updated domain
     * @throws IOException if the request fails or is interrupted
     */
    public Domain replace(String dmarcReportAddress) throws IOException {
        DomainData updated = client.put(path, new ReplaceDomainRequest(dmarcReportAddress))
                .as(DomainData.class);
        return new Domain(client, path, updated, isDeleted());
    }

    /**
     * Listeners authorized to send from this domain.
     *
     * @return a page of domain listener grants
     * @throws IOException if the request fails or is interrupted
     */
    public Paged<DomainListenerGrant> listenerGrants() throws IOException {
        return client.paged(path + "/listener-grants", null, DomainListenerGrant.class);
    }
}
