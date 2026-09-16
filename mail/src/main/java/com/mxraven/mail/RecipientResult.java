package com.mxraven.mail;

import com.mxraven.mail.model.Recipient;

/**
 * The outcome of a single {@code RCPT TO} command during a send operation.
 *
 * @param recipient the recipient the server was asked to accept
 * @param accepted whether the server accepted the recipient
 * @param status the server reply text for the recipient
 */
public record RecipientResult(Recipient recipient, boolean accepted, String status) {
}
