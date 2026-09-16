package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import java.util.List;
import java.util.Map;

/**
 * Hydrated RecipientSetMember entity. Reads return a snapshot; call <code>reload()</code> for fresh state.
 */
public final class RecipientSetMember extends Entity<RecipientSetMemberData> {
    /**
     * Creates a recipient-set-member entity bound to a client, path, and backing data.
     *
     * @param client admin client used for requests
     * @param path resource path
     * @param data backing member data
     */
    public RecipientSetMember(AdminClient client, String path, RecipientSetMemberData data) {
        super(client, path, data);
    }

    private RecipientSetMember(AdminClient client, String path, RecipientSetMemberData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    /**
     * Returns the member email address.
     *
     * @return the member email address
     */
    public String emailAddress() {
        return data.emailAddress();
    }

    /**
     * Returns the time the member was added.
     *
     * @return the time the member was added
     */
    public String addedAt() {
        return data.addedAt();
    }

}
