package com.mxraven.admin.model;

/**
 * Outcome of confirming SMTP forward destination ownership.
 */
public record SmtpForwardDestinationConfirmation(
        boolean verified) {
}
