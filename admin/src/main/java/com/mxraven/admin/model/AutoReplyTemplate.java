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
    /**
     * Creates an auto-reply template entity bound to a client, path, and backing data.
     *
     * @param client admin client used for requests
     * @param path resource path
     * @param data backing template data
     */
    public AutoReplyTemplate(AdminClient client, String path, AutoReplyTemplateData data) {
        super(client, path, data);
    }

    private AutoReplyTemplate(AdminClient client, String path, AutoReplyTemplateData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    /**
     * Returns the template identifier.
     *
     * @return the template identifier
     */
    public String id() {
        return data.id();
    }

    /**
     * Returns the owning tenant identifier.
     *
     * @return the owning tenant identifier
     */
    public String tenantId() {
        return data.tenantId();
    }

    /**
     * Returns the immutable template reference.
     *
     * @return the immutable template reference
     */
    public String templateRef() {
        return data.templateRef();
    }

    /**
     * Returns the human-readable template name.
     *
     * @return the human-readable template name
     */
    public String displayName() {
        return data.displayName();
    }

    /**
     * Returns the sender address used for replies.
     *
     * @return the sender address used for replies
     */
    public String fromAddress() {
        return data.fromAddress();
    }

    /**
     * Returns whether the template is active.
     *
     * @return whether the template is active
     */
    public boolean isActive() {
        return data.isActive();
    }

    /**
     * Returns the masked content summary.
     *
     * @return the masked content summary
     */
    public AutoReplyTemplateContentSummary content() {
        return data.content();
    }

    /**
     * Returns the sender readiness for the template.
     *
     * @return the sender readiness for the template
     */
    public AutoReplySenderReadiness senderReadiness() {
        return data.senderReadiness();
    }

    /**
     * Re-fetches this template and returns a fresh snapshot.
     *
     * @return fresh template snapshot
     * @throws IOException when the request fails
     */
    public AutoReplyTemplate reload() throws IOException {
        return new AutoReplyTemplate(client, path, client.get(path).as(AutoReplyTemplateData.class), isDeleted());
    }

    /**
     * Applies the configured update and returns the updated template.
     *
     * @param configure consumer that configures the update builder
     * @return updated template
     * @throws IOException when the request fails
     */
    public AutoReplyTemplate update(Consumer<UpdateAutoReplyTemplateRequest.Builder> configure) throws IOException {
        UpdateAutoReplyTemplateRequest.Builder builder = UpdateAutoReplyTemplateRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    /**
     * Applies the given update request and returns the updated template.
     *
     * @param request update request
     * @return updated template
     * @throws IOException when the request fails
     */
    public AutoReplyTemplate update(UpdateAutoReplyTemplateRequest request) throws IOException {
        return new AutoReplyTemplate(client, path,
                client.put(path, request).as(AutoReplyTemplateData.class), isDeleted());
    }

    /**
     * Sets whether the template is active.
     *
     * @param isActive new active flag
     * @return updated template
     * @throws IOException when the request fails
     */
    public AutoReplyTemplate setActive(boolean isActive) throws IOException {
        return setActive(new SetAutoReplyTemplateActiveRequest(isActive));
    }

    /**
     * Configures and applies an active-state change.
     *
     * @param configure consumer that configures the active-state request builder
     * @return updated template
     * @throws IOException when the request fails
     */
    public AutoReplyTemplate setActive(Consumer<SetAutoReplyTemplateActiveRequest.Builder> configure) throws IOException {
        SetAutoReplyTemplateActiveRequest.Builder builder = SetAutoReplyTemplateActiveRequest.builder();
        configure.accept(builder);
        return setActive(builder.build());
    }

    /**
     * Applies the given active-state change.
     *
     * @param request active-state request
     * @return updated template
     * @throws IOException when the request fails
     */
    public AutoReplyTemplate setActive(SetAutoReplyTemplateActiveRequest request) throws IOException {
        return new AutoReplyTemplate(client, path,
                client.put(path + "/active", request).as(AutoReplyTemplateData.class), isDeleted());
    }
}
