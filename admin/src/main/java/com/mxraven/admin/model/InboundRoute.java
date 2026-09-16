package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Hydrated InboundRoute entity. Reads return a snapshot; call <code>reload()</code> for fresh state.
 */
public final class InboundRoute extends Entity<InboundRouteData> {
    /**
     * Creates a hydrated inbound route from wire data.
     *
     * @param client the client used to issue requests
     * @param path client-relative path of the route
     * @param data the wire record backing this entity
     */
    public InboundRoute(AdminClient client, String path, InboundRouteData data) {
        super(client, path, data);
    }

    private InboundRoute(AdminClient client, String path, InboundRouteData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    /**
     * Returns the unique inbound-route identifier.
     *
     * @return the route identifier
     */
    public String id() {
        return data.id();
    }

    /**
     * Returns the identifier of the owning tenant.
     *
     * @return the tenant identifier
     */
    public String tenantId() {
        return data.tenantId();
    }

    /**
     * Returns the identifier of the MTA listener that receives routed mail.
     *
     * @return the MTA listener identifier
     */
    public String mtaListenerId() {
        return data.mtaListenerId();
    }

    /**
     * Returns the inbound domain name.
     *
     * @return the domain name
     */
    public String domainName() {
        return data.domainName();
    }

    /**
     * Returns whether the route is fully verified.
     *
     * @return {@code true} when the route is verified
     */
    public boolean isVerified() {
        return data.isVerified();
    }

    /**
     * Returns the current DNS verification status.
     *
     * @return the verification status
     */
    public InboundRouteVerificationStatus verificationStatus() {
        return data.verificationStatus();
    }

    /**
     * Returns whether the TXT ownership record is verified.
     *
     * @return {@code true} when the TXT record is verified
     */
    public boolean txtVerified() {
        return data.txtVerified();
    }

    /**
     * Returns whether the MX record is verified.
     *
     * @return {@code true} when the MX record is verified
     */
    public boolean mxVerified() {
        return data.mxVerified();
    }

    /**
     * Returns when DNS was last checked.
     *
     * @return the timestamp of the last DNS check
     */
    public String dnsLastCheckedAt() {
        return data.dnsLastCheckedAt();
    }

    /**
     * Returns the token expected in the TXT ownership record.
     *
     * @return the verification token
     */
    public String verificationToken() {
        return data.verificationToken();
    }

    /**
     * Returns the DNS records the customer must create.
     *
     * @return the required customer DNS records
     */
    public List<DnsInstructionRecord> requiredCustomerRecords() {
        return data.requiredCustomerRecords();
    }

    /**
     * Reloads the route from the server.
     *
     * @return a fresh route snapshot
     * @throws IOException if the request fails or is interrupted
     */
    public InboundRoute reload() throws IOException {
        return new InboundRoute(client, path, client.get(path).as(InboundRouteData.class), isDeleted());
    }

    /**
     * Updates the route's MTA listener and domain name.
     *
     * @param mtaListenerId the new MTA listener identifier
     * @param domainName the new inbound domain name
     * @return the updated route
     * @throws IOException if the request fails or is interrupted
     */
    public InboundRoute update(String mtaListenerId, String domainName) throws IOException {
        return update(new UpdateInboundRouteRequest(mtaListenerId, domainName));
    }

    /**
     * Updates the route using a request builder.
     *
     * @param configure callback that configures the update request builder
     * @return the updated route
     * @throws IOException if the request fails or is interrupted
     */
    public InboundRoute update(Consumer<UpdateInboundRouteRequest.Builder> configure) throws IOException {
        UpdateInboundRouteRequest.Builder builder = UpdateInboundRouteRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    /**
     * Updates the route with a prepared request.
     *
     * @param request the update request
     * @return the updated route
     * @throws IOException if the request fails or is interrupted
     */
    public InboundRoute update(UpdateInboundRouteRequest request) throws IOException {
        return new InboundRoute(client, path, client.put(path, request).as(InboundRouteData.class), isDeleted());
    }
}
