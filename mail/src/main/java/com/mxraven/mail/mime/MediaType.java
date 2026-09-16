package com.mxraven.mail.mime;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A parsed {@code Content-Type} / {@code Content-Disposition} value: the media
 * type (or disposition token) plus its parameters, with RFC 2231 extended
 * parameter values decoded.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
final class MediaType {
    private final String type;
    private final Map<String, String> parameters;

    public String type() {
        return type;
    }

    public Map<String, String> parameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MediaType that = (MediaType) o;
        return Objects.equals(this.type, that.type)
                && Objects.equals(this.parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.parameters);
    }

    @Override
    public String toString() {
        return "MediaType[" + "type=" + this.type + ", " + "parameters=" + this.parameters + "]";
    }

    /**
     * Creates a new MediaType.
     *
     * @param type
     * @param parameters
     */
    @JsonCreator
    public MediaType(String type, Map<String, String> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    private static final String DEFAULT_TYPE = "text/plain";

    static MediaType parse(String value) {
        if (value == null || Java8.isBlank(value)) {
            return new MediaType(DEFAULT_TYPE, Java8.map());
        }
        List<String> segments = splitTopLevel(value);
        String type = segments.get(0).trim().toLowerCase(Locale.ROOT);
        if (type.isEmpty()) {
            type = DEFAULT_TYPE;
        }

        Map<String, String> raw = new LinkedHashMap<>();
        for (int i = 1; i < segments.size(); i++) {
            String segment = segments.get(i).trim();
            int equals = segment.indexOf('=');
            if (equals <= 0) {
                continue;
            }
            String name = segment.substring(0, equals).trim().toLowerCase(Locale.ROOT);
            String parameter = segment.substring(equals + 1).trim();
            if (parameter.length() >= 2 && parameter.charAt(0) == '"' && parameter.endsWith("\"")) {
                parameter = unquote(parameter);
            }
            if (!name.isEmpty()) {
                raw.put(name, parameter);
            }
        }
        return new MediaType(type, decodeRfc2231(raw));
    }

    String parameter(String name) {
        return parameters.get(name.toLowerCase(Locale.ROOT));
    }

    /** Splits on {@code ';'} while ignoring separators inside quoted strings. */
    private static List<String> splitTopLevel(String value) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        boolean escaped = false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (escaped) {
                current.append(c);
                escaped = false;
            } else if (c == '\\' && quoted) {
                current.append(c);
                escaped = true;
            } else if (c == '"') {
                quoted = !quoted;
                current.append(c);
            } else if (c == ';' && !quoted) {
                out.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        out.add(current.toString());
        return out;
    }

    private static String unquote(String value) {
        StringBuilder out = new StringBuilder(value.length());
        for (int i = 1; i < value.length() - 1; i++) {
            char c = value.charAt(i);
            if (c == '\\' && i + 1 < value.length() - 1) {
                i++;
                out.append(value.charAt(i));
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    /**
     * Decodes RFC 2231 parameter values: {@code name*=charset'lang'pct} and the
     * continuation forms {@code name*0=}, {@code name*0*=}, {@code name*1=}, …
     */
    private static Map<String, String> decodeRfc2231(Map<String, String> raw) {
        Map<String, String> out = new LinkedHashMap<>();
        Map<String, StringBuilder> continuations = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : raw.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            int star = name.indexOf('*');
            if (star < 0) {
                out.put(name, value);
                continue;
            }
            String base = name.substring(0, star);
            String suffix = name.substring(star + 1);
            if (suffix.isEmpty()) {
                out.put(base, decodeExtendedValue(value));
                continue;
            }
            if (suffix.endsWith("*")) {
                value = decodeExtendedValue(value);
                suffix = suffix.substring(0, suffix.length() - 1);
            }
            continuations.computeIfAbsent(base, key -> new StringBuilder()).append(value);
        }
        for (Map.Entry<String, StringBuilder> entry : continuations.entrySet()) {
            out.put(entry.getKey(), entry.getValue().toString());
        }
        return out;
    }

    /** Decodes {@code charset'lang'percent-encoded} extended values. */
    private static String decodeExtendedValue(String value) {
        int firstQuote = value.indexOf('\'');
        if (firstQuote < 0) {
            return percentDecode(value);
        }
        int secondQuote = value.indexOf('\'', firstQuote + 1);
        if (secondQuote < 0) {
            return percentDecode(value);
        }
        String charset = value.substring(0, firstQuote);
        String encoded = value.substring(secondQuote + 1);
        byte[] bytes = percentDecodeBytes(encoded);
        return EncodedWords.decodeCharset(bytes, charset);
    }

    private static String percentDecode(String value) {
        return new String(percentDecodeBytes(value), java.nio.charset.StandardCharsets.UTF_8);
    }

    private static byte[] percentDecodeBytes(String value) {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '%' && i + 2 < value.length()) {
                int hi = Character.digit(value.charAt(i + 1), 16);
                int lo = Character.digit(value.charAt(i + 2), 16);
                if (hi >= 0 && lo >= 0) {
                    out.write((hi << 4) | lo);
                    i += 2;
                    continue;
                }
            }
            out.write(c & 0xff);
        }
        return out.toByteArray();
    }
}
