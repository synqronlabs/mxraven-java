package com.mxraven.admin.model;

/**
 * Outcome of confirming SMTP forward destination ownership.
 *
 * @param verified whether ownership of the destination was verified
 */
public record SmtpForwardDestinationConfirmation(
        boolean verified) {
}
