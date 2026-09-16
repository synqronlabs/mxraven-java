package com.mxraven.mail;

/**
 * The transport security mode used for an SMTP connection.
 */
public enum SecurityMode {
    /** Plaintext transport with no TLS. */
    NONE,
    /** Upgrades an initially plaintext connection with the {@code STARTTLS} command. */
    STARTTLS,
    /** Wraps the connection in TLS from the first byte. */
    IMPLICIT_TLS
}
