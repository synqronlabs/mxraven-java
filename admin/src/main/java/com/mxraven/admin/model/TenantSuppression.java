package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import com.mxraven.admin.model.UpdateTenantSuppressionRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Hydrated TenantSuppression entity. Reads return a snapshot; call <code>reload()</code> for fresh state.
 */
public final class TenantSuppression extends Entity<TenantSuppressionData> {
    public TenantSuppression(AdminClient client, String path, TenantSuppressionData data) {
        super(client, path, data);
    }

    private TenantSuppression(AdminClient client, String path, TenantSuppressionData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    public String tenantId() {
        return data.tenantId();
    }

    public String emailAddress() {
        return data.emailAddress();
    }

    public SuppressionReason reason() {
        return data.reason();
    }

    public TenantSuppression reload() throws IOException {
        return new TenantSuppression(client, path, client.get(path).as(TenantSuppressionData.class), isDeleted());
    }

    public TenantSuppression update(SuppressionReason reason) throws IOException {
        return update(new UpdateTenantSuppressionRequest(reason));
    }

    public TenantSuppression update(Consumer<UpdateTenantSuppressionRequest.Builder> configure) throws IOException {
        UpdateTenantSuppressionRequest.Builder builder = UpdateTenantSuppressionRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    public TenantSuppression update(UpdateTenantSuppressionRequest request) throws IOException {
        return new TenantSuppression(client, path,
                client.put(path, request).as(TenantSuppressionData.class), isDeleted());
    }
}
