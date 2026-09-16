package com.mxraven.mail.model;

/**
 * A single header field, consisting of a name and a value.
 *
 * @param name  the field name
 * @param value the field value
 */
public record Header(String name, String value) {
}
