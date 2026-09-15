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

    public RecipientSetMembersClient(AdminClient client, String path) {
        this.client = client;
        this.path = path;
    }

    /** Start a typed, fluent list request. */
    public RecipientSetMemberQuery query() {
        return new RecipientSetMemberQuery(this);
    }

    public Paged<RecipientSetMember> list() throws IOException {
        return list(null);
    }

    Paged<RecipientSetMember> list(QueryParams params) throws IOException {
        return client.paged(path, params == null ? null : params.toMap(), RecipientSetMemberData.class)
                .map(data -> new RecipientSetMember(client, one(data.emailAddress()), data));
    }

    public RecipientSetMember add(String emailAddress) throws IOException {
        return add(RecipientSetMemberRequest.builder().emailAddress(emailAddress).build());
    }

    public RecipientSetMember add(Consumer<RecipientSetMemberRequest.Builder> configure) throws IOException {
        RecipientSetMemberRequest.Builder builder = RecipientSetMemberRequest.builder();
        configure.accept(builder);
        return add(builder.build());
    }

    public RecipientSetMember add(RecipientSetMemberRequest request) throws IOException {
        RecipientSetMemberData data = client.post(path, request).as(RecipientSetMemberData.class);
        return new RecipientSetMember(client, one(data.emailAddress()), data);
    }

    public void delete(String emailAddress) throws IOException {
        client.delete(one(emailAddress));
    }

    public RecipientSetBatchResult batchAdd(List<String> emailAddresses) throws IOException {
        return batchAdd(RecipientSetMembersBatchRequest.builder().emailAddresses(emailAddresses).build());
    }

    public RecipientSetBatchResult batchAdd(Consumer<RecipientSetMembersBatchRequest.Builder> configure) throws IOException {
        RecipientSetMembersBatchRequest.Builder builder = RecipientSetMembersBatchRequest.builder();
        configure.accept(builder);
        return batchAdd(builder.build());
    }

    public RecipientSetBatchResult batchAdd(RecipientSetMembersBatchRequest request) throws IOException {
        return client.post(path + ":batch-add", request).as(RecipientSetBatchResult.class);
    }

    public RecipientSetBatchResult batchDelete(List<String> emailAddresses) throws IOException {
        return batchDelete(RecipientSetMembersBatchRequest.builder().emailAddresses(emailAddresses).build());
    }

    public RecipientSetBatchResult batchDelete(Consumer<RecipientSetMembersBatchRequest.Builder> configure) throws IOException {
        RecipientSetMembersBatchRequest.Builder builder = RecipientSetMembersBatchRequest.builder();
        configure.accept(builder);
        return batchDelete(builder.build());
    }

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
