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
    public StorageIntegration(AdminClient client, String path, StorageIntegrationData data) {
        super(client, path, data);
    }

    private StorageIntegration(AdminClient client, String path, StorageIntegrationData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    public String id() {
        return data.id();
    }

    public String tenantId() {
        return data.tenantId();
    }

    public String storageRef() {
        return data.storageRef();
    }

    public String displayName() {
        return data.displayName();
    }

    public boolean isActive() {
        return data.isActive();
    }

    public boolean credentialsPresent() {
        return data.credentialsPresent();
    }

    public StorageIntegration reload() throws IOException {
        return new StorageIntegration(client, path,
                client.get(path).as(StorageIntegrationData.class), isDeleted());
    }

    public StorageIntegration update(Consumer<UpdateStorageIntegrationRequest.Builder> configure) throws IOException {
        UpdateStorageIntegrationRequest.Builder builder = UpdateStorageIntegrationRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    public StorageIntegration update(UpdateStorageIntegrationRequest request) throws IOException {
        return new StorageIntegration(client, path,
                client.put(path, request).as(StorageIntegrationData.class), isDeleted());
    }

    public StorageIntegration setActive(boolean isActive) throws IOException {
        return setActive(new SetIntegrationActiveRequest(isActive));
    }

    public StorageIntegration setActive(Consumer<SetIntegrationActiveRequest.Builder> configure) throws IOException {
        SetIntegrationActiveRequest.Builder builder = SetIntegrationActiveRequest.builder();
        configure.accept(builder);
        return setActive(builder.build());
    }

    public StorageIntegration setActive(SetIntegrationActiveRequest request) throws IOException {
        return new StorageIntegration(client, path,
                client.put(path + "/active", request).as(StorageIntegrationData.class), isDeleted());
    }
}
