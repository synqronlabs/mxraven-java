package com.mxraven.mail.mime;

import com.mxraven.mail.model.Headers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Parses an RFC 5322 / MIME message into a {@link MimePart} tree.
 *
 * <p>Multipart boundaries, {@code Content-Type} parameters (including RFC 2231),
 * transfer encodings, and charsets are handled. Bodies are decoded lazily by
 * {@link MimePart#decodedBody()} / {@link MimePart#text()}.
 */
public final class MimeParser {
    private MimeParser() {
    }

    /** Parses a complete raw message (headers and body). */
    public static MimePart parse(byte[] raw) {
        if (raw == null || raw.length == 0) {
            return new MimePart(new Headers(), "text/plain", "us-ascii",
                    ContentTransferEncoding.SEVEN_BIT, null, null, null, new byte[0], List.of());
        }
        return parsePart(raw);
    }

    private static MimePart parsePart(byte[] raw) {
        String source = MimePart.asLatin1(raw);
        int crlf = source.indexOf("\r\n\r\n");
        int lf = source.indexOf("\n\n");

        int headerEnd;
        int bodyStart;
        if (crlf >= 0 && (lf < 0 || crlf < lf)) {
            headerEnd = crlf;
            bodyStart = crlf + 4;
        } else if (lf >= 0) {
            headerEnd = lf;
            bodyStart = lf + 2;
        } else {
            headerEnd = source.length();
            bodyStart = source.length();
        }

        Headers headers = Headers.parse(source.substring(0, headerEnd));
        String body = source.substring(bodyStart);

        MediaType contentType = MediaType.parse(headers.first("Content-Type").orElse(null));
        String mediaType = contentType.type();
        String charset = contentType.parameter("charset");
        if (charset == null) {
            charset = MimePart.defaultCharset(mediaType);
        }

        ContentTransferEncoding encoding = parseEncoding(headers.first("Content-Transfer-Encoding").orElse(null));

        MediaType dispositionType = null;
        String disposition = null;
        var dispositionHeader = headers.first("Content-Disposition");
        if (dispositionHeader.isPresent()) {
            dispositionType = MediaType.parse(dispositionHeader.get());
            disposition = dispositionType.type();
        }

        String filename = firstNonBlank(
                dispositionType == null ? null : dispositionType.parameter("filename"),
                contentType.parameter("name"));
        if (filename != null) {
            filename = EncodedWords.decode(filename);
        }

        String contentId = headers.first("Content-ID")
                .map(value -> value.trim().replaceAll("^<|>$", ""))
                .filter(value -> !value.isEmpty())
                .orElse(null);

        List<MimePart> parts = List.of();
        if (mediaType.startsWith("multipart/")) {
            String boundary = contentType.parameter("boundary");
            if (boundary != null && !boundary.isBlank()) {
                List<String> sections = splitMultipart(body, boundary);
                List<MimePart> children = new ArrayList<>(sections.size());
                for (String section : sections) {
                    children.add(parsePart(MimePart.latin1(section)));
                }
                parts = List.copyOf(children);
            }
        }

        return new MimePart(headers, mediaType, charset, encoding,
                filename, contentId, disposition, MimePart.latin1(body), parts);
    }

    private static ContentTransferEncoding parseEncoding(String value) {
        if (value == null) {
            return ContentTransferEncoding.SEVEN_BIT;
        }
        String token = value.trim().toLowerCase(Locale.ROOT);
        int semicolon = token.indexOf(';');
        if (semicolon >= 0) {
            token = token.substring(0, semicolon).trim();
        }
        return switch (token) {
            case "base64" -> ContentTransferEncoding.BASE64;
            case "quoted-printable" -> ContentTransferEncoding.QUOTED_PRINTABLE;
            case "8bit" -> ContentTransferEncoding.EIGHT_BIT;
            case "binary" -> ContentTransferEncoding.BINARY;
            default -> ContentTransferEncoding.SEVEN_BIT;
        };
    }

    /**
     * Splits a multipart body into its raw part sections. Each returned section
     * contains the part headers and body, without the boundary delimiter lines.
     */
    private static List<String> splitMultipart(String body, String boundary) {
        String marker = "--" + boundary;
        List<int[]> marks = new ArrayList<>();

        int index = 0;
        int length = body.length();
        while (index < length) {
            int newline = body.indexOf('\n', index);
            int rawEnd = newline < 0 ? length : newline + 1;
            String line = body.substring(index, newline < 0 ? length : newline);
            if (line.endsWith("\r")) {
                line = line.substring(0, line.length() - 1);
            }
            if (line.startsWith(marker)) {
                String rest = line.substring(marker.length()).trim();
                if (rest.isEmpty() || rest.startsWith("--")) {
                    marks.add(new int[]{index, rawEnd, rest.startsWith("--") ? 1 : 0});
                }
            }
            index = rawEnd;
        }

        List<String> sections = new ArrayList<>();
        for (int i = 0; i < marks.size(); i++) {
            if (marks.get(i)[2] == 1) {
                break;
            }
            int start = marks.get(i)[1];
            int end = i + 1 < marks.size() ? marks.get(i + 1)[0] : length;
            if (end < start) {
                continue;
            }
            String section = body.substring(start, end);
            // The CRLF before a boundary delimiter belongs to the delimiter.
            if (section.endsWith("\r\n")) {
                section = section.substring(0, section.length() - 2);
            } else if (section.endsWith("\n")) {
                section = section.substring(0, section.length() - 1);
            }
            sections.add(section);
        }
        return sections;
    }

    private static String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        return second != null && !second.isBlank() ? second : null;
    }
}
