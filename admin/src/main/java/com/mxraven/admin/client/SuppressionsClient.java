package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.CreateTenantSuppressionRequest;
import com.mxraven.admin.model.TenantSuppression;
import com.mxraven.admin.model.TenantSuppressionData;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Tenant suppression collection. Reads and creation return hydrated
 * {@link TenantSuppression} entities keyed by email address.
 */
public final class SuppressionsClient {
    private final AdminClient client;
    private final String tenantSlug;

    public SuppressionsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /** Start a typed, fluent list request. */
    public SuppressionQuery query() {
        return new SuppressionQuery(this);
    }

    public Paged<TenantSuppression> list() throws IOException {
        return list(null);
    }

    Paged<TenantSuppression> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), TenantSuppressionData.class)
                .map(data -> new TenantSuppression(client, one(data.emailAddress()), data));
    }

    public TenantSuppression create(Consumer<CreateTenantSuppressionRequest.Builder> configure) throws IOException {
        CreateTenantSuppressionRequest.Builder builder = CreateTenantSuppressionRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    public TenantSuppression create(CreateTenantSuppressionRequest request) throws IOException {
        TenantSuppressionData data = client.post(base(), request).as(TenantSuppressionData.class);
        return new TenantSuppression(client, one(data.emailAddress()), data);
    }

    private String base() {
        return "/tenants/" + tenantSlug + "/suppressions";
    }

    private String one(String emailAddress) {
        return base() + "/" + encode(emailAddress);
    }

    private static String encode(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8);
    }

    public TenantSuppression get(String emailAddress) throws IOException {
        String resourcePath = one(emailAddress);
        return new TenantSuppression(client, resourcePath, client.get(resourcePath).as(TenantSuppressionData.class));
    }
}
