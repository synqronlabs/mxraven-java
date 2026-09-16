package com.mxraven.mail.mime;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import com.mxraven.mail.model.Header;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * Encodes text and assembles MIME bodies for outbound messages.
 *
 * <p>Implements the pieces RFC 2045–2047 need to build a message: base64 and
 * quoted-printable transfer encodings, RFC 2047 encoded-words for header text,
 * RFC 2231 parameter values, and multipart framing with a generated boundary.
 */
public final class MimeWriter {
    private static final byte[] CRLF = {'\r', '\n'};
    private static final int BASE64_LINE = 76;
    private static final int QP_LINE = 75;

    private MimeWriter() {
    }

    /**
     * Generates a multipart boundary.
     *
     * @return a new random boundary value
     */
    public static String newBoundary() {
        return "----=_mxraven_" + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * One section of a multipart body: its headers and already-encoded body.
     */
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static final class Section {
        private final List<Header> headers;
        private final byte[] body;

        /** the section headers; a {@code null} value is replaced with an empty list */
        public List<Header> headers() {
            return headers;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Section that = (Section) o;
            return Objects.equals(this.headers, that.headers)
                    && Objects.equals(this.body, that.body);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.headers, this.body);
        }

        @Override
        public String toString() {
            return "Section[" + "headers=" + this.headers + ", " + "body=" + this.body + "]";
        }

        /**
         * Creates a section, copying the headers and body.
         */
        @JsonCreator
        public Section(List<Header> headers, byte[] body) {

            headers = headers == null ? Java8.list() : Java8.copyList(headers);
            body = body == null ? new byte[0] : body.clone();
        
            this.headers = headers;
            this.body = body;
        }

        public byte[] body() {
            return body.clone();
        }
    
    }

    /**
     * Whether every byte is US-ASCII.
     *
     * @param data the bytes to inspect
     * @return {@code true} when no byte has its high bit set
     */
    public static boolean isAscii(byte[] data) {
        for (byte b : data) {
            if ((b & 0x80) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Base64-encodes the given bytes with CRLF line breaks every 76 characters
     * (RFC 2045 §6.8).
     *
     * @param data the bytes to encode
     * @return the encoded bytes
     */
    public static byte[] base64(byte[] data) {
        String encoded = Base64.getEncoder().encodeToString(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream(encoded.length() + (encoded.length() / BASE64_LINE + 1) * 2);
        for (int i = 0; i < encoded.length(); i += BASE64_LINE) {
            int end = Math.min(i + BASE64_LINE, encoded.length());
            Java8.writeBytes(out, encoded.substring(i, end).getBytes(StandardCharsets.US_ASCII));
            Java8.writeBytes(out, CRLF);
        }
        return out.toByteArray();
    }

    /**
     * Quoted-printable-encodes the given bytes with soft line breaks
     * (RFC 2045 §6.7).
     *
     * @param data the bytes to encode
     * @return the encoded bytes
     */
    public static byte[] quotedPrintable(byte[] data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length + 16);
        int lineLength = 0;
        for (int i = 0; i < data.length; i++) {
            int b = data[i] & 0xff;
            if (b == '\r' && i + 1 < data.length && data[i + 1] == '\n') {
                Java8.writeBytes(out, CRLF);
                lineLength = 0;
                i++;
                continue;
            }
            String token;
            if ((b >= 0x21 && b <= 0x7e && b != '=') || b == ' ' || b == '\t') {
                token = String.valueOf((char) b);
            } else {
                token = "=" + String.format("%02X", b);
            }
            if (lineLength + token.length() > QP_LINE) {
                out.write('=');
                Java8.writeBytes(out, CRLF);
                lineLength = 0;
            }
            Java8.writeBytes(out, token.getBytes(StandardCharsets.US_ASCII));
            lineLength += token.length();
        }
        return out.toByteArray();
    }

    /**
     * Encodes header text as an RFC 2047 Base64 encoded-word when it contains
     * non-ASCII characters; otherwise returns it unchanged.
     *
     * @param value the header text, or {@code null}
     * @return the encoded text, or {@code value} unchanged when it is {@code null}
     *         or already ASCII
     */
    public static String encodeWord(String value) {
        if (value == null || isAscii(value.getBytes(StandardCharsets.UTF_8))) {
            return value;
        }
        String encoded = Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
        return "=?UTF-8?B?" + encoded + "?=";
    }

    /**
     * Formats a {@code Content-Disposition} value, using RFC 2231 extended syntax
     * when the filename is not plain ASCII. The returned value contains only the
     * disposition when the filename is {@code null} or empty.
     *
     * @param disposition the disposition token, for example {@code "attachment"}
     * @param filename    the filename, or {@code null}
     * @return the formatted disposition value
     */
    public static String contentDisposition(String disposition, String filename) {
        if (filename == null || filename.isEmpty()) {
            return disposition;
        }
        if (isAscii(filename.getBytes(StandardCharsets.UTF_8))) {
            return disposition + "; filename=\"" + filename.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
        }
        return disposition + "; filename*=UTF-8''" + percentEncode(filename);
    }

    private static String percentEncode(String value) {
        StringBuilder out = new StringBuilder();
        for (byte b : value.getBytes(StandardCharsets.UTF_8)) {
            int c = b & 0xff;
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')
                    || c == '-' || c == '_' || c == '.' || c == '~') {
                out.append((char) c);
            } else {
                out.append('%').append(String.format("%02X", c));
            }
        }
        return out.toString();
    }

    /**
     * Assembles sections into a multipart body with the given boundary, ending each
     * section with CRLF when its body does not already end in a newline.
     *
     * @param boundary the boundary value
     * @param sections the sections to write
     * @return the assembled multipart body
     */
    public static byte[] multipart(String boundary, List<Section> sections) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (Section section : sections) {
            writeAscii(out, "--" + boundary + "\r\n");
            for (Header header : section.headers()) {
                writeAscii(out, header.name() + ": " + header.value() + "\r\n");
            }
            writeAscii(out, "\r\n");
            byte[] body = section.body();
            Java8.writeBytes(out, body);
            if (body.length == 0 || body[body.length - 1] != '\n') {
                Java8.writeBytes(out, CRLF);
            }
        }
        writeAscii(out, "--" + boundary + "--\r\n");
        return out.toByteArray();
    }

    /**
     * Folds a long header value at whitespace near 78 columns. Values that already
     * contain a newline are returned unfolded.
     *
     * @param name  the header field name
     * @param value the header field value
     * @return the folded header, including the {@code "name: "} prefix
     */
    public static String foldHeader(String name, String value) {
        String prefix = name + ": ";
        if (prefix.length() + value.length() <= 78 || value.indexOf('\n') >= 0) {
            return prefix + value;
        }
        StringBuilder out = new StringBuilder(prefix);
        int lineLength = prefix.length();
        for (String word : value.split(" ")) {
            if (lineLength + 1 + word.length() > 78) {
                out.append("\r\n ");
                lineLength = 1;
            } else if (lineLength > prefix.length()) {
                out.append(' ');
                lineLength++;
            }
            out.append(word);
            lineLength += word.length();
        }
        return out.toString();
    }

    private static void writeAscii(ByteArrayOutputStream out, String value) {
        Java8.writeBytes(out, value.getBytes(StandardCharsets.US_ASCII));
    }
}
