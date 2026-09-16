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
    /**
     * Creates a destination bound to the given client, path, and data.
     *
     * @param client the admin client
     * @param path   the resource path
     * @param data   the backing data
     */
    public SmtpForwardDestination(AdminClient client, String path, SmtpForwardDestinationData data) {
        super(client, path, data);
    }

    private SmtpForwardDestination(AdminClient client, String path, SmtpForwardDestinationData data, boolean deleted) {
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
     * Returns the stable reference for the destination.
     *
     * @return the destination reference
     */
    public String destinationRef() {
        return data.destinationRef();
    }

    /**
     * Returns the human-readable destination name.
     *
     * @return the destination name
     */
    public String displayName() {
        return data.displayName();
    }

    /**
     * Returns the destination email address.
     *
     * @return the destination email address
     */
    public String emailAddress() {
        return data.emailAddress();
    }

    /**
     * Returns the ownership verification status.
     *
     * @return the verification status
     */
    public SmtpForwardDestinationVerificationStatus verificationStatus() {
        return data.verificationStatus();
    }

    /**
     * Returns how ownership is verified.
     *
     * @return the verification method
     */
    public SmtpForwardDestinationVerificationMethod verificationMethod() {
        return data.verificationMethod();
    }

    /**
     * Returns the status of the verification email delivery.
     *
     * @return the delivery status
     */
    public SmtpForwardVerificationDeliveryStatus deliveryStatus() {
        return data.deliveryStatus();
    }

    /**
     * Returns the timestamp when ownership was verified.
     *
     * @return the verification timestamp
     */
    public String verifiedAt() {
        return data.verifiedAt();
    }

    /**
     * Returns the timestamp when verification delivery was requested.
     *
     * @return the delivery request timestamp
     */
    public String deliveryRequestedAt() {
        return data.deliveryRequestedAt();
    }

    /**
     * Returns the timestamp when the verification email was last sent.
     *
     * @return the last verification email timestamp
     */
    public String verificationEmailSentAt() {
        return data.verificationEmailSentAt();
    }

    /**
     * Returns the timestamp when the destination was created.
     *
     * @return the creation timestamp
     */
    public String createdAt() {
        return data.createdAt();
    }

    /**
     * Returns the timestamp when the destination was last updated.
     *
     * @return the last-update timestamp
     */
    public String updatedAt() {
        return data.updatedAt();
    }

    /**
     * Re-fetches this destination and returns a fresh snapshot.
     *
     * @return a fresh destination snapshot
     * @throws IOException if the request fails
     */
    public SmtpForwardDestination reload() throws IOException {
        return new SmtpForwardDestination(client, path,
                client.get(path).as(SmtpForwardDestinationData.class), isDeleted());
    }

    /**
     * Queues another verification email and returns the updated destination.
     *
     * @return the updated destination
     * @throws IOException if the request fails
     */
    public SmtpForwardDestination sendVerification() throws IOException {
        return new SmtpForwardDestination(client, path,
                client.post(path + "/send-verification", null).as(SmtpForwardDestinationData.class), isDeleted());
    }
}
