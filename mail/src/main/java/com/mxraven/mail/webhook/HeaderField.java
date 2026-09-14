package com.mxraven.mail.webhook;

/**
 * One message header occurrence, in the order received.
 *
 * @param name  the header field name
 * @param value the header field value
 */
public record HeaderField(String name, String value) {
}
