package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Hydrated SmtpForwardDestination entity. Reads return a snapshot; call <code>reload()</code> for fresh state.
 */
public final class SmtpForwardDestination extends Entity<SmtpForwardDestinationData> {
    public SmtpForwardDestination(AdminClient client, String path, SmtpForwardDestinationData data) {
        super(client, path, data);
    }

    private SmtpForwardDestination(AdminClient client, String path, SmtpForwardDestinationData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    public String id() {
        return data.id();
    }

    public String tenantId() {
        return data.tenantId();
    }

    public String destinationRef() {
        return data.destinationRef();
    }

    public String displayName() {
        return data.displayName();
    }

    public String emailAddress() {
        return data.emailAddress();
    }

    public SmtpForwardDestinationVerificationStatus verificationStatus() {
        return data.verificationStatus();
    }

    public SmtpForwardDestinationVerificationMethod verificationMethod() {
        return data.verificationMethod();
    }

    public SmtpForwardVerificationDeliveryStatus deliveryStatus() {
        return data.deliveryStatus();
    }

    public String verifiedAt() {
        return data.verifiedAt();
    }

    public String deliveryRequestedAt() {
        return data.deliveryRequestedAt();
    }

    public String verificationEmailSentAt() {
        return data.verificationEmailSentAt();
    }

    public String createdAt() {
        return data.createdAt();
    }

    public String updatedAt() {
        return data.updatedAt();
    }

    public SmtpForwardDestination reload() throws IOException {
        return new SmtpForwardDestination(client, path,
                client.get(path).as(SmtpForwardDestinationData.class), isDeleted());
    }

    /** Queues another verification email and returns the updated destination. */
    public SmtpForwardDestination sendVerification() throws IOException {
        return new SmtpForwardDestination(client, path,
                client.post(path + "/send-verification", null).as(SmtpForwardDestinationData.class), isDeleted());
    }
}
