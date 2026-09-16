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

    /**
     * Sets the {@code q} search filter for set members.
     *
     * @param query search text; {@code null} or blank is ignored
     * @return this query
     * @throws IllegalArgumentException if the query is non-blank and shorter than
     *                                  {@value QueryParams#MIN_SEARCH_LENGTH} characters
     */
    public RecipientSetMemberQuery search(String query) {
        params.q(query);
        return this;
    }

    /**
     * Sets the inclusive lower bound as an ISO-8601 timestamp.
     *
     * @param addedFrom inclusive lower bound as an ISO-8601 timestamp
     * @return this query
     */
    public RecipientSetMemberQuery addedFrom(String addedFrom) {
        params.put("added_from", addedFrom);
        return this;
    }

    /**
     * Sets the exclusive upper bound as an ISO-8601 timestamp.
     *
     * @param addedTo exclusive upper bound as an ISO-8601 timestamp
     * @return this query
     */
    public RecipientSetMemberQuery addedTo(String addedTo) {
        params.put("added_to", addedTo);
        return this;
    }

    /**
     * Sets the maximum number of members per page.
     *
     * @param pageSize number of items per page, between 1 and 500
     * @return this query
     * @throws IllegalArgumentException if {@code pageSize} is outside the range 1 to 500
     */
    public RecipientSetMemberQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    /**
     * Sets the opaque page token to continue from.
     *
     * @param pageToken opaque page token; {@code null} is ignored
     * @return this query
     */
    public RecipientSetMemberQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    /**
     * Lists members matching the configured filters.
     *
     * @return a lazily paginated collection of set members
     * @throws IOException if the request fails or is interrupted
     */
    public Paged<RecipientSetMember> list() throws IOException {
        return client.list(params);
    }
}
