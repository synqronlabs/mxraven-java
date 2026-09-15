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

    public SendingDomainPoliciesClient(AdminClient client, String path) {
        this.client = client;
        this.path = path;
    }

    public SendingDomainPolicy get() throws IOException {
        return client.get(path).as(SendingDomainPolicy.class);
    }

    /** Atomically updates the listener's complete set of domain grants. */
    public SendingDomainPolicy update(List<SendingDomainPolicyGrantRequest> grants) throws IOException {
        return update(new ReplaceSendingDomainPolicyRequest(grants));
    }

    public SendingDomainPolicy update(Consumer<ReplaceSendingDomainPolicyRequest.Builder> configure) throws IOException {
        ReplaceSendingDomainPolicyRequest.Builder builder = ReplaceSendingDomainPolicyRequest.builder();
        configure.accept(builder);
        return update(builder.build());
    }

    public SendingDomainPolicy update(ReplaceSendingDomainPolicyRequest request) throws IOException {
        return client.put(path, request).as(SendingDomainPolicy.class);
    }
}
