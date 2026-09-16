package com.mxraven.mail.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Internal compatibility helpers that back the Java 9+ APIs used by this SDK
 * with Java 8 equivalents.
 *
 * <p>This type is an implementation detail and is not part of the public API.
 */
public final class Java8 {
    private Java8() {
    }

    /**
     * Returns whether a character sequence is {@code null}, empty, or contains
     * only Unicode whitespace. Mirrors {@code String.isBlank()}.
     *
     * @param value the value to test, may be {@code null}
     * @return {@code true} when the value is blank
     */
    public static boolean isBlank(CharSequence value) {
        if (value == null) {
            return true;
        }
        int length = value.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether a character sequence is not blank.
     *
     * @param value the value to test, may be {@code null}
     * @return {@code true} when the value has a non-whitespace character
     */
    public static boolean isNotBlank(CharSequence value) {
        return !isBlank(value);
    }

    /**
     * Repeats a string a number of times. Backs {@code String.repeat(int)}.
     *
     * @param value the value to repeat
     * @param count the number of repetitions; must not be negative
     * @return the repeated value
     */
    public static String repeat(String value, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must not be negative");
        }
        StringBuilder out = new StringBuilder(value.length() * count);
        for (int i = 0; i < count; i++) {
            out.append(value);
        }
        return out.toString();
    }

    /**
     * URL-encodes a value using UTF-8. Backs {@code URLEncoder.encode(String, Charset)}.
     *
     * @param value the value to encode
     * @return the encoded value
     */
    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 is not available", e);
        }
    }

    /**
     * Reads all remaining bytes from a stream. Backs {@code InputStream.readAllBytes()}.
     *
     * @param input the stream to read
     * @return the bytes read
     * @throws IOException when the read fails
     */
    public static byte[] readAllBytes(InputStream input) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = input.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        return out.toByteArray();
    }

    /**
     * Writes all bytes to a byte-array stream without a checked exception.
     * Backs {@code ByteArrayOutputStream.writeBytes()}.
     *
     * @param out the target stream
     * @param bytes the bytes to write; may be empty
     */
    public static void writeBytes(ByteArrayOutputStream out, byte[] bytes) {
        out.write(bytes, 0, bytes.length);
    }

    /**
     * Formats bytes as lowercase hexadecimal. Backs {@code HexFormat.formatHex()}.
     *
     * @param bytes the bytes to format
     * @return the lowercase hexadecimal string
     */
    public static String toHex(byte[] bytes) {
        char[] hex = "0123456789abcdef".toCharArray();
        char[] out = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xff;
            out[i * 2] = hex[value >>> 4];
            out[i * 2 + 1] = hex[value & 0x0f];
        }
        return new String(out);
    }

    /**
     * Parses a hexadecimal string into bytes. Backs {@code HexFormat.parseHex()}.
     *
     * @param hex the hexadecimal string; case-insensitive
     * @return the decoded bytes
     * @throws IllegalArgumentException when the length is odd or a character is not hexadecimal
     */
    public static byte[] fromHex(String hex) {
        int length = hex.length();
        if ((length & 1) != 0) {
            throw new IllegalArgumentException("hex string must have an even length");
        }
        byte[] out = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            int high = Character.digit(hex.charAt(i), 16);
            int low = Character.digit(hex.charAt(i + 1), 16);
            if (high < 0 || low < 0) {
                throw new IllegalArgumentException("invalid hexadecimal character");
            }
            out[i / 2] = (byte) ((high << 4) | low);
        }
        return out;
    }

    /**
     * Returns an unmodifiable list of the given elements. Backs {@code List.of()}.
     *
     * @param elements the elements; {@code null} elements are rejected
     * @param <T> element type
     * @return an unmodifiable list
     */
    @SafeVarargs
    public static <T> List<T> list(T... elements) {
        List<T> out = new ArrayList<>(elements.length);
        for (T element : elements) {
            if (element == null) {
                throw new NullPointerException("element must not be null");
            }
            out.add(element);
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * Returns an empty unmodifiable list. Backs {@code List.of()}.
     *
     * @param <T> element type
     * @return an empty unmodifiable list
     */
    public static <T> List<T> list() {
        return Collections.emptyList();
    }

    /**
     * Returns an unmodifiable copy of a collection. Backs {@code List.copyOf()}.
     *
     * @param collection the collection to copy; {@code null} entries are rejected
     * @param <T> element type
     * @return an unmodifiable copy
     */
    public static <T> List<T> copyList(Collection<? extends T> collection) {
        if (collection == null) {
            throw new NullPointerException("collection must not be null");
        }
        List<T> out = new ArrayList<>(collection.size());
        for (T element : collection) {
            if (element == null) {
                throw new NullPointerException("element must not be null");
            }
            out.add(element);
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * Returns an empty unmodifiable map. Backs {@code Map.of()}.
     *
     * @param <K> key type
     * @param <V> value type
     * @return an empty unmodifiable map
     */
    public static <K, V> Map<K, V> map() {
        return Collections.emptyMap();
    }

    /**
     * Returns an unmodifiable single-entry map. Backs {@code Map.of(k, v)}.
     *
     * @param key the key; must not be {@code null}
     * @param value the value; must not be {@code null}
     * @param <K> key type
     * @param <V> value type
     * @return an unmodifiable map
     */
    public static <K, V> Map<K, V> map(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key and value must not be null");
        }
        Map<K, V> out = new LinkedHashMap<>();
        out.put(key, value);
        return Collections.unmodifiableMap(out);
    }

    /**
     * Returns an unmodifiable copy of a map. Backs {@code Map.copyOf()}.
     *
     * @param source the map to copy; {@code null} keys and values are rejected
     * @param <K> key type
     * @param <V> value type
     * @return an unmodifiable copy
     */
    public static <K, V> Map<K, V> copyMap(Map<? extends K, ? extends V> source) {
        if (source == null) {
            throw new NullPointerException("map must not be null");
        }
        Map<K, V> out = new LinkedHashMap<>();
        for (Map.Entry<? extends K, ? extends V> entry : source.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                throw new NullPointerException("key and value must not be null");
            }
            out.put(entry.getKey(), entry.getValue());
        }
        return Collections.unmodifiableMap(out);
    }
}
