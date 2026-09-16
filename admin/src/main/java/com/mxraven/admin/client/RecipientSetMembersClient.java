package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.RecipientSetBatchResult;
import com.mxraven.admin.model.RecipientSetMember;
import com.mxraven.admin.model.RecipientSetMemberData;
import com.mxraven.admin.model.RecipientSetMemberRequest;
import com.mxraven.admin.model.RecipientSetMembersBatchRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * Membership of a single recipient set, obtained from
 * {@link com.mxraven.admin.model.RecipientSet#members()}.
 */
public final class RecipientSetMembersClient {
    private final AdminClient client;
    private final String path;

    /**
     * Creates a client for the given recipient-set membership path.
     *
     * @param client underlying admin client
     * @param path client-relative membership resource path
     */
    public RecipientSetMembersClient(AdminClient client, String path) {
        this.client = client;
        this.path = path;
    }

    /**
     * Starts a typed, fluent list request.
     *
     * @return a new member query
     */
    public RecipientSetMemberQuery query() {
        return new RecipientSetMemberQuery(this);
    }

    /**
     * Lists members of the recipient set.
     *
     * @return a lazily paginated collection of set members
     * @throws IOException if the request fails or is interrupted
     */
    public Paged<RecipientSetMember> list() throws IOException {
        return list(null);
    }

    Paged<RecipientSetMember> list(QueryParams params) throws IOException {
        return client.paged(path, params == null ? null : params.toMap(), RecipientSetMemberData.class)
                .map(data -> new RecipientSetMember(client, one(data.emailAddress()), data));
    }

    /**
     * Adds the given email address to the recipient set.
     *
     * @param emailAddress email address to add
     * @return the added member
     * @throws IOException if the request fails or is interrupted
     */
    public RecipientSetMember add(String emailAddress) throws IOException {
        return add(RecipientSetMemberRequest.builder().emailAddress(emailAddress).build());
    }

    /**
     * Adds a member configured through the given builder consumer.
     *
     * @param configure consumer that populates the member request builder
     * @return the added member
     * @throws IOException if the request fails or is interrupted
     */
    public RecipientSetMember add(Consumer<RecipientSetMemberRequest.Builder> configure) throws IOException {
        RecipientSetMemberRequest.Builder builder = RecipientSetMemberRequest.builder();
        configure.accept(builder);
        return add(builder.build());
    }

    /**
     * Adds a member described by the given request.
     *
     * @param request member creation request
     * @return the added member
     * @throws IOException if the request fails or is interrupted
     */
    public RecipientSetMember add(RecipientSetMemberRequest request) throws IOException {
        RecipientSetMemberData data = client.post(path, request).as(RecipientSetMemberData.class);
        return new RecipientSetMember(client, one(data.emailAddress()), data);
    }

    /**
     * Removes the given email address from the recipient set.
     *
     * @param emailAddress email address to remove
     * @throws IOException if the request fails or is interrupted
     */
    public void delete(String emailAddress) throws IOException {
        client.delete(one(emailAddress));
    }

    /**
     * Adds multiple email addresses in a single batch.
     *
     * @param emailAddresses email addresses to add
     * @return per-operation outcome counts
     * @throws IOException if the request fails or is interrupted
     */
    public RecipientSetBatchResult batchAdd(List<String> emailAddresses) throws IOException {
        return batchAdd(RecipientSetMembersBatchRequest.builder().emailAddresses(emailAddresses).build());
    }

    /**
     * Adds a batch of members configured through the given builder consumer.
     *
     * @param configure consumer that populates the batch request builder
     * @return per-operation outcome counts
     * @throws IOException if the request fails or is interrupted
     */
    public RecipientSetBatchResult batchAdd(Consumer<RecipientSetMembersBatchRequest.Builder> configure) throws IOException {
        RecipientSetMembersBatchRequest.Builder builder = RecipientSetMembersBatchRequest.builder();
        configure.accept(builder);
        return batchAdd(builder.build());
    }

    /**
     * Adds a batch of members described by the given request.
     *
     * @param request batch add request
     * @return per-operation outcome counts
     * @throws IOException if the request fails or is interrupted
     */
    public RecipientSetBatchResult batchAdd(RecipientSetMembersBatchRequest request) throws IOException {
        return client.post(path + ":batch-add", request).as(RecipientSetBatchResult.class);
    }

    /**
     * Removes multiple email addresses in a single batch.
     *
     * @param emailAddresses email addresses to remove
     * @return per-operation outcome counts
     * @throws IOException if the request fails or is interrupted
     */
    public RecipientSetBatchResult batchDelete(List<String> emailAddresses) throws IOException {
        return batchDelete(RecipientSetMembersBatchRequest.builder().emailAddresses(emailAddresses).build());
    }

    /**
     * Removes a batch of members configured through the given builder consumer.
     *
     * @param configure consumer that populates the batch request builder
     * @return per-operation outcome counts
     * @throws IOException if the request fails or is interrupted
     */
    public RecipientSetBatchResult batchDelete(Consumer<RecipientSetMembersBatchRequest.Builder> configure) throws IOException {
        RecipientSetMembersBatchRequest.Builder builder = RecipientSetMembersBatchRequest.builder();
        configure.accept(builder);
        return batchDelete(builder.build());
    }

    /**
     * Removes a batch of members described by the given request.
     *
     * @param request batch delete request
     * @return per-operation outcome counts
     * @throws IOException if the request fails or is interrupted
     */
    public RecipientSetBatchResult batchDelete(RecipientSetMembersBatchRequest request) throws IOException {
        return client.post(path + ":batch-delete", request).as(RecipientSetBatchResult.class);
    }

    private String one(String emailAddress) {
        return path + "/" + encode(emailAddress);
    }

    private static String encode(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8);
    }
}
