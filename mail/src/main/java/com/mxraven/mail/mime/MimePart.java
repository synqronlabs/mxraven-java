package com.mxraven.mail.mime;

import com.mxraven.mail.model.Headers;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * One MIME entity parsed from a message: its headers, decoded metadata, the raw
 * transfer-encoded body, and any child parts.
 *
 * <p>Use {@link #decodedBody()} for the transfer-decoded bytes and {@link #text()}
 * for a charset-decoded text body. Multipart entities have {@link #parts()}.
 */
public final class MimePart {
    private final Headers headers;
    private final String mediaType;
    private final String charset;
    private final ContentTransferEncoding encoding;
    private final String filename;
    private final String contentId;
    private final String disposition;
    private final byte[] rawBody;
    private final List<MimePart> parts;

    MimePart(Headers headers, String mediaType, String charset, ContentTransferEncoding encoding,
             String filename, String contentId, String disposition, byte[] rawBody, List<MimePart> parts) {
        this.headers = headers;
        this.mediaType = mediaType;
        this.charset = charset;
        this.encoding = encoding;
        this.filename = filename;
        this.contentId = contentId;
        this.disposition = disposition;
        this.rawBody = rawBody;
        this.parts = parts;
    }

    /**
     * The part's headers.
     *
     * @return the headers
     */
    public Headers headers() {
        return headers;
    }

    /**
     * The lowercased media type, for example {@code "text/html"}.
     *
     * @return the media type
     */
    public String mediaType() {
        return mediaType;
    }

    /**
     * The declared charset, or {@code null} when the part did not declare one.
     *
     * @return the charset, or {@code null}
     */
    public String charset() {
        return charset;
    }

    /**
     * The declared content transfer encoding.
     *
     * @return the content transfer encoding
     */
    public ContentTransferEncoding encoding() {
        return encoding;
    }

    /**
     * The attachment filename, if any.
     *
     * @return the filename, or empty
     */
    public Optional<String> filename() {
        return Optional.ofNullable(filename);
    }

    /**
     * The {@code Content-ID} value without angle brackets, if any.
     *
     * @return the content identifier, or empty
     */
    public Optional<String> contentId() {
        return Optional.ofNullable(contentId);
    }

    /**
     * The content disposition token, for example {@code "attachment"} or
     * {@code "inline"}.
     *
     * @return the disposition token, or empty
     */
    public Optional<String> disposition() {
        return Optional.ofNullable(disposition);
    }

    /**
     * Whether this part is multipart and has children.
     *
     * @return {@code true} when this part has child parts
     */
    public boolean isMultipart() {
        return !parts.isEmpty();
    }

    /**
     * Child parts of a multipart entity, in order.
     *
     * @return the child parts
     */
    public List<MimePart> parts() {
        return parts;
    }

    /**
     * The raw, still transfer-encoded body bytes.
     *
     * @return a copy of the raw body bytes
     */
    public byte[] rawBody() {
        return rawBody.clone();
    }

    /**
     * The transfer-decoded body bytes.
     *
     * @return a copy of the decoded body bytes
     */
    public byte[] decodedBody() {
        return switch (encoding) {
            case BASE64 -> Base64.getMimeDecoder().decode(rawBody);
            case QUOTED_PRINTABLE -> decodeQuotedPrintable(rawBody);
            default -> rawBody.clone();
        };
    }

    /**
     * The decoded body interpreted as text using the declared charset.
     *
     * @return the decoded text
     */
    public String text() {
        return EncodedWords.decodeCharset(decodedBody(), charset);
    }

    /**
     * Whether this part is an attachment: an explicit {@code attachment}
     * disposition or any part carrying a filename.
     *
     * @return {@code true} when this part is treated as an attachment
     */
    public boolean isAttachment() {
        if (disposition != null && disposition.equalsIgnoreCase("attachment")) {
            return true;
        }
        return filename != null && !isTextBody();
    }

    /**
     * Whether this part is a body text part ({@code text/plain} or
     * {@code text/html}).
     *
     * @return {@code true} when this part is {@code text/plain} or {@code text/html}
     */
    public boolean isTextBody() {
        String type = mediaType.toLowerCase(Locale.ROOT);
        return type.equals("text/plain") || type.equals("text/html");
    }

    private static byte[] decodeQuotedPrintable(byte[] data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            if (b == '=' && i + 1 < data.length) {
                byte next = data[i + 1];
                if (next == '\r' || next == '\n') {
                    // Soft line break: skip the '=' and the line ending.
                    i++;
                    if (next == '\r' && i + 1 < data.length && data[i + 1] == '\n') {
                        i++;
                    }
                    continue;
                }
                if (i + 2 < data.length) {
                    int hi = Character.digit(data[i + 1], 16);
                    int lo = Character.digit(data[i + 2], 16);
                    if (hi >= 0 && lo >= 0) {
                        out.write((hi << 4) | lo);
                        i += 2;
                        continue;
                    }
                }
            }
            out.write(b);
        }
        return out.toByteArray();
    }

    static String defaultCharset(String mediaType) {
        return mediaType.toLowerCase(Locale.ROOT).startsWith("text/") ? "us-ascii" : null;
    }

    static String asLatin1(byte[] data) {
        return new String(data, StandardCharsets.ISO_8859_1);
    }

    static byte[] latin1(String value) {
        return value.getBytes(StandardCharsets.ISO_8859_1);
    }
}
