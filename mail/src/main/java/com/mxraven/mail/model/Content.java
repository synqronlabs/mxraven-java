package com.mxraven.mail.model;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

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
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Content {
    private final Headers headers;
    private final byte[] body;
    private final ContentTransferEncoding encoding;
    private final String charset;

    /** the header block; a {@code null} value is replaced with an empty {@link Headers} */
    public Headers headers() {
        return headers;
    }

    /** the transfer-encoded body bytes; a {@code null} value is replaced with an empty array */
    public byte[] body() {
        return body;
    }

    /** the applied content transfer encoding; a {@code null} value is replaced with {@link ContentTransferEncoding#SEVEN_BIT} */
    public ContentTransferEncoding encoding() {
        return encoding;
    }

    /** the declared character set, or {@code null} */
    public String charset() {
        return charset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Content that = (Content) o;
        return Objects.equals(this.headers, that.headers)
                && Objects.equals(this.body, that.body)
                && Objects.equals(this.encoding, that.encoding)
                && Objects.equals(this.charset, that.charset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.headers, this.body, this.encoding, this.charset);
    }

    @Override
    public String toString() {
        return "Content[" + "headers=" + this.headers + ", " + "body=" + this.body + ", " + "encoding=" + this.encoding + ", " + "charset=" + this.charset + "]";
    }

    /**
     * Creates content, replacing {@code null} fields with empty defaults.
     */
    @JsonCreator
    public Content(Headers headers, byte[] body, ContentTransferEncoding encoding, String charset) {

        headers = headers == null ? new Headers() : headers;
        body = body == null ? new byte[0] : body;
        encoding = encoding == null ? ContentTransferEncoding.SEVEN_BIT : encoding;
    
        this.headers = headers;
        this.body = body;
        this.encoding = encoding;
        this.charset = charset;
    }

    /**
     * Serializes the header block and body to raw message bytes.
     *
     * @return the serialized message, with CRLF line endings between fields
     */
    public byte[] toRaw() {
        ByteArrayOutputStream out = new ByteArrayOutputStream(body.length + 256);
        for (Header header : headers.fields()) {
            Java8.writeBytes(out, (header.name() + ": " + header.value() + "\r\n")
                    .getBytes(StandardCharsets.ISO_8859_1));
        }
        Java8.writeBytes(out, "\r\n".getBytes(StandardCharsets.ISO_8859_1));
        Java8.writeBytes(out, body);
        return out.toByteArray();
    }
}
