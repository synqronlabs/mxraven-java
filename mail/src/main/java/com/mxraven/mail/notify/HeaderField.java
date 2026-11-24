package com.mxraven.mail.notify;

/**
 * One parsed message header occurrence.
 *
 * @param name the header field name
 * @param value the header field value
 */
public record HeaderField(String name, String value) {
}
