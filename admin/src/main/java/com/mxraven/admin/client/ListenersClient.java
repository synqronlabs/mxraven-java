package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.CreateListenerRequest;
import com.mxraven.admin.model.Listener;
import com.mxraven.admin.model.ListenerData;
import com.mxraven.admin.model.ListenerType;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Submission and MTA listener collection. Reads and creation return hydrated
 * {@link Listener} entities whose operations and children act on that listener.
 */
public final class ListenersClient {
    private final AdminClient client;
    private final String tenantSlug;

    /**
     * Create a client for the listener collection of a tenant.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug whose listeners are accessed
     */
    public ListenersClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * Start a typed, fluent list request.
     *
     * @return a new listener query
     */
    public ListenerQuery query() {
        return new ListenerQuery(this);
    }

    /**
     * List all listeners (first page fetched eagerly, remaining pages lazy).
     *
     * @return a lazily paginating collection of listeners
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<Listener> list() throws IOException {
        return list(null);
    }

    Paged<Listener> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), ListenerData.class)
                .map(data -> new Listener(client, base() + "/" + data.id(), data));
    }

    /**
     * Get a listener by its identifier.
     *
     * @param listenerId listener identifier
     * @return the hydrated listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener get(String listenerId) throws IOException {
        String resourcePath = base() + "/" + listenerId;
        return new Listener(client, resourcePath, client.get(resourcePath).as(ListenerData.class));
    }

    /**
     * Create a listener from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener create(Consumer<CreateListenerRequest.Builder> configure) throws IOException {
        CreateListenerRequest.Builder builder = CreateListenerRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    /**
     * Create a listener.
     *
     * @param request creation request
     * @return the created listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener create(CreateListenerRequest request) throws IOException {
        ListenerData data = client.post(base(), request).as(ListenerData.class);
        return new Listener(client, base() + "/" + data.id(), data);
    }

    /**
     * Fetch every MTA listener, following cursor pages.
     *
     * @return all MTA listeners
     * @throws IOException if a page request fails or is interrupted
     */
    public List<Listener> listMtaListeners() throws IOException {
        return listByType(ListenerType.MTA);
    }

    /**
     * Fetch every submission listener, following cursor pages.
     *
     * @return all submission listeners
     * @throws IOException if a page request fails or is interrupted
     */
    public List<Listener> listSubmissionListeners() throws IOException {
        return listByType(ListenerType.SUBMISSION);
    }

    private List<Listener> listByType(ListenerType listenerType) throws IOException {
        QueryParams params = QueryParams.create().put("listener_type", listenerType.wire());
        return list(params).toList();
    }

    private String base() {
        return "/tenants/" + tenantSlug + "/listeners";
    }
}
