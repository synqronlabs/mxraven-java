package com.mxraven.mail.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An ordered collection of header fields that preserves insertion order and
 * matches lookup names case-insensitively.
 */
public final class Headers {
    private final List<Header> fields = new ArrayList<>();

    /**
     * Appends a header field.
     *
     * @param header the field to append
     * @return this headers
     */
    public Headers add(Header header) {
        fields.add(header);
        return this;
    }

    /**
     * Appends a header field built from a name and value.
     *
     * @param name  the field name
     * @param value the field value
     * @return this headers
     */
    public Headers add(String name, String value) {
        return add(new Header(name, value));
    }

    /**
     * Returns the header fields in insertion order.
     *
     * @return an immutable copy of the fields
     */
    public List<Header> fields() {
        return List.copyOf(fields);
    }

    /**
     * Returns the value of the first field with the given name, ignoring case.
     *
     * @param name the field name
     * @return the first matching value, or empty when absent
     */
    public Optional<String> first(String name) {
        return fields.stream()
                .filter(h -> h.name().equalsIgnoreCase(name))
                .map(Header::value)
                .findFirst();
    }

    /**
     * Returns the values of every field with the given name, ignoring case.
     *
     * @param name the field name
     * @return the matching values in insertion order
     */
    public List<String> all(String name) {
        return fields.stream()
                .filter(h -> h.name().equalsIgnoreCase(name))
                .map(Header::value)
                .toList();
    }

    /**
     * Whether no header fields have been added.
     *
     * @return {@code true} when this collection is empty
     */
    public boolean isEmpty() {
        return fields.isEmpty();
    }

    /**
     * Parses a header block, unfolding continuation lines that start with a space
     * or tab.
     *
     * @param block the raw header block, or {@code null}
     * @return the parsed headers, empty when {@code block} is {@code null} or blank
     */
    public static Headers parse(String block) {
        Headers headers = new Headers();
        if (block == null || block.isBlank()) {
            return headers;
        }
        String[] lines = block.replace("\r\n", "\n").split("\n", -1);
        String currentName = null;
        StringBuilder currentValue = new StringBuilder();
        for (String line : lines) {
            if (line.isEmpty()) {
                break;
            }
            if (line.startsWith(" ") || line.startsWith("\t")) {
                if (currentName != null) {
                    currentValue.append(' ').append(line.trim());
                }
                continue;
            }
            if (currentName != null) {
                headers.add(currentName, currentValue.toString());
            }
            int colon = line.indexOf(':');
            if (colon <= 0) {
                currentName = null;
                currentValue = new StringBuilder();
                continue;
            }
            currentName = line.substring(0, colon).trim();
            currentValue = new StringBuilder(line.substring(colon + 1).trim());
        }
        if (currentName != null) {
            headers.add(currentName, currentValue.toString());
        }
        return headers;
    }
}
