package com.mxraven.admin.model;

/**
 * Presence and length metadata for a masked template field.
 */
public record MaskedTemplateField(
        boolean present,
        int length) {
}
