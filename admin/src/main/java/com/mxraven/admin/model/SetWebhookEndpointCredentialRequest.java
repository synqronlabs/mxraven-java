package com.mxraven.admin.model;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/webhook-endpoints/{id}/credential}.
 *
 * <p>The header names where the credential is sent on routing-script
 * {@code mx.http} calls. The value is stored in Vault and never returned. While
 * a credential is set, calls carry only the script's headers and the
 * credential: they are not signed and carry no {@code X-MxRaven-*} headers.
 *
 * @param header {@code Authorization} (the default when omitted), or an
 *               {@code X-} header such as {@code X-API-Key}
 * @param value the exact header value, such as {@code Bearer sk-123}; no line breaks
 */
public record SetWebhookEndpointCredentialRequest(String header, String value) {

    /**
     * Creates a request, validating the header and value locally.
     */
    public SetWebhookEndpointCredentialRequest {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value is required");
        }
        if (value.codePointCount(0, value.length()) > 4096) {
            throw new IllegalArgumentException("value must be 4096 characters or fewer");
        }
        if (value.indexOf('\r') >= 0 || value.indexOf('\n') >= 0) {
            throw new IllegalArgumentException("value must not contain line breaks");
        }
        if (header != null) {
            String normalized = header.trim();
            if (normalized.isEmpty() || normalized.codePointCount(0, normalized.length()) > 100) {
                throw new IllegalArgumentException("header must be 1-100 characters");
            }
            String lower = normalized.toLowerCase(java.util.Locale.ROOT);
            if (!lower.equals("authorization") && !lower.startsWith("x-")) {
                throw new IllegalArgumentException("header must be Authorization or an X- header");
            }
            if (lower.startsWith("x-mxr-") || lower.startsWith("x-mxraven-")) {
                throw new IllegalArgumentException("header must not use an mxRaven-owned prefix");
            }
            header = normalized;
        }
    }

    /**
     * Creates a request with the default {@code Authorization} header.
     *
     * @param value the exact header value
     * @return the request
     */
    public static SetWebhookEndpointCredentialRequest authorization(String value) {
        return new SetWebhookEndpointCredentialRequest(null, value);
    }

    /**
     * Creates a request with an explicit header.
     *
     * @param header the header name, or {@code null} for {@code Authorization}
     * @param value the exact header value
     * @return the request
     */
    public static SetWebhookEndpointCredentialRequest of(String header, String value) {
        return new SetWebhookEndpointCredentialRequest(header, value);
    }
}
