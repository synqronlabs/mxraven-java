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

    /**
     * Creates a client bound to the given tenant.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug
     */
    public SmtpForwardDestinationsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * Lists SMTP forward destinations for the tenant.
     *
     * @return a lazily paginated collection of forward destinations
     * @throws IOException if the request fails or is interrupted
     */
    public Paged<SmtpForwardDestination> list() throws IOException {
        return list(null);
    }

    Paged<SmtpForwardDestination> list(QueryParams params) throws IOException {
        return client.paged(base(), params == null ? null : params.toMap(), SmtpForwardDestinationData.class)
                .map(data -> new SmtpForwardDestination(client, one(data.id()), data));
    }

    /**
     * Creates a forward destination configured through the given builder consumer.
     *
     * @param configure consumer that populates the creation request builder
     * @return the created forward destination
     * @throws IOException if the request fails or is interrupted
     */
    public SmtpForwardDestination create(Consumer<CreateSmtpForwardDestinationRequest.Builder> configure) throws IOException {
        CreateSmtpForwardDestinationRequest.Builder builder = CreateSmtpForwardDestinationRequest.builder();
        configure.accept(builder);
        return create(builder.build());
    }

    /**
     * Creates a forward destination described by the given request.
     *
     * @param request forward-destination creation request
     * @return the created forward destination
     * @throws IOException if the request fails or is interrupted
     */
    public SmtpForwardDestination create(CreateSmtpForwardDestinationRequest request) throws IOException {
        SmtpForwardDestinationData data = client.post(base(), request).as(SmtpForwardDestinationData.class);
        return new SmtpForwardDestination(client, one(data.id()), data);
    }

    /**
     * Confirms ownership with an emailed token. This operation is
     * account-level, not tenant-scoped.
     *
     * @param token emailed confirmation token
     * @return the confirmation outcome
     * @throws IOException if the request fails or is interrupted
     */
    public SmtpForwardDestinationConfirmation confirm(String token) throws IOException {
        return confirm(ConfirmSmtpForwardDestinationRequest.builder().token(token).build());
    }

    /**
     * Confirms ownership through the given builder consumer.
     *
     * @param configure consumer that populates the confirmation request builder
     * @return the confirmation outcome
     * @throws IOException if the request fails or is interrupted
     */
    public SmtpForwardDestinationConfirmation confirm(Consumer<ConfirmSmtpForwardDestinationRequest.Builder> configure) throws IOException {
        ConfirmSmtpForwardDestinationRequest.Builder builder = ConfirmSmtpForwardDestinationRequest.builder();
        configure.accept(builder);
        return confirm(builder.build());
    }

    /**
     * Confirms ownership with the given request.
     *
     * @param request confirmation request carrying the emailed token
     * @return the confirmation outcome
     * @throws IOException if the request fails or is interrupted
     */
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

    /**
     * Gets a forward destination by its identifier.
     *
     * @param destinationId forward-destination identifier
     * @return the forward destination
     * @throws IOException if the request fails or is interrupted
     */
    public SmtpForwardDestination get(String destinationId) throws IOException {
        String resourcePath = one(destinationId);
        return new SmtpForwardDestination(client, resourcePath, client.get(resourcePath).as(SmtpForwardDestinationData.class));
    }

    /**
     * Gets an SMTP forward destination by its immutable {@code destination_ref}.
     *
     * @param destinationRef immutable forward-destination reference
     * @return the forward destination
     * @throws IOException if the request fails or is interrupted
     */
    public SmtpForwardDestination getByRef(String destinationRef) throws IOException {
        String refPath = base() + "/ref/" + encode(destinationRef);
        SmtpForwardDestinationData data = client.get(refPath).as(SmtpForwardDestinationData.class);
        return new SmtpForwardDestination(client, one(data.id()), data);
    }
}
