package com.mxraven.mail.feedback;

import com.mxraven.mail.internal.Java8;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeedbackClientTest {
    private static final String USERNAME = "mxr_tx_ab12cd34ef56";
    private static final String SECRET = "supersecret";
    private static final byte[] RAW_MIME =
            "From: a@example.com\r\nSubject: sample\r\n\r\nbody\r\n".getBytes(StandardCharsets.UTF_8);

    private static HttpServer server(HttpHandler handler) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", handler);
        server.start();
        return server;
    }

    private static String baseUrl(HttpServer server) {
        return "http://127.0.0.1:" + server.getAddress().getPort();
    }

    private static void respond(HttpExchange exchange, int status, String json) throws IOException {
        byte[] payload = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, payload.length);
        exchange.getResponseBody().write(payload);
        exchange.close();
    }

    private static byte[] body(HttpExchange exchange) throws IOException {
        return Java8.readAllBytes(exchange.getRequestBody());
    }

    @Test
    void builderValidatesConfiguration() {
        assertThrows(IllegalStateException.class, () -> FeedbackClient.builder().build());
        assertThrows(IllegalArgumentException.class, () -> FeedbackClient.builder().baseUrl(""));
        assertThrows(IllegalArgumentException.class, () -> FeedbackClient.builder().credentials("", SECRET));
        assertThrows(IllegalArgumentException.class, () -> FeedbackClient.builder().credentials(USERNAME, ""));
        assertThrows(IllegalArgumentException.class, () -> FeedbackClient.builder().httpClient(null));
    }

    @Test
    void learnSpamPostsTheRawMessageWithBasicAuth() throws Exception {
        AtomicReference<String> method = new AtomicReference<>();
        AtomicReference<String> path = new AtomicReference<>();
        AtomicReference<String> contentType = new AtomicReference<>();
        AtomicReference<String> auth = new AtomicReference<>();
        AtomicReference<byte[]> submitted = new AtomicReference<>();

        HttpServer server = server(exchange -> {
            method.set(exchange.getRequestMethod());
            path.set(exchange.getRequestURI().getPath());
            contentType.set(exchange.getRequestHeaders().getFirst("Content-Type"));
            auth.set(exchange.getRequestHeaders().getFirst("Authorization"));
            submitted.set(body(exchange));
            respond(exchange, 200, "{\"status\":\"learned\",\"disposition\":\"spam\","
                    + "\"tenant_id\":\"t1\",\"listener_id\":\"l1\",\"matched_hash_kind\":\"rendered_eml_sha256\"}");
        });
        try {
            FeedbackClient client = FeedbackClient.builder()
                    .baseUrl(baseUrl(server))
                    .credentials(USERNAME, SECRET)
                    .build();

            LearningResult result = client.learnSpam(RAW_MIME);

            assertEquals("POST", method.get());
            assertEquals("/v1/feedback/learn/spam", path.get());
            assertEquals("message/rfc822", contentType.get());
            assertEquals("Basic " + Base64.getEncoder()
                    .encodeToString((USERNAME + ":" + SECRET).getBytes(StandardCharsets.UTF_8)), auth.get());
            assertArrayEquals(RAW_MIME, submitted.get());

            assertEquals("learned", result.status());
            assertEquals(Disposition.SPAM, result.disposition());
            assertEquals("l1", result.listenerId());
            assertEquals("rendered_eml_sha256", result.matchedHashKind());
        } finally {
            server.stop(0);
        }
    }

    @Test
    void learnHamUsesTheHamEndpoint() throws Exception {
        AtomicReference<String> path = new AtomicReference<>();
        HttpServer server = server(exchange -> {
            path.set(exchange.getRequestURI().getPath());
            respond(exchange, 200, "{\"status\":\"learned\",\"disposition\":\"ham\"}");
        });
        try {
            FeedbackClient client = FeedbackClient.builder()
                    .baseUrl(baseUrl(server))
                    .credentials(USERNAME, SECRET)
                    .build();

            assertEquals(Disposition.HAM, client.learnHam(RAW_MIME).disposition());
            assertEquals("/v1/feedback/learn/ham", path.get());
        } finally {
            server.stop(0);
        }
    }

    @Test
    void surfacesServiceErrorsAsTypedExceptions() throws Exception {
        HttpServer server = server(exchange ->
                respond(exchange, 404, "{\"error\":\"message evidence not found\"}"));
        try {
            FeedbackClient client = FeedbackClient.builder()
                    .baseUrl(baseUrl(server))
                    .credentials(USERNAME, SECRET)
                    .build();

            FeedbackException error = assertThrows(FeedbackException.class, () -> client.learnSpam(RAW_MIME));
            assertEquals(404, error.statusCode());
            assertEquals("message evidence not found", error.detail());
            assertFalse(error.retryable());
        } finally {
            server.stop(0);
        }
    }

    @Test
    void reportsRateLimitsAsRetryable() throws Exception {
        HttpServer server = server(exchange ->
                respond(exchange, 429, "{\"error\":\"learning rate limit exceeded\"}"));
        try {
            FeedbackClient client = FeedbackClient.builder()
                    .baseUrl(baseUrl(server))
                    .credentials(USERNAME, SECRET)
                    .build();

            FeedbackException error = assertThrows(FeedbackException.class, () -> client.learnSpam(RAW_MIME));
            assertEquals(429, error.statusCode());
            assertTrue(error.retryable());
        } finally {
            server.stop(0);
        }
    }

    @Test
    void unsubscribePostsTheOneClickBody() throws Exception {
        AtomicReference<String> method = new AtomicReference<>();
        AtomicReference<String> path = new AtomicReference<>();
        AtomicReference<String> contentType = new AtomicReference<>();
        AtomicReference<byte[]> submitted = new AtomicReference<>();

        HttpServer server = server(exchange -> {
            method.set(exchange.getRequestMethod());
            path.set(exchange.getRequestURI().getPath());
            contentType.set(exchange.getRequestHeaders().getFirst("Content-Type"));
            submitted.set(body(exchange));
            respond(exchange, 200, "{\"status\":\"unsubscribed\"}");
        });
        try {
            FeedbackClient client = FeedbackClient.builder().baseUrl(baseUrl(server)).build();

            client.unsubscribe("header.payload.signature");

            assertEquals("POST", method.get());
            assertEquals("/v1/feedback/unsubscribe/header.payload.signature", path.get());
            assertEquals("application/x-www-form-urlencoded", contentType.get());
            assertEquals("List-Unsubscribe=One-Click", new String(submitted.get(), StandardCharsets.UTF_8));
        } finally {
            server.stop(0);
        }
    }

    @Test
    void learningRequiresCredentials() {
        FeedbackClient client = FeedbackClient.builder().baseUrl("https://feedback.example.com").build();

        assertThrows(IllegalStateException.class, () -> client.learnSpam(RAW_MIME));
        assertThrows(IllegalArgumentException.class, () -> client.learn(Disposition.SPAM, (byte[]) null));
        assertThrows(IllegalArgumentException.class, () -> client.learn(null, RAW_MIME));
        assertThrows(IllegalArgumentException.class, () -> client.unsubscribe("  "));
    }
}
