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
    /**
     * Creates a hydrated listener from wire data.
     *
     * @param client the client used to issue requests
     * @param path client-relative path of the listener
     * @param data the wire record backing this entity
     */
    public Listener(AdminClient client, String path, ListenerData data) {
        super(client, path, data);
    }

    private Listener(AdminClient client, String path, ListenerData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    // --- data ----------------------------------------------------------------

    /**
     * Returns the unique listener identifier.
     *
     * @return the listener identifier
     */
    public String id() {
        return data.id();
    }

    /**
     * Returns the identifier of the owning tenant.
     *
     * @return the tenant identifier
     */
    public String tenantId() {
        return data.tenantId();
    }

    /**
     * Returns the human-readable listener name.
     *
     * @return the display name
     */
    public String displayName() {
        return data.displayName();
    }

    /**
     * Returns whether this is a submission or MTA listener.
     *
     * @return the listener type
     */
    public ListenerType listenerType() {
        return data.listenerType();
    }

    /**
     * Returns the message stream handled by the listener.
     *
     * @return the stream type
     */
    public StreamType streamType() {
        return data.streamType();
    }

    /**
     * Returns the terminal action applied when no rule matches.
     *
     * @return the default terminal action type
     */
    public TerminalActionType defaultTerminalActionType() {
        return data.defaultTerminalActionType();
    }

    /**
     * Returns the action-specific payload for the default terminal action.
     *
     * @return the default terminal action payload
     */
    public TerminalActionPayload defaultTerminalActionPayload() {
        return data.defaultTerminalActionPayload();
    }

    /**
     * Returns whether inbound Rspamd scanning is enabled.
     *
     * @return {@code true} when Rspamd scanning is enabled
     */
    public boolean rspamdScanningEnabled() {
        return data.rspamdScanningEnabled();
    }

    // --- operations ----------------------------------------------------------

    /**
     * Reloads the listener from the server.
     *
     * @return a fresh listener snapshot
     * @throws IOException if the request fails or is interrupted
     */
    public Listener reload() throws IOException {
        return new Listener(client, path, client.get(path).as(ListenerData.class), isDeleted());
    }

    /**
     * Replace the listener display name.
     *
     * @param displayName the new display name
     * @return the updated listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener rename(String displayName) throws IOException {
        return update(new UpdateListenerRequest(displayName));
    }

    /**
     * Replace the writable listener details (currently the display name).
     *
     * @param configure callback that configures the update request builder
     * @return the updated listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener update(Consumer<UpdateListenerRequest.Builder> configure) throws IOException {
        UpdateListenerRequest.Builder builder = UpdateListenerRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    /**
     * Updates the listener with a prepared request.
     *
     * @param request the update request
     * @return the updated listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener update(UpdateListenerRequest request) throws IOException {
        return new Listener(client, path, client.put(path, request).as(ListenerData.class), isDeleted());
    }

    /**
     * Set the default terminal action with an empty payload.
     *
     * @param actionType the new default terminal action type
     * @return the updated listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener updateDefaultTerminalAction(TerminalActionType actionType) throws IOException {
        return updateDefaultTerminalAction(actionType, TerminalActionPayload.empty());
    }

    /**
     * Set the default terminal action and its action-specific payload.
     *
     * @param actionType the new default terminal action type
     * @param actionPayload the action-specific payload
     * @return the updated listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener updateDefaultTerminalAction(TerminalActionType actionType, TerminalActionPayload actionPayload)
            throws IOException {
        return updateDefaultTerminalAction(new UpdateListenerDefaultTerminalActionRequest(actionType, actionPayload));
    }

    /**
     * Updates the default terminal action using a request builder.
     *
     * @param configure callback that configures the update request builder
     * @return the updated listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener updateDefaultTerminalAction(Consumer<UpdateListenerDefaultTerminalActionRequest.Builder> configure) throws IOException {
        UpdateListenerDefaultTerminalActionRequest.Builder builder = UpdateListenerDefaultTerminalActionRequest.builder();
        configure.accept(builder);
        return updateDefaultTerminalAction(builder.build());
    }

    /**
     * Updates the default terminal action with a prepared request.
     *
     * @param request the update request
     * @return the updated listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener updateDefaultTerminalAction(UpdateListenerDefaultTerminalActionRequest request)
            throws IOException {
        return new Listener(client, path,
                client.put(path + "/default-terminal-action", request).as(ListenerData.class), isDeleted());
    }

    /**
     * Enable or disable inbound Rspamd scanning.
     *
     * @param enabled {@code true} to enable scanning, {@code false} to disable it
     * @return the updated listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener updateRspamdScanning(boolean enabled) throws IOException {
        return updateRspamdScanning(new UpdateListenerRspamdScanningRequest(enabled));
    }

    /**
     * Updates Rspamd scanning using a request builder.
     *
     * @param configure callback that configures the update request builder
     * @return the updated listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener updateRspamdScanning(Consumer<UpdateListenerRspamdScanningRequest.Builder> configure) throws IOException {
        UpdateListenerRspamdScanningRequest.Builder builder = UpdateListenerRspamdScanningRequest.builder();
        configure.accept(builder);
        return updateRspamdScanning(builder.build());
    }

    /**
     * Updates Rspamd scanning with a prepared request.
     *
     * @param request the update request
     * @return the updated listener
     * @throws IOException if the request fails or is interrupted
     */
    public Listener updateRspamdScanning(UpdateListenerRspamdScanningRequest request) throws IOException {
        return new Listener(client, path,
                client.put(path + "/rspamd-scanning", request).as(ListenerData.class), isDeleted());
    }

    // --- children ------------------------------------------------------------

    /**
     * Returns a client for the listener's routing rules.
     *
     * @return the routing rules client
     */
    public RoutingRulesClient routingRules() {
        return new RoutingRulesClient(client, path + "/rules");
    }

    /**
     * Returns a client for the listener's API keys.
     *
     * @return the API keys client
     */
    public ApiKeysClient apiKeys() {
        return new ApiKeysClient(client, path + "/api-keys");
    }

    /**
     * Returns a client for the listener's sending-domain policy.
     *
     * @return the sending domain policy client
     */
    public SendingDomainPoliciesClient sendingDomainPolicy() {
        return new SendingDomainPoliciesClient(client, path + "/sending-domain-policy");
    }

    /**
     * Returns a client for the listener's MTA rate-limit override.
     *
     * @return the MTA rate-limit client
     * @throws IllegalStateException if the listener is not an {@link ListenerType#MTA}
     */
    public ListenerMtaRateLimitClient mtaRateLimit() {
        if (listenerType() != ListenerType.MTA) {
            throw new IllegalStateException(
                    "mtaRateLimit is only available for MTA listeners (this listener is " + listenerType() + ")");
        }
        return new ListenerMtaRateLimitClient(client, path + "/mta-rate-limit-override");
    }
}
