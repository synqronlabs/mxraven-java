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

    public ListenersClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /** Start a typed, fluent list request. */
    public ListenerQuery query() {
        return new ListenerQuery(this);
    }

    public Paged<Listener> list() throws IOException {
        return list(null);
    }

    Paged<Listener> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), ListenerData.class)
                .map(data -> new Listener(client, base() + "/" + data.id(), data));
    }

    public Listener get(String listenerId) throws IOException {
        String resourcePath = base() + "/" + listenerId;
        return new Listener(client, resourcePath, client.get(resourcePath).as(ListenerData.class));
    }

    public Listener create(Consumer<CreateListenerRequest.Builder> configure) throws IOException {
        CreateListenerRequest.Builder builder = CreateListenerRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    public Listener create(CreateListenerRequest request) throws IOException {
        ListenerData data = client.post(base(), request).as(ListenerData.class);
        return new Listener(client, base() + "/" + data.id(), data);
    }

    /** Fetch every MTA listener, following cursor pages. */
    public List<Listener> listMtaListeners() throws IOException {
        return listByType(ListenerType.MTA);
    }

    /** Fetch every submission listener, following cursor pages. */
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
