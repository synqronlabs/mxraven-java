package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.model.ReplaceSendingDomainPolicyRequest;
import com.mxraven.admin.model.SendingDomainPolicy;
import com.mxraven.admin.model.SendingDomainPolicyGrantRequest;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * The sending-domain policy for a single submission listener, obtained from
 * {@link com.mxraven.admin.model.Listener#sendingDomainPolicy()}.
 *
 * <p>The policy is read and replaced as a complete set of domain grants.
 */
public final class SendingDomainPoliciesClient {
    private final AdminClient client;
    private final String path;

    /**
     * Creates a client for the given sending-domain policy path.
     *
     * @param client underlying admin client
     * @param path client-relative policy resource path
     */
    public SendingDomainPoliciesClient(AdminClient client, String path) {
        this.client = client;
        this.path = path;
    }

    /**
     * Gets the listener's sending-domain policy.
     *
     * @return the policy with its current set of domain grants
     * @throws IOException if the request fails or is interrupted
     */
    public SendingDomainPolicy get() throws IOException {
        return client.get(path).as(SendingDomainPolicy.class);
    }

    /**
     * Atomically updates the listener's complete set of domain grants.
     *
     * @param grants replacement domain grants
     * @return the updated sending-domain policy
     * @throws IOException if the request fails or is interrupted
     */
    public SendingDomainPolicy update(List<SendingDomainPolicyGrantRequest> grants) throws IOException {
        return update(new ReplaceSendingDomainPolicyRequest(grants));
    }

    /**
     * Updates the policy through the given builder consumer.
     *
     * @param configure consumer that populates the replacement request builder
     * @return the updated sending-domain policy
     * @throws IOException if the request fails or is interrupted
     */
    public SendingDomainPolicy update(Consumer<ReplaceSendingDomainPolicyRequest.Builder> configure) throws IOException {
        ReplaceSendingDomainPolicyRequest.Builder builder = ReplaceSendingDomainPolicyRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    /**
     * Atomically replaces the policy with the given request.
     *
     * @param request replacement policy request
     * @return the updated sending-domain policy
     * @throws IOException if the request fails or is interrupted
     */
    public SendingDomainPolicy update(ReplaceSendingDomainPolicyRequest request) throws IOException {
        return client.put(path, request).as(SendingDomainPolicy.class);
    }
}
