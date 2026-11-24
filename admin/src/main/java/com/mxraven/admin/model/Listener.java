package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import com.mxraven.admin.client.ApiKeysClient;
import com.mxraven.admin.client.RoutingScriptsClient;
import com.mxraven.admin.client.SendingDomainPoliciesClient;
import com.mxraven.admin.client.ListenerMtaRateLimitClient;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * A submission or MTA listener.
 *
 * <p>Listener type, stream, scanning, notification settings, and the routing
 * script are immutable through the plain update; use the dedicated operations.
 * Reach the listener's strictly-owned children through {@link #routingScripts()},
 * {@link #apiKeys()}, {@link #sendingDomainPolicy()}, and {@link #mtaRateLimit()}.
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
     * Returns the published routing-script revision, when the listener has one.
     *
     * <p>Absent means the listener-type default applies: submission listeners
     * deliver through the shared pool and MTA listeners drop after acceptance.
     *
     * @return the published revision identifier, or {@code null}
     */
    public String activeScriptRevisionId() {
        return data.activeScriptRevisionId();
    }

    /**
     * Returns whether the routing script may read the text/plain body.
     *
     * <p>Body access is available to MTA listeners only.
     *
     * @return {@code true} when {@code mail.body:text()} may read the body
     */
    public boolean scriptBodyAccess() {
        return data.scriptBodyAccess();
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

    /**
     * Reads the listener's client notification setting.
     *
     * <p>Reports whether the listener publishes client notification events, and
     * the subject and durable consumer clients use to read them.
     *
     * @return the listener's notification setting
     * @throws IOException if the request fails or is interrupted
     */
    public ListenerNotificationSetting notificationSetting() throws IOException {
        return client.get(path + "/notifications").as(ListenerNotificationSetting.class);
    }

    /**
     * Enable or disable client notification publishing for the listener.
     *
     * <p>Enabling creates the listener's durable consumer and starts event
     * publishing; disabling stops publishing and retains the consumer, so events
     * a client already handled are not replayed if it is enabled again.
     *
     * @param enabled {@code true} to publish client notifications, {@code false} to stop
     * @return the updated notification setting
     * @throws IOException if the request fails or is interrupted
     */
    public ListenerNotificationSetting updateNotifications(boolean enabled) throws IOException {
        return updateNotifications(new UpdateListenerNotificationsRequest(enabled));
    }

    /**
     * Updates the notification setting using a request builder.
     *
     * @param configure callback that configures the update request builder
     * @return the updated notification setting
     * @throws IOException if the request fails or is interrupted
     */
    public ListenerNotificationSetting updateNotifications(Consumer<UpdateListenerNotificationsRequest.Builder> configure) throws IOException {
        UpdateListenerNotificationsRequest.Builder builder = UpdateListenerNotificationsRequest.builder();
        configure.accept(builder);
        return updateNotifications(builder.build());
    }

    /**
     * Updates the notification setting with a prepared request.
     *
     * @param request the update request
     * @return the updated notification setting
     * @throws IOException if the request fails or is interrupted
     */
    public ListenerNotificationSetting updateNotifications(UpdateListenerNotificationsRequest request) throws IOException {
        return client.put(path + "/notifications", request).as(ListenerNotificationSetting.class);
    }

    // --- children ------------------------------------------------------------

    /**
     * Returns a client for the listener's routing script.
     *
     * @return the routing-script client
     */
    public RoutingScriptsClient routingScripts() {
        return new RoutingScriptsClient(client, path + "/script", listenerType());
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
