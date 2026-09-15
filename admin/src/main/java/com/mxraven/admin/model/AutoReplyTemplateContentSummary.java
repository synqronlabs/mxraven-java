package com.mxraven.admin.model;

/**
 * Masked summary of an auto-reply template's content.
 */
public record AutoReplyTemplateContentSummary(
        MaskedTemplateField subject,
        MaskedTemplateField textBody,
        MaskedTemplateField htmlBody,
        int headerCount) {
}
