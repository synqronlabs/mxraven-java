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
 */
public record Content(Headers headers, byte[] body, ContentTransferEncoding encoding, String charset) {
    public Content {
        headers = headers == null ? new Headers() : headers;
        body = body == null ? new byte[0] : body;
        encoding = encoding == null ? ContentTransferEncoding.SEVEN_BIT : encoding;
    }

    /** Serializes the header block and body to raw message bytes. */
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
