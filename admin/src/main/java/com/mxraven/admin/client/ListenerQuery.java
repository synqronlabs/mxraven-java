package com.mxraven.admin.client;

import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.TerminalActionType;
import com.mxraven.admin.model.Listener;
import com.mxraven.admin.model.StreamType;
import com.mxraven.admin.model.ListenerType;

import java.io.IOException;

/**
 * Typed filter builder for {@link ListenersClient#query()}.
 */
public final class ListenerQuery {
    private final ListenersClient client;
    private final QueryParams params = QueryParams.create();

    ListenerQuery(ListenersClient client) {
        this.client = client;
    }

    /**
     * Set the {@code q} search filter.
     *
     * @param query search text
     * @return this query
     */
    public ListenerQuery search(String query) {
        params.q(query);
        return this;
    }

    /**
     * Filter by listener type.
     *
     * @param type listener type; ignored when {@code null}
     * @return this query
     */
    public ListenerQuery listenerType(ListenerType type) {
        params.put("listener_type", type == null ? null : type.wire());
        return this;
    }

    /**
     * Filter by stream type.
     *
     * @param type stream type; ignored when {@code null}
     * @return this query
     */
    public ListenerQuery streamType(StreamType type) {
        params.put("stream_type", type == null ? null : type.wire());
        return this;
    }

    /**
     * Filter by default terminal action type.
     *
     * @param type terminal action type; ignored when {@code null}
     * @return this query
     */
    public ListenerQuery defaultTerminalActionType(TerminalActionType type) {
        params.put("default_terminal_action_type", type == null ? null : type.wire());
        return this;
    }

    /**
     * Filter by whether Rspamd scanning is enabled.
     *
     * @param enabled required Rspamd scanning state
     * @return this query
     */
    public ListenerQuery rspamdScanningEnabled(boolean enabled) {
        params.put("rspamd_scanning_enabled", enabled);
        return this;
    }

    /**
     * Set the maximum number of listeners per page.
     *
     * @param pageSize page size, between 1 and 500
     * @return this query
     */
    public ListenerQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    /**
     * Continue from a previously returned page.
     *
     * @param pageToken opaque page token; {@code null} is ignored
     * @return this query
     */
    public ListenerQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    /**
     * Fetch the matching listeners (first page fetched eagerly, remaining pages
     * lazy).
     *
     * @return a lazily paginating collection of listeners
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<Listener> list() throws IOException {
        return client.list(params);
    }
}
