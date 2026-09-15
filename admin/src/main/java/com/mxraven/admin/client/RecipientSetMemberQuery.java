package com.mxraven.admin.client;

import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.RecipientSetMember;

import java.io.IOException;

/**
 * Typed filter builder for {@link RecipientSetMembersClient#query()}.
 */
public final class RecipientSetMemberQuery {
    private final RecipientSetMembersClient client;
    private final QueryParams params = QueryParams.create();

    RecipientSetMemberQuery(RecipientSetMembersClient client) {
        this.client = client;
    }

    public RecipientSetMemberQuery search(String query) {
        params.q(query);
        return this;
    }

    /** Inclusive lower bound as an ISO-8601 timestamp. */
    public RecipientSetMemberQuery addedFrom(String addedFrom) {
        params.put("added_from", addedFrom);
        return this;
    }

    /** Exclusive upper bound as an ISO-8601 timestamp. */
    public RecipientSetMemberQuery addedTo(String addedTo) {
        params.put("added_to", addedTo);
        return this;
    }

    public RecipientSetMemberQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    public RecipientSetMemberQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    public Paged<RecipientSetMember> list() throws IOException {
        return client.list(params);
    }
}
