package com.mxraven.admin;

import com.mxraven.admin.internal.Java8;

import com.mxraven.admin.exception.ApiException;
import com.mxraven.admin.model.Domain;
import com.mxraven.admin.model.ListenerType;
import com.mxraven.admin.model.MTARateLimitPolicy;
import com.mxraven.admin.model.SmtpRelay;
import com.mxraven.admin.model.StreamType;
import com.mxraven.admin.model.TerminalActionType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminClientTest {
    private static HttpServer server;
    private static int port;

    private static final AtomicReference<String> LAST_METHOD = new AtomicReference<>();
    private static final AtomicReference<String> LAST_AUTH = new AtomicReference<>();
    private static final AtomicReference<String> LAST_BODY = new AtomicReference<>();
    private static final AtomicReference<String> LAST_PATH = new AtomicReference<>();

    @BeforeAll
    static void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", AdminClientTest::handle);
        server.start();
        port = server.getAddress().getPort();
    }

    @AfterAll
    static void stop() {
        server.stop(0);
    }

    private static void handle(HttpExchange exchange) throws IOException {
        LAST_METHOD.set(exchange.getRequestMethod());
        LAST_AUTH.set(exchange.getRequestHeaders().getFirst("Authorization"));
        LAST_BODY.set(new String(Java8.readAllBytes(exchange.getRequestBody()), StandardCharsets.UTF_8));
        LAST_PATH.set(exchange.getRequestURI().getPath());

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (path.equals("/v2/tenants/acme/domains") && method.equals("GET")) {
            exchange.getResponseHeaders().add("X-Next-Page-Token", "next-token");
            respond(exchange, 200, "[" + domainJSON() + "]");
        } else if (path.equals("/v2/tenants/acme/domains") && method.equals("POST")) {
            respond(exchange, 201, domainJSON());
        } else if (path.equals("/v2/tenants/acme/listeners") && method.equals("POST")) {
            respond(exchange, 201, listenerJSON());
        } else if (path.equals("/v2/tenants/acme/mta-rate-limit-override") && method.equals("GET")) {
            respond(exchange, 200, mtaRateLimitOverrideJSON());
        } else if (path.equals("/v2/tenants/acme/mta-rate-limit-override") && method.equals("PUT")) {
            respond(exchange, 200, mtaRateLimitOverrideJSON());
        } else if (path.equals("/v2/tenants/acme/domains/d1") && method.equals("GET")) {
            respond(exchange, 200, domainJSON());
        } else if (path.equals("/v2/tenants/acme/domains/d1") && method.equals("PUT")) {
            respond(exchange, 200, domainJSON());
        } else if (path.equals("/v2/tenants/acme/auto-reply-templates/ref/welcome") && method.equals("GET")) {
            respond(exchange, 200, autoReplyTemplateJSON());
        } else if (path.equals("/v2/tenants/acme/storage-integrations/ref/archive") && method.equals("GET")) {
            respond(exchange, 200, storageIntegrationJSON());
        } else if (path.equals("/v2/tenants/acme/webhook-endpoints/ref/hooks") && method.equals("GET")) {
            respond(exchange, 200, webhookEndpointJSON());
        } else if (path.equals("/v2/tenants/acme/smtp-forward-destinations/ref/forwarding")
                && method.equals("GET")) {
            respond(exchange, 200, smtpForwardDestinationJSON());
        } else if (path.equals("/v2/tenants/acme/smtp-relays/ref/primary") && method.equals("GET")) {
            respond(exchange, 200, smtpRelayJSON());
        } else if (path.equals("/v2/tenants/acme/smtp-relays/r1") && method.equals("DELETE")) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
        } else {
            exchange.getResponseHeaders().add("X-Trace-ID", "0123456789abcdef0123456789abcdef");
            respond(exchange, 422, "{\"type\":\"urn:mxraven:problem:validation_failed\","
                    + "\"title\":\"Validation failed\",\"status\":422,"
                    + "\"code\":\"validation_failed\",\"detail\":\"slug is invalid\","
                    + "\"trace_id\":\"0123456789abcdef0123456789abcdef\","
                    + "\"errors\":[{\"pointer\":\"/slug\",\"code\":\"invalid_slug\","
                    + "\"detail\":\"bad slug\"}]}");
        }
    }

    private static String domainJSON() {
        return "{\"id\":\"d1\",\"tenant_id\":\"t1\",\"domain_name\":\"example.com\","
                + "\"verification_token\":\"tok\",\"sending_enabled\":true,"
                + "\"dkim_active_selector\":\"mxr1\",\"spf_verified\":false,"
                + "\"dkim_verified\":false,\"dmarc_verified\":false,"
                + "\"dmarc_report_address\":null,\"dns_last_checked_at\":null,"
                + "\"status\":\"pending\"}";
    }

    private static String autoReplyTemplateJSON() {
        return "{\"id\":\"art1\",\"tenant_id\":\"t1\",\"template_ref\":\"welcome\","
                + "\"display_name\":\"Welcome\",\"from_address\":\"a@b.com\"}";
    }

    private static String storageIntegrationJSON() {
        return "{\"id\":\"s1\",\"tenant_id\":\"t1\",\"storage_ref\":\"archive\","
                + "\"display_name\":\"Archive\",\"is_active\":true,\"credentials_present\":true}";
    }

    private static String webhookEndpointJSON() {
        return "{\"id\":\"w1\",\"tenant_id\":\"t1\",\"webhook_ref\":\"hooks\","
                + "\"display_name\":\"Hooks\",\"target_url\":\"https://example.com/hook\","
                + "\"signing_kid\":\"k1\",\"has_signing_secret\":true,\"is_active\":true,"
                + "\"created_at\":\"2026-01-01T00:00:00Z\",\"updated_at\":\"2026-01-01T00:00:00Z\"}";
    }

    private static String smtpForwardDestinationJSON() {
        return "{\"id\":\"f1\",\"tenant_id\":\"t1\",\"destination_ref\":\"forwarding\","
                + "\"display_name\":\"Forward\",\"email_address\":\"f@example.com\","
                + "\"verification_status\":\"pending\",\"delivery_status\":\"queued\","
                + "\"created_at\":\"2026-01-01T00:00:00Z\",\"updated_at\":\"2026-01-01T00:00:00Z\"}";
    }

    private static String smtpRelayJSON() {
        return "{\"id\":\"r1\",\"tenant_id\":\"t1\",\"relay_ref\":\"primary\","
                + "\"display_name\":\"Primary\",\"is_active\":true,\"credentials_present\":true}";
    }

    private static String mtaRateLimitOverrideJSON() {
        return "{\"scope\":\"tenant\",\"tenant_id\":\"t1\","
                + "\"inherited\":{\"message_rate_per_minute\":60,\"recipient_rate_per_minute\":600,"
                + "\"task_rate_per_minute\":600,\"burst\":50,\"max_concurrency\":10},"
                + "\"effective\":{\"message_rate_per_minute\":60,\"recipient_rate_per_minute\":600,"
                + "\"task_rate_per_minute\":600,\"burst\":50,\"max_concurrency\":10},"
                + "\"effective_source\":\"guardrail\"}";
    }

    private static String listenerJSON() {
        return "{\"id\":\"l1\",\"tenant_id\":\"t1\",\"display_name\":\"Listener\","
                + "\"listener_type\":\"mta\",\"stream_type\":\"marketing\","
                + "\"default_terminal_action_type\":\"DROP\","
                + "\"default_terminal_action_payload\":{},\"rspamd_scanning_enabled\":true}";
    }

    private static void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private AdminClient client() {
        return new AdminClient("http://127.0.0.1:" + port, "secret-token");
    }

    @Test
    void sendsBearerTokenAndParsesPagination() throws IOException {
        Paged<Domain> page = client().workspace("acme").domains().list();
        assertEquals("Bearer secret-token", LAST_AUTH.get());
        assertEquals("GET", LAST_METHOD.get());
        Page<Domain> first = page.firstPage();
        assertEquals(1, first.items().size());
        assertEquals("example.com", first.items().get(0).domainName());
        assertEquals("next-token", first.nextPageToken());
        assertTrue(first.hasNext());
        assertFalse(first.hasPrevious());
    }

    @Test
    void serializesRequestsAsSnakeCaseAndOmitsUnsetOptionals() throws IOException {
        client().workspace("acme").domains().create("example.com");
        assertEquals("POST", LAST_METHOD.get());
        assertTrue(LAST_BODY.get().contains("\"domain_name\":\"example.com\""));
        assertFalse(LAST_BODY.get().contains("domainName"));
        assertFalse(LAST_BODY.get().contains("dmarc_report_address"));
    }

    @Test
    void sendsExplicitNullForRequiredNullableFields() throws IOException {
        client().workspace("acme").domains().get("d1").replace(null);
        assertEquals("PUT", LAST_METHOD.get());
        assertEquals("{\"dmarc_report_address\":null}", LAST_BODY.get());
    }

    @Test
    void configuratorOverloadBuildsAndSendsTheRequest() throws IOException {
        client().workspace("acme").listeners().create(listener -> listener
                .displayName("Inbound")
                .listenerType(ListenerType.MTA)
                .streamType(StreamType.MARKETING)
                .defaultTerminalAction(TerminalActionType.DROP));
        assertEquals("POST", LAST_METHOD.get());
        assertTrue(LAST_BODY.get().contains("\"display_name\":\"Inbound\""));
        assertTrue(LAST_BODY.get().contains("\"listener_type\":\"mta\""));
        assertTrue(LAST_BODY.get().contains("\"default_terminal_action_type\":\"DROP\""));
    }

    @Test
    void rejectsLooseMtaRateLimitOverrideBeforeSending() {
        MTARateLimitPolicy looser = new MTARateLimitPolicy(600, 6000, 600, 100, 50);
        assertThrows(IllegalArgumentException.class,
                () -> client().workspace("acme").mtaRateLimits().put(looser));
    }

    @Test
    void acceptsStricterMtaRateLimitOverride() throws IOException {
        client().workspace("acme").mtaRateLimits().put(new MTARateLimitPolicy(30, 300, 300, 25, 5));
        assertEquals("PUT", LAST_METHOD.get());
    }

    @Test
    void getByRefResolvesAndRebindsToIdPath() throws IOException {
        assertEquals("art1", client().workspace("acme").autoReplyTemplates().getByRef("welcome").id());
        assertEquals("/v2/tenants/acme/auto-reply-templates/ref/welcome", LAST_PATH.get());

        assertEquals("s1", client().workspace("acme").storageIntegrations().getByRef("archive").id());
        assertEquals("/v2/tenants/acme/storage-integrations/ref/archive", LAST_PATH.get());

        assertEquals("w1", client().workspace("acme").webhookEndpoints().getByRef("hooks").id());
        assertEquals("/v2/tenants/acme/webhook-endpoints/ref/hooks", LAST_PATH.get());

        assertEquals("f1", client().workspace("acme").smtpForwardDestinations().getByRef("forwarding").id());
        assertEquals("/v2/tenants/acme/smtp-forward-destinations/ref/forwarding", LAST_PATH.get());

        SmtpRelay relay = client().workspace("acme").smtpRelays().getByRef("primary");
        assertEquals("r1", relay.id());
        assertEquals("/v2/tenants/acme/smtp-relays/ref/primary", LAST_PATH.get());

        relay.delete();
        assertEquals("/v2/tenants/acme/smtp-relays/r1", LAST_PATH.get());
    }

    @Test
    void parsesRfc9457ProblemIntoApiException() {
        ApiException error = assertThrows(ApiException.class,
                () -> client().workspace("missing").domains().list());
        assertEquals(422, error.status());
        assertEquals("validation_failed", error.code());
        assertEquals("slug is invalid", error.detail());
        assertEquals("0123456789abcdef0123456789abcdef", error.traceId());
        assertEquals(1, error.errors().size());
        assertEquals("/slug", error.errors().get(0).pointer());
        assertEquals("invalid_slug", error.errors().get(0).code());
    }

    @Test
    void defaultsToTheProductionBaseUrl() {
        AdminClient client = new AdminClient("token");
        assertEquals("https://api.mxraven.email", client.baseUrl());
        assertEquals(AdminClient.DEFAULT_BASE_URL, client.baseUrl());
        assertEquals("v2", client.apiVersion());
    }
}
