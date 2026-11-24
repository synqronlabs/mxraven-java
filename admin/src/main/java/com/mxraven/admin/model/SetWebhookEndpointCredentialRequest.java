package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Locale;
import java.util.Objects;

/**
 * Request body for {@code PUT /v2/tenants/{slug}/webhook-endpoints/{id}/credential}.
 *
 * <p>The header names where the credential is sent on routing-script
 * {@code mx.http} calls. The value is stored in Vault and never returned. While
 * a credential is set, calls carry only the script's headers and the
 * credential: they are not signed and carry no {@code X-MxRaven-*} headers.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SetWebhookEndpointCredentialRequest {
    private final String header;
    private final String value;

    /** {@code Authorization} (the default when omitted), or an {@code X-} header */
    public String header() {
        return header;
    }

    /** the exact header value, such as {@code Bearer sk-123}; no line breaks */
    public String value() {
        return value;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SetWebhookEndpointCredentialRequest that = (SetWebhookEndpointCredentialRequest) o;
        return Objects.equals(this.header, that.header) && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.header, this.value);
    }

    @Override
    public String toString() {
        return "SetWebhookEndpointCredentialRequest[" + "header=" + this.header
                + ", " + "value=" + this.value + "]";
    }

    /**
     * Creates a request, validating the header and value locally.
     *
     * @param header the header name, or {@code null} for {@code Authorization}
     * @param value the exact header value
     */
    @JsonCreator
    public SetWebhookEndpointCredentialRequest(String header, String value) {
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
            String lower = normalized.toLowerCase(Locale.ROOT);
            if (!lower.equals("authorization") && !lower.startsWith("x-")) {
                throw new IllegalArgumentException("header must be Authorization or an X- header");
            }
            if (lower.startsWith("x-mxr-") || lower.startsWith("x-mxraven-")) {
                throw new IllegalArgumentException("header must not use an mxRaven-owned prefix");
            }
            header = normalized;
        }
        this.header = header;
        this.value = value;
    }
}
