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

    public ListenerQuery search(String query) {
        params.q(query);
        return this;
    }

    public ListenerQuery listenerType(ListenerType type) {
        params.put("listener_type", type == null ? null : type.wire());
        return this;
    }

    public ListenerQuery streamType(StreamType type) {
        params.put("stream_type", type == null ? null : type.wire());
        return this;
    }

    public ListenerQuery defaultTerminalActionType(TerminalActionType type) {
        params.put("default_terminal_action_type", type == null ? null : type.wire());
        return this;
    }

    public ListenerQuery rspamdScanningEnabled(boolean enabled) {
        params.put("rspamd_scanning_enabled", enabled);
        return this;
    }

    public ListenerQuery pageSize(int pageSize) {
        params.pageSize(pageSize);
        return this;
    }

    public ListenerQuery pageToken(String pageToken) {
        params.pageToken(pageToken);
        return this;
    }

    public Paged<Listener> list() throws IOException {
        return client.list(params);
    }
}
