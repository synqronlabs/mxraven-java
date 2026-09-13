package com.mxraven.mail.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeadersTest {

    @Test
    void lookupIsCaseInsensitive() {
        Headers headers = new Headers().add("Subject", "Hello");
        assertEquals(Optional.of("Hello"), headers.first("subject"));
        assertEquals(Optional.of("Hello"), headers.first("SUBJECT"));
        assertTrue(headers.first("From").isEmpty());
    }

    @Test
    void returnsAllValuesInOrder() {
        Headers headers = new Headers()
                .add("Received", "one")
                .add("Received", "two");
        assertEquals(List.of("one", "two"), headers.all("received"));
    }

    @Test
    void parsesFoldedHeadersUntilTheBlankLine() {
        String block = "Subject: hello\r\n world\r\nFrom: a@b.com\r\n\r\nbody";
        Headers headers = Headers.parse(block);
        assertEquals("hello world", headers.first("Subject").orElseThrow());
        assertEquals("a@b.com", headers.first("From").orElseThrow());
        assertEquals(2, headers.fields().size());
    }

    @Test
    void parsesEmptyInputAsNoHeaders() {
        assertTrue(Headers.parse(null).isEmpty());
        assertTrue(Headers.parse("").isEmpty());
        assertTrue(Headers.parse("  ").isEmpty());
    }

    @Test
    void ignoresLinesWithoutAColon() {
        Headers headers = Headers.parse("not-a-header\r\nX: y");
        assertEquals("y", headers.first("X").orElseThrow());
        assertEquals(1, headers.fields().size());
    }
}
