package com.mxraven.admin.model;

/**
 * Wire representation backing the {@link AutoReplyTemplate} entity.
 *
 * @param id template identifier
 * @param tenantId owning tenant identifier
 * @param templateRef immutable template reference
 * @param displayName human-readable template name
 * @param fromAddress sender address used for replies
 * @param isActive whether the template is active
 * @param content masked content summary
 * @param senderReadiness sender readiness for the template
 */
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
