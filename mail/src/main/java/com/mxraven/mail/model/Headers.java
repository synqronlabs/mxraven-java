package com.mxraven.mail.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Headers {
    private final List<Header> fields = new ArrayList<>();

    public Headers add(Header header) {
        fields.add(header);
        return this;
    }

    public Headers add(String name, String value) {
        return add(new Header(name, value));
    }

    public List<Header> fields() {
        return List.copyOf(fields);
    }

    public Optional<String> first(String name) {
        return fields.stream()
                .filter(h -> h.name().equalsIgnoreCase(name))
                .map(Header::value)
                .findFirst();
    }

    public List<String> all(String name) {
        return fields.stream()
                .filter(h -> h.name().equalsIgnoreCase(name))
                .map(Header::value)
                .toList();
    }

    public boolean isEmpty() {
        return fields.isEmpty();
    }

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
