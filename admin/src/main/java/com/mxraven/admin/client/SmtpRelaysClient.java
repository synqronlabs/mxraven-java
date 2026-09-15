package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.CreateSmtpRelayRequest;
import com.mxraven.admin.model.SmtpRelay;
import com.mxraven.admin.model.SmtpRelayData;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Tenant SMTP relay collection. Reads and creation return hydrated
 * {@link SmtpRelay} entities.
 */
public final class SmtpRelaysClient {
    private final AdminClient client;
    private final String tenantSlug;

    public SmtpRelaysClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    public Paged<SmtpRelay> list() throws IOException {
        return list(null);
    }

    Paged<SmtpRelay> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), SmtpRelayData.class)
                .map(data -> new SmtpRelay(client, one(data.id()), data));
    }

    public SmtpRelay create(Consumer<CreateSmtpRelayRequest.Builder> configure) throws IOException {
        CreateSmtpRelayRequest.Builder builder = CreateSmtpRelayRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    public SmtpRelay create(CreateSmtpRelayRequest request) throws IOException {
        SmtpRelayData data = client.post(base(), request).as(SmtpRelayData.class);
        return new SmtpRelay(client, one(data.id()), data);
    }

    private String base() {
        return "/tenants/" + seg(tenantSlug) + "/smtp-relays";
    }

    private String one(String id) {
        return base() + "/" + seg(id);
    }

    private static String seg(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public SmtpRelay get(String relayId) throws IOException {
        String resourcePath = one(relayId);
        return new SmtpRelay(client, resourcePath, client.get(resourcePath).as(SmtpRelayData.class));
    }

    /** Get an SMTP relay by its immutable {@code relay_ref}. */
    public SmtpRelay getByRef(String relayRef) throws IOException {
        String refPath = base() + "/ref/" + seg(relayRef);
        SmtpRelayData data = client.get(refPath).as(SmtpRelayData.class);
        return new SmtpRelay(client, one(data.id()), data);
    }
}
