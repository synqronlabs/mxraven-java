package com.mxraven.mail.model;

/**
 * The DSN parameters carried on the SMTP envelope (RFC 3461): the {@code RET}
 * value.
 *
 * @param ret the {@code RET} value selecting how much of the original message is
 *            returned, for example {@code FULL} or {@code HDRS}
 */
public record DSNEnvelopeParams(String ret) {
}
