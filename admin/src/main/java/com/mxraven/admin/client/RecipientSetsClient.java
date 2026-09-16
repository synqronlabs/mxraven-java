package com.mxraven.admin.client;

import com.mxraven.admin.internal.Java8;

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

    /**
     * Creates a client bound to the given tenant.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug
     */
    public RecipientSetsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * Starts a typed, fluent list request.
     *
     * @return a new recipient-set query
     */
    public RecipientSetQuery query() {
        return new RecipientSetQuery(this);
    }

    /**
     * Lists recipient sets for the tenant.
     *
     * @return a lazily paginated collection of recipient sets
     * @throws IOException if the request fails or is interrupted
     */
    public Paged<RecipientSet> list() throws IOException {
        return list(null);
    }

    Paged<RecipientSet> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), RecipientSetData.class)
                .map(data -> new RecipientSet(client, one(data.setRef()), data));
    }

    /**
     * Creates a recipient set configured through the given builder consumer.
     *
     * @param configure consumer that populates the creation request builder
     * @return the created recipient set
     * @throws IOException if the request fails or is interrupted
     */
    public RecipientSet create(Consumer<CreateRecipientSetRequest.Builder> configure) throws IOException {
        CreateRecipientSetRequest.Builder builder = CreateRecipientSetRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    /**
     * Creates a recipient set described by the given request.
     *
     * @param request recipient-set creation request
     * @return the created recipient set
     * @throws IOException if the request fails or is interrupted
     */
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
        return Java8.urlEncode(segment);
    }

    /**
     * Gets a recipient set by its immutable {@code set_ref}.
     *
     * @param setRef immutable recipient-set reference
     * @return the recipient set
     * @throws IOException if the request fails or is interrupted
     */
    public RecipientSet getByRef(String setRef) throws IOException {
        String resourcePath = one(setRef);
        return new RecipientSet(client, resourcePath, client.get(resourcePath).as(RecipientSetData.class));
    }
}
