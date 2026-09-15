package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import com.mxraven.admin.client.ApiKeysClient;
import com.mxraven.admin.client.RoutingRulesClient;
import com.mxraven.admin.client.SendingDomainPoliciesClient;
import com.mxraven.admin.client.ListenerMtaRateLimitClient;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * A submission or MTA listener.
 *
 * <p>Listener type, stream, default action, and scanning settings are immutable
 * through the plain update; use the dedicated operations. Reach the listener's
 * strictly-owned children through {@link #routingRules()}, {@link #apiKeys()},
 * {@link #sendingDomainPolicy()}, and {@link #mtaRateLimit()}.
 */
public final class Listener extends Entity<ListenerData> {
    public Listener(AdminClient client, String path, ListenerData data) {
        super(client, path, data);
    }

    private Listener(AdminClient client, String path, ListenerData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    // --- data ----------------------------------------------------------------

    public String id() {
        return data.id();
    }

    public String tenantId() {
        return data.tenantId();
    }

    public String displayName() {
        return data.displayName();
    }

    public ListenerType listenerType() {
        return data.listenerType();
    }

    public StreamType streamType() {
        return data.streamType();
    }

    public TerminalActionType defaultTerminalActionType() {
        return data.defaultTerminalActionType();
    }

    public TerminalActionPayload defaultTerminalActionPayload() {
        return data.defaultTerminalActionPayload();
    }

    public boolean rspamdScanningEnabled() {
        return data.rspamdScanningEnabled();
    }

    // --- operations ----------------------------------------------------------

    public Listener reload() throws IOException {
        return new Listener(client, path, client.get(path).as(ListenerData.class), isDeleted());
    }

    /** Replace the listener display name. */
    public Listener rename(String displayName) throws IOException {
        return update(new UpdateListenerRequest(displayName));
    }

    /** Replace the writable listener details (currently the display name). */
    public Listener update(Consumer<UpdateListenerRequest.Builder> configure) throws IOException {
        UpdateListenerRequest.Builder builder = UpdateListenerRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    public Listener update(UpdateListenerRequest request) throws IOException {
        return new Listener(client, path, client.put(path, request).as(ListenerData.class), isDeleted());
    }

    /** Set the default terminal action with an empty payload. */
    public Listener updateDefaultTerminalAction(TerminalActionType actionType) throws IOException {
        return updateDefaultTerminalAction(actionType, TerminalActionPayload.empty());
    }

    /** Set the default terminal action and its action-specific payload. */
    public Listener updateDefaultTerminalAction(TerminalActionType actionType, TerminalActionPayload actionPayload)
            throws IOException {
        return updateDefaultTerminalAction(new UpdateListenerDefaultTerminalActionRequest(actionType, actionPayload));
    }

    public Listener updateDefaultTerminalAction(Consumer<UpdateListenerDefaultTerminalActionRequest.Builder> configure) throws IOException {
        UpdateListenerDefaultTerminalActionRequest.Builder builder = UpdateListenerDefaultTerminalActionRequest.builder();
        configure.accept(builder);
        return updateDefaultTerminalAction(builder.build());
    }

    public Listener updateDefaultTerminalAction(UpdateListenerDefaultTerminalActionRequest request)
            throws IOException {
        return new Listener(client, path,
                client.put(path + "/default-terminal-action", request).as(ListenerData.class), isDeleted());
    }

    /** Enable or disable inbound Rspamd scanning. */
    public Listener updateRspamdScanning(boolean enabled) throws IOException {
        return updateRspamdScanning(new UpdateListenerRspamdScanningRequest(enabled));
    }

    public Listener updateRspamdScanning(Consumer<UpdateListenerRspamdScanningRequest.Builder> configure) throws IOException {
        UpdateListenerRspamdScanningRequest.Builder builder = UpdateListenerRspamdScanningRequest.builder();
        configure.accept(builder);
        return updateRspamdScanning(builder.build());
    }

    public Listener updateRspamdScanning(UpdateListenerRspamdScanningRequest request) throws IOException {
        return new Listener(client, path,
                client.put(path + "/rspamd-scanning", request).as(ListenerData.class), isDeleted());
    }

    // --- children ------------------------------------------------------------

    public RoutingRulesClient routingRules() {
        return new RoutingRulesClient(client, path + "/rules");
    }

    public ApiKeysClient apiKeys() {
        return new ApiKeysClient(client, path + "/api-keys");
    }

    public SendingDomainPoliciesClient sendingDomainPolicy() {
        return new SendingDomainPoliciesClient(client, path + "/sending-domain-policy");
    }

    public ListenerMtaRateLimitClient mtaRateLimit() {
        if (listenerType() != ListenerType.MTA) {
            throw new IllegalStateException(
                    "mtaRateLimit is only available for MTA listeners (this listener is " + listenerType() + ")");
        }
        return new ListenerMtaRateLimitClient(client, path + "/mta-rate-limit-override");
    }
}
