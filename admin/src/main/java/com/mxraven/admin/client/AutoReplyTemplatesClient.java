package com.mxraven.admin.client;

import com.mxraven.admin.internal.Java8;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.AutoReplyTemplate;
import com.mxraven.admin.model.AutoReplyTemplateData;
import com.mxraven.admin.model.CreateAutoReplyTemplateRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Tenant auto-reply template collection. Reads and creation return hydrated
 * {@link AutoReplyTemplate} entities.
 */
public final class AutoReplyTemplatesClient {
    private final AdminClient client;
    private final String tenantSlug;

    /**
     * Create a client for the auto-reply-template collection of a tenant.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug whose templates are accessed
     */
    public AutoReplyTemplatesClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * List the auto-reply templates (first page fetched eagerly, remaining pages
     * lazy).
     *
     * @return a lazily paginating collection of auto-reply templates
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<AutoReplyTemplate> list() throws IOException {
        return list(null);
    }

    Paged<AutoReplyTemplate> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), AutoReplyTemplateData.class)
                .map(data -> new AutoReplyTemplate(client, one(data.id()), data));
    }

    /**
     * Create an auto-reply template from a configured builder.
     *
     * @param configure consumer that configures the request builder
     * @return the created auto-reply template
     * @throws IOException if the request fails or is interrupted
     */
    public AutoReplyTemplate create(Consumer<CreateAutoReplyTemplateRequest.Builder> configure) throws IOException {
        CreateAutoReplyTemplateRequest.Builder builder = CreateAutoReplyTemplateRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    /**
     * Create an auto-reply template.
     *
     * @param request creation request
     * @return the created auto-reply template
     * @throws IOException if the request fails or is interrupted
     */
    public AutoReplyTemplate create(CreateAutoReplyTemplateRequest request) throws IOException {
        AutoReplyTemplateData data = client.post(base(), request).as(AutoReplyTemplateData.class);
        return new AutoReplyTemplate(client, one(data.id()), data);
    }

    private String base() {
        return "/tenants/" + tenantSlug + "/auto-reply-templates";
    }

    private String one(String id) {
        return base() + "/" + encode(id);
    }

    private static String encode(String segment) {
        return Java8.urlEncode(segment);
    }

    /**
     * Get an auto-reply template by its identifier.
     *
     * @param templateId template identifier
     * @return the hydrated auto-reply template
     * @throws IOException if the request fails or is interrupted
     */
    public AutoReplyTemplate get(String templateId) throws IOException {
        String resourcePath = one(templateId);
        return new AutoReplyTemplate(client, resourcePath, client.get(resourcePath).as(AutoReplyTemplateData.class));
    }

    /**
     * Get an auto-reply template by its immutable {@code template_ref}.
     *
     * @param templateRef immutable template reference
     * @return the hydrated auto-reply template
     * @throws IOException if the request fails or is interrupted
     */
    public AutoReplyTemplate getByRef(String templateRef) throws IOException {
        String refPath = base() + "/ref/" + encode(templateRef);
        AutoReplyTemplateData data = client.get(refPath).as(AutoReplyTemplateData.class);
        return new AutoReplyTemplate(client, one(data.id()), data);
    }
}
