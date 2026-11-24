package com.mxraven.admin;

import com.mxraven.admin.model.IssuedWebhookEndpoint;
import com.mxraven.admin.model.SetWebhookEndpointCredentialRequest;
import com.mxraven.admin.model.WebhookEndpoint;
import com.mxraven.admin.model.WebhookEndpointData;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebhookCredentialsTest {
    private HttpServer server;
    private AdminClient client;

    private final AtomicReference<String> method = new AtomicReference<>();
    private final AtomicReference<String> path = new AtomicReference<>();
    private final AtomicReference<String> body = new AtomicReference<>();

    @BeforeEach
    void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", this::handle);
        server.start();
        client = new AdminClient("http://127.0.0.1:" + server.getAddress().getPort(), "token");
    }

    @AfterEach
    void stop() {
        server.stop(0);
    }

    private void handle(HttpExchange exchange) throws IOException {
        method.set(exchange.getRequestMethod());
        path.set(exchange.getRequestURI().getPath());
        body.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
        respond(exchange, 200, endpointJSON());
    }

    private WebhookEndpoint endpoint() {
        return new WebhookEndpoint(client, "/tenants/acme/webhook-endpoints/e1",
                new WebhookEndpointData("e1", "t1", "risk", "Risk service",
                        "https://risk.example/check", "whk_1", true,
                        "X-API-Key", true, "2026-01-01T00:00:00Z", "2026-01-01T00:00:00Z"));
    }

    @Test
    void setsAndRemovesAStoredCredential() throws Exception {
        WebhookEndpoint endpoint = endpoint();
        assertEquals("X-API-Key", endpoint.credentialHeader());

        WebhookEndpoint updated = endpoint.setCredential(
                SetWebhookEndpointCredentialRequest.of("X-API-Key", "sk-123"));
        assertEquals("PUT", method.get());
        assertEquals("/v2/tenants/acme/webhook-endpoints/e1/credential", path.get());
        assertEquals("{\"header\":\"X-API-Key\",\"value\":\"sk-123\"}", body.get());
        assertEquals("X-API-Key", updated.credentialHeader());

        updated.deleteCredential();
        assertEquals("DELETE", method.get());
        assertEquals("/v2/tenants/acme/webhook-endpoints/e1/credential", path.get());
    }

    @Test
    void omitsTheDefaultAuthorizationHeader() throws Exception {
        endpoint().setCredential(SetWebhookEndpointCredentialRequest.authorization("Bearer sk-123"));
        assertEquals("{\"value\":\"Bearer sk-123\"}", body.get());
    }

    @Test
    void validatesTheCredentialLocally() {
        assertThrows(IllegalArgumentException.class,
                () -> SetWebhookEndpointCredentialRequest.of("Authorization", ""));
        assertThrows(IllegalArgumentException.class,
                () -> SetWebhookEndpointCredentialRequest.of("X-API-Key", "line\r\nbreak"));
        assertThrows(IllegalArgumentException.class,
                () -> SetWebhookEndpointCredentialRequest.of("Cookie", "value"));
        assertThrows(IllegalArgumentException.class,
                () -> SetWebhookEndpointCredentialRequest.of("X-MxRaven-Signature", "value"));
    }

    private static void respond(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private static String endpointJSON() {
        return """
                {"id":"e1","tenant_id":"t1","webhook_ref":"risk","display_name":"Risk service",
                 "target_url":"https://risk.example/check","signing_kid":"whk_1",
                 "has_signing_secret":true,"credential_header":"X-API-Key","is_active":true,
                 "created_at":"2026-01-01T00:00:00Z","updated_at":"2026-01-01T00:00:00Z"}""";
    }
}
