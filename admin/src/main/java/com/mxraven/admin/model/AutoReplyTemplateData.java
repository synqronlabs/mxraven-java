package com.mxraven.admin.model;

/** Wire representation backing the {@link AutoReplyTemplate} entity. */
public record AutoReplyTemplateData(
        String id,
        String tenantId,
        String templateRef,
        String displayName,
        String fromAddress,
        boolean isActive,
        AutoReplyTemplateContentSummary content,
        AutoReplySenderReadiness senderReadiness) {
}
