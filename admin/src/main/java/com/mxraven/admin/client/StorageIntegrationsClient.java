package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.CreateStorageIntegrationRequest;
import com.mxraven.admin.model.StorageIntegration;
import com.mxraven.admin.model.StorageIntegrationData;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Tenant object-storage integration collection. Reads and creation return
 * hydrated {@link StorageIntegration} entities.
 */
public final class StorageIntegrationsClient {
    private final AdminClient client;
    private final String tenantSlug;

    public StorageIntegrationsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    public Paged<StorageIntegration> list() throws IOException {
        return list(null);
    }

    Paged<StorageIntegration> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), StorageIntegrationData.class)
                .map(data -> new StorageIntegration(client, one(data.id()), data));
    }

    public StorageIntegration create(Consumer<CreateStorageIntegrationRequest.Builder> configure) throws IOException {
        CreateStorageIntegrationRequest.Builder builder = CreateStorageIntegrationRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    public StorageIntegration create(CreateStorageIntegrationRequest request) throws IOException {
        StorageIntegrationData data = client.post(base(), request).as(StorageIntegrationData.class);
        return new StorageIntegration(client, one(data.id()), data);
    }

    private String base() {
        return "/tenants/" + seg(tenantSlug) + "/storage-integrations";
    }

    private String one(String id) {
        return base() + "/" + seg(id);
    }

    private static String seg(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public StorageIntegration get(String integrationId) throws IOException {
        String resourcePath = one(integrationId);
        return new StorageIntegration(client, resourcePath, client.get(resourcePath).as(StorageIntegrationData.class));
    }

    /** Get a storage integration by its immutable {@code storage_ref}. */
    public StorageIntegration getByRef(String storageRef) throws IOException {
        String refPath = base() + "/ref/" + seg(storageRef);
        StorageIntegrationData data = client.get(refPath).as(StorageIntegrationData.class);
        return new StorageIntegration(client, one(data.id()), data);
    }
}
