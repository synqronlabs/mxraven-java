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
    /**
     * Creates a suppression bound to the given client, path, and data.
     *
     * @param client the admin client
     * @param path   the resource path
     * @param data   the backing data
     */
    public TenantSuppression(AdminClient client, String path, TenantSuppressionData data) {
        super(client, path, data);
    }

    private TenantSuppression(AdminClient client, String path, TenantSuppressionData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    /**
     * Returns the identifier of the owning tenant.
     *
     * @return the owning tenant identifier
     */
    public String tenantId() {
        return data.tenantId();
    }

    /**
     * Returns the suppressed email address.
     *
     * @return the suppressed email address
     */
    public String emailAddress() {
        return data.emailAddress();
    }

    /**
     * Returns the reason the address is suppressed.
     *
     * @return the suppression reason
     */
    public SuppressionReason reason() {
        return data.reason();
    }

    /**
     * Re-fetches this suppression and returns a fresh snapshot.
     *
     * @return a fresh suppression snapshot
     * @throws IOException if the request fails
     */
    public TenantSuppression reload() throws IOException {
        return new TenantSuppression(client, path, client.get(path).as(TenantSuppressionData.class), isDeleted());
    }

    /**
     * Replaces the suppression reason.
     *
     * @param reason replacement suppression reason; may be null
     * @return the updated suppression
     * @throws IOException if the request fails
     */
    public TenantSuppression update(SuppressionReason reason) throws IOException {
        return update(new UpdateTenantSuppressionRequest(reason));
    }

    /**
     * Replaces the suppression reason using a builder.
     *
     * @param configure callback that configures the update request
     * @return the updated suppression
     * @throws IOException if the request fails
     */
    public TenantSuppression update(Consumer<UpdateTenantSuppressionRequest.Builder> configure) throws IOException {
        UpdateTenantSuppressionRequest.Builder builder = UpdateTenantSuppressionRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    /**
     * Replaces the suppression reason.
     *
     * @param request replacement request
     * @return the updated suppression
     * @throws IOException if the request fails
     */
    public TenantSuppression update(UpdateTenantSuppressionRequest request) throws IOException {
        return new TenantSuppression(client, path,
                client.put(path, request).as(TenantSuppressionData.class), isDeleted());
    }
}
