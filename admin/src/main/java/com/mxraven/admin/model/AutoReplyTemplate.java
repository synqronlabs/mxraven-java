package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Hydrated AutoReplyTemplate entity. Reads return a snapshot; call <code>reload()</code> for fresh state.
 */
public final class AutoReplyTemplate extends Entity<AutoReplyTemplateData> {
    public AutoReplyTemplate(AdminClient client, String path, AutoReplyTemplateData data) {
        super(client, path, data);
    }

    private AutoReplyTemplate(AdminClient client, String path, AutoReplyTemplateData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    public String id() {
        return data.id();
    }

    public String tenantId() {
        return data.tenantId();
    }

    public String templateRef() {
        return data.templateRef();
    }

    public String displayName() {
        return data.displayName();
    }

    public String fromAddress() {
        return data.fromAddress();
    }

    public boolean isActive() {
        return data.isActive();
    }

    public AutoReplyTemplateContentSummary content() {
        return data.content();
    }

    public AutoReplySenderReadiness senderReadiness() {
        return data.senderReadiness();
    }

    public AutoReplyTemplate reload() throws IOException {
        return new AutoReplyTemplate(client, path, client.get(path).as(AutoReplyTemplateData.class), isDeleted());
    }

    public AutoReplyTemplate update(Consumer<UpdateAutoReplyTemplateRequest.Builder> configure) throws IOException {
        UpdateAutoReplyTemplateRequest.Builder builder = UpdateAutoReplyTemplateRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    public AutoReplyTemplate update(UpdateAutoReplyTemplateRequest request) throws IOException {
        return new AutoReplyTemplate(client, path,
                client.put(path, request).as(AutoReplyTemplateData.class), isDeleted());
    }

    public AutoReplyTemplate setActive(boolean isActive) throws IOException {
        return setActive(new SetAutoReplyTemplateActiveRequest(isActive));
    }

    public AutoReplyTemplate setActive(Consumer<SetAutoReplyTemplateActiveRequest.Builder> configure) throws IOException {
        SetAutoReplyTemplateActiveRequest.Builder builder = SetAutoReplyTemplateActiveRequest.builder();
        configure.accept(builder);
        return setActive(builder.build());
    }

    public AutoReplyTemplate setActive(SetAutoReplyTemplateActiveRequest request) throws IOException {
        return new AutoReplyTemplate(client, path,
                client.put(path + "/active", request).as(AutoReplyTemplateData.class), isDeleted());
    }
}
