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
    public SmtpRelay(AdminClient client, String path, SmtpRelayData data) {
        super(client, path, data);
    }

    private SmtpRelay(AdminClient client, String path, SmtpRelayData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    public String id() {
        return data.id();
    }

    public String tenantId() {
        return data.tenantId();
    }

    public String relayRef() {
        return data.relayRef();
    }

    public String displayName() {
        return data.displayName();
    }

    public boolean isActive() {
        return data.isActive();
    }

    public boolean credentialsPresent() {
        return data.credentialsPresent();
    }

    public SmtpRelay reload() throws IOException {
        return new SmtpRelay(client, path, client.get(path).as(SmtpRelayData.class), isDeleted());
    }

    public SmtpRelay update(Consumer<UpdateSmtpRelayRequest.Builder> configure) throws IOException {
        UpdateSmtpRelayRequest.Builder builder = UpdateSmtpRelayRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    public SmtpRelay update(UpdateSmtpRelayRequest request) throws IOException {
        return new SmtpRelay(client, path, client.put(path, request).as(SmtpRelayData.class), isDeleted());
    }

    public SmtpRelay setActive(boolean isActive) throws IOException {
        return setActive(new SetIntegrationActiveRequest(isActive));
    }

    public SmtpRelay setActive(Consumer<SetIntegrationActiveRequest.Builder> configure) throws IOException {
        SetIntegrationActiveRequest.Builder builder = SetIntegrationActiveRequest.builder();
        configure.accept(builder);
        return setActive(builder.build());
    }

    public SmtpRelay setActive(SetIntegrationActiveRequest request) throws IOException {
        return new SmtpRelay(client, path, client.put(path + "/active", request).as(SmtpRelayData.class), isDeleted());
    }
}
