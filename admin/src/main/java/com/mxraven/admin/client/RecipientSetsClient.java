package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.CreateRecipientSetRequest;
import com.mxraven.admin.model.RecipientSet;
import com.mxraven.admin.model.RecipientSetData;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Tenant recipient-set collection. Reads and creation return hydrated
 * {@link RecipientSet} entities.
 */
public final class RecipientSetsClient {
    private final AdminClient client;
    private final String tenantSlug;

    public RecipientSetsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /** Start a typed, fluent list request. */
    public RecipientSetQuery query() {
        return new RecipientSetQuery(this);
    }

    public Paged<RecipientSet> list() throws IOException {
        return list(null);
    }

    Paged<RecipientSet> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), RecipientSetData.class)
                .map(data -> new RecipientSet(client, one(data.setRef()), data));
    }

    public RecipientSet create(Consumer<CreateRecipientSetRequest.Builder> configure) throws IOException {
        CreateRecipientSetRequest.Builder builder = CreateRecipientSetRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    public RecipientSet create(CreateRecipientSetRequest request) throws IOException {
        RecipientSetData data = client.post(base(), request).as(RecipientSetData.class);
        return new RecipientSet(client, one(data.setRef()), data);
    }

    private String base() {
        return "/tenants/" + tenantSlug + "/recipient-sets";
    }

    private String one(String setRef) {
        return base() + "/" + encode(setRef);
    }

    private static String encode(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8);
    }

    /** Get a recipient set by its immutable {@code set_ref}. */
    public RecipientSet getByRef(String setRef) throws IOException {
        String resourcePath = one(setRef);
        return new RecipientSet(client, resourcePath, client.get(resourcePath).as(RecipientSetData.class));
    }
}
