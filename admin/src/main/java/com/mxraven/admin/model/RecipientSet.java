package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import com.mxraven.admin.client.RecipientSetMembersClient;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Hydrated RecipientSet entity. Reads return a snapshot; call <code>reload()</code> for fresh state.
 */
public final class RecipientSet extends Entity<RecipientSetData> {
    public RecipientSet(AdminClient client, String path, RecipientSetData data) {
        super(client, path, data);
    }

    private RecipientSet(AdminClient client, String path, RecipientSetData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    public String id() {
        return data.id();
    }

    public String tenantId() {
        return data.tenantId();
    }

    public String setRef() {
        return data.setRef();
    }

    public String displayName() {
        return data.displayName();
    }

    public String description() {
        return data.description();
    }

    public String createdAt() {
        return data.createdAt();
    }

    public String updatedAt() {
        return data.updatedAt();
    }

    public RecipientSet reload() throws IOException {
        return new RecipientSet(client, path, client.get(path).as(RecipientSetData.class), isDeleted());
    }

    public RecipientSet update(Consumer<UpdateRecipientSetRequest.Builder> configure) throws IOException {
        UpdateRecipientSetRequest.Builder builder = UpdateRecipientSetRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    public RecipientSet update(UpdateRecipientSetRequest request) throws IOException {
        return new RecipientSet(client, path, client.put(path, request).as(RecipientSetData.class), isDeleted());
    }

    public RecipientSetMembersClient members() {
        return new RecipientSetMembersClient(client, path + "/members");
    }
}
