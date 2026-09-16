package com.mxraven.admin.model;

/**
 * Presence and length metadata for a masked template field.
 *
 * @param present whether the field is present
 * @param length  length of the field value
 */
public record MaskedTemplateField(
        boolean present,
        int length) {
}
