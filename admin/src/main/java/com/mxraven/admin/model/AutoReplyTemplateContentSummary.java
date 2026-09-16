package com.mxraven.admin.model;

/**
 * Masked summary of an auto-reply template's content.
 *
 * @param subject masked subject field
 * @param textBody masked plain-text body field
 * @param htmlBody masked HTML body field
 * @param headerCount number of custom headers
 */
public record AutoReplyTemplateContentSummary(
        MaskedTemplateField subject,
        MaskedTemplateField textBody,
        MaskedTemplateField htmlBody,
        int headerCount) {
}
