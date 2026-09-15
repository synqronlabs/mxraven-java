package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import java.util.List;
import java.util.Map;

/**
 * Hydrated RecipientSetMember entity. Reads return a snapshot; call <code>reload()</code> for fresh state.
 */
public final class RecipientSetMember extends Entity<RecipientSetMemberData> {
    public RecipientSetMember(AdminClient client, String path, RecipientSetMemberData data) {
        super(client, path, data);
    }

    private RecipientSetMember(AdminClient client, String path, RecipientSetMemberData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    public String emailAddress() {
        return data.emailAddress();
    }

    public String addedAt() {
        return data.addedAt();
    }

}
