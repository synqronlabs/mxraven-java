package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Hydrated StorageIntegration entity. Reads return a snapshot; call <code>reload()</code> for fresh state.
 */
public final class StorageIntegration extends Entity<StorageIntegrationData> {
    /**
     * Creates an integration bound to the given client, path, and data.
     *
     * @param client the admin client
     * @param path   the resource path
     * @param data   the backing data
     */
    public StorageIntegration(AdminClient client, String path, StorageIntegrationData data) {
        super(client, path, data);
    }

    private StorageIntegration(AdminClient client, String path, StorageIntegrationData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    /**
     * Returns the entity identifier.
     *
     * @return the entity identifier
     */
    public String id() {
        return data.id();
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
     * Returns the stable reference for the integration.
     *
     * @return the integration reference
     */
    public String storageRef() {
        return data.storageRef();
    }

    /**
     * Returns the human-readable integration name.
     *
     * @return the integration name
     */
    public String displayName() {
        return data.displayName();
    }

    /**
     * Returns whether the integration is active.
     *
     * @return {@code true} if the integration is active
     */
    public boolean isActive() {
        return data.isActive();
    }

    /**
     * Returns whether storage credentials are stored.
     *
     * @return {@code true} if credentials are stored
     */
    public boolean credentialsPresent() {
        return data.credentialsPresent();
    }

    /**
     * Re-fetches this integration and returns a fresh snapshot.
     *
     * @return a fresh integration snapshot
     * @throws IOException if the request fails
     */
    public StorageIntegration reload() throws IOException {
        return new StorageIntegration(client, path,
                client.get(path).as(StorageIntegrationData.class), isDeleted());
    }

    /**
     * Replaces this integration's configuration using a builder.
     *
     * @param configure callback that configures the update request
     * @return the updated integration
     * @throws IOException if the request fails
     */
    public StorageIntegration update(Consumer<UpdateStorageIntegrationRequest.Builder> configure) throws IOException {
        UpdateStorageIntegrationRequest.Builder builder = UpdateStorageIntegrationRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    /**
     * Replaces this integration's configuration.
     *
     * @param request replacement request
     * @return the updated integration
     * @throws IOException if the request fails
     */
    public StorageIntegration update(UpdateStorageIntegrationRequest request) throws IOException {
        return new StorageIntegration(client, path,
                client.put(path, request).as(StorageIntegrationData.class), isDeleted());
    }

    /**
     * Enables or disables the integration.
     *
     * @param isActive whether the integration is active
     * @return the updated integration
     * @throws IOException if the request fails
     */
    public StorageIntegration setActive(boolean isActive) throws IOException {
        return setActive(new SetIntegrationActiveRequest(isActive));
    }

    /**
     * Enables or disables the integration using a builder.
     *
     * @param configure callback that configures the active-state request
     * @return the updated integration
     * @throws IOException if the request fails
     */
    public StorageIntegration setActive(Consumer<SetIntegrationActiveRequest.Builder> configure) throws IOException {
        SetIntegrationActiveRequest.Builder builder = SetIntegrationActiveRequest.builder();
        configure.accept(builder);
        return setActive(builder.build());
    }

    /**
     * Sets the integration active state.
     *
     * @param request active-state request
     * @return the updated integration
     * @throws IOException if the request fails
     */
    public StorageIntegration setActive(SetIntegrationActiveRequest request) throws IOException {
        return new StorageIntegration(client, path,
                client.put(path + "/active", request).as(StorageIntegrationData.class), isDeleted());
    }
}
