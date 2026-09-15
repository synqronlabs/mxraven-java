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
    public InboundRoute(AdminClient client, String path, InboundRouteData data) {
        super(client, path, data);
    }

    private InboundRoute(AdminClient client, String path, InboundRouteData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    public String id() {
        return data.id();
    }

    public String tenantId() {
        return data.tenantId();
    }

    public String mtaListenerId() {
        return data.mtaListenerId();
    }

    public String domainName() {
        return data.domainName();
    }

    public boolean isVerified() {
        return data.isVerified();
    }

    public InboundRouteVerificationStatus verificationStatus() {
        return data.verificationStatus();
    }

    public boolean txtVerified() {
        return data.txtVerified();
    }

    public boolean mxVerified() {
        return data.mxVerified();
    }

    public String dnsLastCheckedAt() {
        return data.dnsLastCheckedAt();
    }

    public String verificationToken() {
        return data.verificationToken();
    }

    public List<DnsInstructionRecord> requiredCustomerRecords() {
        return data.requiredCustomerRecords();
    }

    public InboundRoute reload() throws IOException {
        return new InboundRoute(client, path, client.get(path).as(InboundRouteData.class), isDeleted());
    }

    public InboundRoute update(String mtaListenerId, String domainName) throws IOException {
        return update(new UpdateInboundRouteRequest(mtaListenerId, domainName));
    }

    public InboundRoute update(Consumer<UpdateInboundRouteRequest.Builder> configure) throws IOException {
        UpdateInboundRouteRequest.Builder builder = UpdateInboundRouteRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    public InboundRoute update(UpdateInboundRouteRequest request) throws IOException {
        return new InboundRoute(client, path, client.put(path, request).as(InboundRouteData.class), isDeleted());
    }
}
