package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Hydrated SmtpRelay entity. Reads return a snapshot; call <code>reload()</code> for fresh state.
 */
public final class SmtpRelay extends Entity<SmtpRelayData> {
    /**
     * Creates a relay bound to the given client, path, and data.
     *
     * @param client the admin client
     * @param path   the resource path
     * @param data   the backing data
     */
    public SmtpRelay(AdminClient client, String path, SmtpRelayData data) {
        super(client, path, data);
    }

    private SmtpRelay(AdminClient client, String path, SmtpRelayData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    /**
     * Returns the entity identifier.
     *
     * @return the entity identifier
     */
    public String id() {
        return data.id();
    }

    /**
     * Returns the identifier of the owning tenant.
     *
     * @return the owning tenant identifier
     */
    public String tenantId() {
        return data.tenantId();
    }

    /**
     * Returns the stable reference for the relay.
     *
     * @return the relay reference
     */
    public String relayRef() {
        return data.relayRef();
    }

    /**
     * Returns the human-readable relay name.
     *
     * @return the relay name
     */
    public String displayName() {
        return data.displayName();
    }

    /**
     * Returns whether the relay is active.
     *
     * @return {@code true} if the relay is active
     */
    public boolean isActive() {
        return data.isActive();
    }

    /**
     * Returns whether relay credentials are stored.
     *
     * @return {@code true} if credentials are stored
     */
    public boolean credentialsPresent() {
        return data.credentialsPresent();
    }

    /**
     * Re-fetches this relay and returns a fresh snapshot.
     *
     * @return a fresh relay snapshot
     * @throws IOException if the request fails
     */
    public SmtpRelay reload() throws IOException {
        return new SmtpRelay(client, path, client.get(path).as(SmtpRelayData.class), isDeleted());
    }

    /**
     * Replaces this relay's configuration using a builder.
     *
     * @param configure callback that configures the update request
     * @return the updated relay
     * @throws IOException if the request fails
     */
    public SmtpRelay update(Consumer<UpdateSmtpRelayRequest.Builder> configure) throws IOException {
        UpdateSmtpRelayRequest.Builder builder = UpdateSmtpRelayRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    /**
     * Replaces this relay's configuration.
     *
     * @param request replacement request
     * @return the updated relay
     * @throws IOException if the request fails
     */
    public SmtpRelay update(UpdateSmtpRelayRequest request) throws IOException {
        return new SmtpRelay(client, path, client.put(path, request).as(SmtpRelayData.class), isDeleted());
    }

    /**
     * Enables or disables the relay.
     *
     * @param isActive whether the relay is active
     * @return the updated relay
     * @throws IOException if the request fails
     */
    public SmtpRelay setActive(boolean isActive) throws IOException {
        return setActive(new SetIntegrationActiveRequest(isActive));
    }

    /**
     * Enables or disables the relay using a builder.
     *
     * @param configure callback that configures the active-state request
     * @return the updated relay
     * @throws IOException if the request fails
     */
    public SmtpRelay setActive(Consumer<SetIntegrationActiveRequest.Builder> configure) throws IOException {
        SetIntegrationActiveRequest.Builder builder = SetIntegrationActiveRequest.builder();
        configure.accept(builder);
        return setActive(builder.build());
    }

    /**
     * Sets the relay active state.
     *
     * @param request active-state request
     * @return the updated relay
     * @throws IOException if the request fails
     */
    public SmtpRelay setActive(SetIntegrationActiveRequest request) throws IOException {
        return new SmtpRelay(client, path, client.put(path + "/active", request).as(SmtpRelayData.class), isDeleted());
    }
}
