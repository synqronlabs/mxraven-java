package com.mxraven.mail.model;

import com.mxraven.mail.mime.ContentTransferEncoding;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * RFC 5322 content: the header block plus the (already transfer-encoded) body.
 *
 * <p>{@link #toRaw()} serializes the content to wire bytes. Note that the body
 * is written as-is; apply the transfer encoding when building the message (as
 * {@link MailBuilder} does) rather than here.
 *
 * @param headers  the header block; a {@code null} value is replaced with an empty
 *                 {@link Headers}
 * @param body     the transfer-encoded body bytes; a {@code null} value is replaced
 *                 with an empty array
 * @param encoding the applied content transfer encoding; a {@code null} value is
 *                 replaced with {@link ContentTransferEncoding#SEVEN_BIT}
 * @param charset  the declared character set, or {@code null}
 */
public record Content(Headers headers, byte[] body, ContentTransferEncoding encoding, String charset) {
    /**
     * Creates content, replacing {@code null} fields with empty defaults.
     */
    public Content {
        headers = headers == null ? new Headers() : headers;
        body = body == null ? new byte[0] : body;
        encoding = encoding == null ? ContentTransferEncoding.SEVEN_BIT : encoding;
    }

    /**
     * Serializes the header block and body to raw message bytes.
     *
     * @return the serialized message, with CRLF line endings between fields
     */
    public byte[] toRaw() {
        ByteArrayOutputStream out = new ByteArrayOutputStream(body.length + 256);
        for (Header header : headers.fields()) {
            out.writeBytes((header.name() + ": " + header.value() + "\r\n")
                    .getBytes(StandardCharsets.ISO_8859_1));
        }
        out.writeBytes("\r\n".getBytes(StandardCharsets.ISO_8859_1));
        out.writeBytes(body);
        return out.toByteArray();
    }
}
