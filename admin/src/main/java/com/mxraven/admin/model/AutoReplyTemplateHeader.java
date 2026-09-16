package com.mxraven.admin.model;

import java.util.List;

/**
 * A custom header stored on an auto-reply template.
 *
 * @param name header name
 * @param value header value
 */
public record AutoReplyTemplateHeader(
        String name,
        String value) {

    static void validateList(List<AutoReplyTemplateHeader> headers, boolean required) {
        if (headers == null) {
            if (required) {
                throw new IllegalArgumentException("headers is required");
            }
            return;
        }
        if (headers.size() > 10000) {
            throw new IllegalArgumentException("headers must contain 10000 entries or fewer");
        }
        for (int index = 0; index < headers.size(); index++) {
            AutoReplyTemplateHeader header = headers.get(index);
            if (header == null || header.name() == null || header.name().isBlank()) {
                throw new IllegalArgumentException("headers[" + index + "].name is required");
            }
            if (header.name().length() > 255) {
                throw new IllegalArgumentException("headers[" + index + "].name must be 255 characters or fewer");
            }
            for (int position = 0; position < header.name().length(); position++) {
                char character = header.name().charAt(position);
                if (character <= 32 || character >= 127 || character == ':') {
                    throw new IllegalArgumentException("headers[" + index + "].name is invalid");
                }
            }
            if (header.value() != null && header.value().length() > 1_048_576) {
                throw new IllegalArgumentException(
                        "headers[" + index + "].value must be 1048576 characters or fewer");
            }
        }
    }
}
