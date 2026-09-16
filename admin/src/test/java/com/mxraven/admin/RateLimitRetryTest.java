package com.mxraven.admin;

import com.mxraven.admin.internal.Java8;

import com.mxraven.admin.exception.RateLimitException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RateLimitRetryTest {
    private static HttpServer server;
    private static int port;
    private static final ConcurrentHashMap<String, AtomicInteger> CALLS = new ConcurrentHashMap<>();

    @BeforeAll
    static void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", RateLimitRetryTest::handle);
        server.start();
        port = server.getAddress().getPort();
    }

    @AfterAll
    static void stop() {
        server.stop(0);
    }

    private static void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String route = path.startsWith("/v2") ? path.substring(3) : path;
        int call = CALLS.computeIfAbsent(route, key -> new AtomicInteger()).incrementAndGet();
        switch (route) {
            case "/limited-once":
                if (call == 1) {
                    rateLimited(exchange, "0");
                } else {
                    respond(exchange, 200, "{}");
                }
                break;
            case "/limited-delete-once":
                if (call == 1) {
                    rateLimited(exchange, "0");
                } else {
                    exchange.sendResponseHeaders(204, -1);
                    exchange.close();
                }
                break;
            case "/always-exhausted":
            case "/always-disabled":
            case "/always-post":
            case "/always-put":
            case "/always-delete":
                rateLimited(exchange, "0");
                break;
            default:
                respond(exchange, 200, "{}");
        }
    }

    private static void rateLimited(HttpExchange exchange, String retryAfter) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/problem+json");
        exchange.getResponseHeaders().set("Retry-After", retryAfter);
        respond(exchange, 429, "{\"type\":\"urn:mxraven:problem:rate_limited\","
                + "\"title\":\"Too Many Requests\",\"status\":429,\"code\":\"rate_limited\","
                + "\"detail\":\"the tenant operation rate limit was exceeded\"}");
    }

    private static void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private static int calls(String path) {
        return CALLS.get(path).get();
    }

    private AdminClient client(RateLimitConfig config) {
        return new AdminClient("http://127.0.0.1:" + port, "token", config);
    }

    @Test
    void retriesOnRateLimitAndThenSucceeds() throws IOException {
        RateLimitConfig config = RateLimitConfig.builder()
                .maxRetries(2)
                .defaultBackoff(Duration.ZERO)
                .build();
        Response response = client(config).get("/limited-once");
        assertEquals(200, response.status());
        assertEquals(2, calls("/limited-once"));
    }

    @Test
    void throwsAfterRetriesAreExhausted() {
        RateLimitConfig config = RateLimitConfig.builder()
                .maxRetries(1)
                .defaultBackoff(Duration.ZERO)
                .build();
        RateLimitException error = assertThrows(RateLimitException.class,
                () -> client(config).get("/always-exhausted"));
        assertEquals(429, error.status());
        assertEquals("rate_limited", error.code());
        assertEquals(Duration.ZERO, error.retryAfter());
        assertEquals(2, calls("/always-exhausted"));
    }

    @Test
    void disabledSurfacesRateLimitImmediately() {
        assertThrows(RateLimitException.class, () -> client(RateLimitConfig.disabled()).get("/always-disabled"));
        assertEquals(1, calls("/always-disabled"));
    }

    @Test
    void retriesDeleteOnRateLimit() throws IOException {
        RateLimitConfig config = RateLimitConfig.builder()
                .maxRetries(2)
                .defaultBackoff(Duration.ZERO)
                .build();
        Response response = client(config).delete("/limited-delete-once");
        assertEquals(204, response.status());
        assertEquals(2, calls("/limited-delete-once"));
    }

    @Test
    void doesNotRetryPostOrPut() {
        RateLimitConfig config = RateLimitConfig.builder()
                .maxRetries(3)
                .defaultBackoff(Duration.ZERO)
                .build();
        assertThrows(RateLimitException.class,
                () -> client(config).post("/always-post", Java8.map("x", 1)));
        assertEquals(1, calls("/always-post"));
        assertThrows(RateLimitException.class,
                () -> client(config).put("/always-put", Java8.map("x", 1)));
        assertEquals(1, calls("/always-put"));
    }

    @Test
    void parsesRetryAfterSecondsAndDates() {
        assertEquals(Duration.ofSeconds(5), AdminClient.parseRetryAfter("5"));
        assertEquals(Duration.ZERO, AdminClient.parseRetryAfter("-3"));
        assertNull(AdminClient.parseRetryAfter("not-a-delay"));
        String date = ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(30)
                .format(DateTimeFormatter.RFC_1123_DATE_TIME);
        Duration fromDate = AdminClient.parseRetryAfter(date);
        assertTrue(fromDate.getSeconds() >= 25 && fromDate.getSeconds() <= 35);
    }
}
