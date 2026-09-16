package com.mxraven.mail.webhook;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.mxraven.mail.webhook.WebhookTestSupport.INBOUND_BODY;
import static com.mxraven.mail.webhook.WebhookTestSupport.SECRET;
import static com.mxraven.mail.webhook.WebhookTestSupport.bytes;
import static com.mxraven.mail.webhook.WebhookTestSupport.signedHeaders;

class WebhookServerTest {

    private static String now() {
        return String.valueOf(Instant.now().getEpochSecond());
    }

    private static WebhookServer server(WebhookListener listener) {
        WebhookHandler handler = WebhookHandler.builder()
                .verifier(WebhookVerifier.builder().secret(SECRET).build())
                .listener(listener)
                .build();
        return WebhookServer.builder()
                .port(0)
                .path("/mxraven/webhook")
                .handler(handler)
                .build();
    }

    private static int post(URI uri, Map<String, String> headers, byte[] body) throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(uri.toString())
                .post(RequestBody.create(body, null));
        headers.forEach(builder::header);
        try (okhttp3.Response response = new OkHttpClient().newCall(builder.build()).execute()) {
            return response.code();
        }
    }

    @Test
    void servesVerifiedWebhooksOverHttp() throws Exception {
        List<WebhookEvent> received = new CopyOnWriteArrayList<>();

        try (WebhookServer server = server(received::add)) {
            server.start();
            URI uri = URI.create("http://127.0.0.1:" + server.port() + "/mxraven/webhook");
            byte[] body = bytes(INBOUND_BODY);
            Map<String, String> headers = signedHeaders("POST", uri, body, now(), "task-123", SECRET);

            int status = post(uri, headers, body);

            assertEquals(204, status);
            assertEquals(1, received.size());
            assertInstanceOf(InboundEmail.class, received.get(0));
        }
    }

    @Test
    void rejectsTamperedRequests() throws Exception {
        List<WebhookEvent> received = new CopyOnWriteArrayList<>();

        try (WebhookServer server = server(received::add)) {
            server.start();
            URI uri = URI.create("http://127.0.0.1:" + server.port() + "/mxraven/webhook");
            byte[] body = bytes(INBOUND_BODY);
            Map<String, String> headers = signedHeaders("POST", uri, body, now(), "task-123", SECRET);

            int status = post(uri, headers, bytes(INBOUND_BODY + " "));

            assertEquals(401, status);
            assertEquals(0, received.size());
        }
    }

    @Test
    void printsADevelopmentBannerByDefault() throws Exception {
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream original = System.err;
        System.setErr(new PrintStream(captured, true, StandardCharsets.UTF_8.name()));
        try (WebhookServer server = server(event -> { })) {
            server.start();
        } finally {
            System.setErr(original);
        }

        String output = new String(captured.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(output.contains("Running on http://127.0.0.1:"), output);
        assertTrue(output.contains("development server"), output);
    }

    @Test
    void bannerCanBeDisabled() throws Exception {
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream original = System.err;
        System.setErr(new PrintStream(captured, true, StandardCharsets.UTF_8.name()));
        try (WebhookServer server = WebhookServer.builder()
                .port(0)
                .path("/mxraven/webhook")
                .handler(WebhookHandler.builder()
                        .verifier(WebhookVerifier.builder().secret(SECRET).build())
                        .listener(event -> { })
                        .build())
                .banner(false)
                .build()) {
            server.start();
        } finally {
            System.setErr(original);
        }

        assertEquals("", new String(captured.toByteArray(), StandardCharsets.UTF_8));
    }
}
