package com.mxraven.admin.model;

import java.util.List;

/**
 * Stored auto-reply template content as the listener renders it.
 *
 * @param subject message subject
 * @param textBody plain-text body, when set
 * @param htmlBody stored HTML body after the authoring-time content policy
 * @param headers additional headers
 */
public record AutoReplyTemplateContent(
        String subject,
        String textBody,
        String htmlBody,
        List<AutoReplyTemplateHeader> headers) {

    /**
     * Creates content, copying the header list.
     */
    public AutoReplyTemplateContent {
        headers = headers == null ? List.of() : List.copyOf(headers);
    }
}
