package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.ConfirmSmtpForwardDestinationRequest;
import com.mxraven.admin.model.CreateSmtpForwardDestinationRequest;
import com.mxraven.admin.model.SmtpForwardDestination;
import com.mxraven.admin.model.SmtpForwardDestinationConfirmation;
import com.mxraven.admin.model.SmtpForwardDestinationData;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Tenant SMTP forward-destination collection. Reads and creation return hydrated
 * {@link SmtpForwardDestination} entities.
 */
public final class SmtpForwardDestinationsClient {
    private final AdminClient client;
    private final String tenantSlug;

    public SmtpForwardDestinationsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    public Paged<SmtpForwardDestination> list() throws IOException {
        return list(null);
    }

    Paged<SmtpForwardDestination> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), SmtpForwardDestinationData.class)
                .map(data -> new SmtpForwardDestination(client, one(data.id()), data));
    }

    public SmtpForwardDestination create(Consumer<CreateSmtpForwardDestinationRequest.Builder> configure) throws IOException {
        CreateSmtpForwardDestinationRequest.Builder builder = CreateSmtpForwardDestinationRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    public SmtpForwardDestination create(CreateSmtpForwardDestinationRequest request) throws IOException {
        SmtpForwardDestinationData data = client.post(base(), request).as(SmtpForwardDestinationData.class);
        return new SmtpForwardDestination(client, one(data.id()), data);
    }

    /**
     * Confirms ownership with an emailed token. This operation is
     * account-level, not tenant-scoped.
     */
    public SmtpForwardDestinationConfirmation confirm(String token) throws IOException {
        return confirm(ConfirmSmtpForwardDestinationRequest.builder().token(token).build());
    }

    public SmtpForwardDestinationConfirmation confirm(Consumer<ConfirmSmtpForwardDestinationRequest.Builder> configure) throws IOException {
        ConfirmSmtpForwardDestinationRequest.Builder builder = ConfirmSmtpForwardDestinationRequest.builder();
        configure.accept(builder);
        return confirm(builder.build());
    }

    public SmtpForwardDestinationConfirmation confirm(ConfirmSmtpForwardDestinationRequest request) throws IOException {
        return client.post("/smtp-forward-destination-verifications/confirm", request)
                .as(SmtpForwardDestinationConfirmation.class);
    }

    private String base() {
        return "/tenants/" + tenantSlug + "/smtp-forward-destinations";
    }

    private String one(String id) {
        return base() + "/" + encode(id);
    }

    private static String encode(String segment) {
        return URLEncoder.encode(segment, StandardCharsets.UTF_8);
    }

    public SmtpForwardDestination get(String destinationId) throws IOException {
        String resourcePath = one(destinationId);
        return new SmtpForwardDestination(client, resourcePath, client.get(resourcePath).as(SmtpForwardDestinationData.class));
    }

    /** Get an SMTP forward destination by its immutable {@code destination_ref}. */
    public SmtpForwardDestination getByRef(String destinationRef) throws IOException {
        String refPath = base() + "/ref/" + encode(destinationRef);
        SmtpForwardDestinationData data = client.get(refPath).as(SmtpForwardDestinationData.class);
        return new SmtpForwardDestination(client, one(data.id()), data);
    }
}
