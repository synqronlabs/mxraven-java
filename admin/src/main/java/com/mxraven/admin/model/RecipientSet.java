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
    /**
     * Creates a recipient-set entity bound to a client, path, and backing data.
     *
     * @param client admin client used for requests
     * @param path resource path
     * @param data backing recipient-set data
     */
    public RecipientSet(AdminClient client, String path, RecipientSetData data) {
        super(client, path, data);
    }

    private RecipientSet(AdminClient client, String path, RecipientSetData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    /**
     * Returns the recipient set identifier.
     *
     * @return the recipient set identifier
     */
    public String id() {
        return data.id();
    }

    /**
     * Returns the owning tenant identifier.
     *
     * @return the owning tenant identifier
     */
    public String tenantId() {
        return data.tenantId();
    }

    /**
     * Returns the stable set reference.
     *
     * @return the stable set reference
     */
    public String setRef() {
        return data.setRef();
    }

    /**
     * Returns the display name.
     *
     * @return the display name
     */
    public String displayName() {
        return data.displayName();
    }

    /**
     * Returns the description.
     *
     * @return the description
     */
    public String description() {
        return data.description();
    }

    /**
     * Returns the creation timestamp.
     *
     * @return the creation timestamp
     */
    public String createdAt() {
        return data.createdAt();
    }

    /**
     * Returns the last update timestamp.
     *
     * @return the last update timestamp
     */
    public String updatedAt() {
        return data.updatedAt();
    }

    /**
     * Reloads the recipient set from the server.
     *
     * @return a fresh snapshot
     * @throws IOException if the request fails
     */
    public RecipientSet reload() throws IOException {
        return new RecipientSet(client, path, client.get(path).as(RecipientSetData.class), isDeleted());
    }

    /**
     * Updates the recipient set using the configured builder.
     *
     * @param configure configures the update request
     * @return the updated recipient set
     * @throws IOException if the request fails
     */
    public RecipientSet update(Consumer<UpdateRecipientSetRequest.Builder> configure) throws IOException {
        UpdateRecipientSetRequest.Builder builder = UpdateRecipientSetRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    /**
     * Updates the recipient set with a full request.
     *
     * @param request update request
     * @return the updated recipient set
     * @throws IOException if the request fails
     */
    public RecipientSet update(UpdateRecipientSetRequest request) throws IOException {
        return new RecipientSet(client, path, client.put(path, request).as(RecipientSetData.class), isDeleted());
    }

    /**
     * Returns a client for the set's members.
     *
     * @return a client for the set's members
     */
    public RecipientSetMembersClient members() {
        return new RecipientSetMembersClient(client, path + "/members");
    }
}
