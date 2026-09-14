package com.mxraven.mail.webhook;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RawEmailTest {
    private static final byte[] MESSAGE =
            "From: a@example.com\r\nSubject: sample\r\n\r\nbody\r\n".getBytes(StandardCharsets.UTF_8);

    private static String sha256(byte[] data) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(data));
    }

    private static HttpServer server(int status, byte[] body) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/raw", exchange -> {
            exchange.sendResponseHeaders(status, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
        return server;
    }

    private static String url(HttpServer server) {
        return "http://127.0.0.1:" + server.getAddress().getPort() + "/raw";
    }

    @Test
    void downloadsAndVerifiesTheMessage() throws Exception {
        HttpServer server = server(200, MESSAGE);
        try {
            RawEmail raw = new RawEmail(url(server), "Bearer", "token", 0,
                    MESSAGE.length, sha256(MESSAGE), "message/rfc822");

            assertArrayEquals(MESSAGE, raw.fetch());
        } finally {
            server.stop(0);
        }
    }

    @Test
    void rejectsADigestMismatch() throws Exception {
        HttpServer server = server(200, MESSAGE);
        try {
            RawEmail raw = new RawEmail(url(server), "Bearer", "token", 0,
                    MESSAGE.length, "deadbeef", null);

            assertThrows(WebhookException.class, raw::fetch);
        } finally {
            server.stop(0);
        }
    }

    @Test
    void rejectsASizeMismatch() throws Exception {
        HttpServer server = server(200, MESSAGE);
        try {
            RawEmail raw = new RawEmail(url(server), "Bearer", "token", 0,
                    MESSAGE.length + 1, sha256(MESSAGE), null);

            assertThrows(WebhookException.class, raw::fetch);
        } finally {
            server.stop(0);
        }
    }

    @Test
    void rejectsANonSuccessResponse() throws Exception {
        HttpServer server = server(403, new byte[0]);
        try {
            RawEmail raw = new RawEmail(url(server), "Bearer", "token", 0, 0, null, null);

            assertThrows(WebhookException.class, raw::fetch);
        } finally {
            server.stop(0);
        }
    }

    @Test
    void requiresUrlAndToken() {
        RawEmail noUrl = new RawEmail("", "Bearer", "token", 0, 0, null, null);
        RawEmail noToken = new RawEmail("https://raw.example.com/m.eml", "Bearer", "", 0, 0, null, null);

        assertThrows(WebhookException.class, noUrl::fetch);
        assertThrows(WebhookException.class, noToken::fetch);
    }
}
