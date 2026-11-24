package com.mxraven.admin;

import com.mxraven.admin.client.RoutingScriptsClient;
import com.mxraven.admin.model.ExportedListenerScript;
import com.mxraven.admin.model.Listener;
import com.mxraven.admin.model.ListenerData;
import com.mxraven.admin.model.ListenerScript;
import com.mxraven.admin.model.ListenerType;
import com.mxraven.admin.model.ScriptIntentKind;
import com.mxraven.admin.model.ScriptRevision;
import com.mxraven.admin.model.ScriptRevisionStatus;
import com.mxraven.admin.model.ScriptSimulationResult;
import com.mxraven.admin.model.SimulateScriptRevisionRequest;
import com.mxraven.admin.model.StreamType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoutingScriptsClientTest {
    private HttpServer server;
    private RoutingScriptsClient scripts;

    private final AtomicReference<String> method = new AtomicReference<>();
    private final AtomicReference<String> path = new AtomicReference<>();
    private final AtomicReference<String> body = new AtomicReference<>();
    private final AtomicInteger listCalls = new AtomicInteger();

    @BeforeEach
    void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", this::handle);
        server.start();
        AdminClient client = new AdminClient("http://127.0.0.1:" + server.getAddress().getPort(), "token");
        Listener listener = new Listener(client, "/tenants/acme/listeners/l1",
                new ListenerData("l1", "t1", "Outbound", ListenerType.MTA,
                        StreamType.TRANSACTIONAL, true, "rev-id", true));
        scripts = listener.routingScripts();
    }

    @AfterEach
    void stop() {
        server.stop(0);
    }

    private void handle(HttpExchange exchange) throws IOException {
        method.set(exchange.getRequestMethod());
        path.set(exchange.getRequestURI().getPath());
        body.set(new String(com.mxraven.admin.internal.Java8.readAllBytes(exchange.getRequestBody()),
                StandardCharsets.UTF_8));
        String requestPath = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        if (requestPath.endsWith("/script") && requestMethod.equals("GET")) {
            respond(exchange, 200, listenerScriptJSON());
        } else if (requestPath.endsWith("/script") && requestMethod.equals("DELETE")) {
            respond(exchange, 200, listenerScriptJSON());
        } else if (requestPath.endsWith("/script/revisions") && requestMethod.equals("POST")) {
            respond(exchange, 201, revisionJSON());
        } else if (requestPath.endsWith("/script/revisions/3") && requestMethod.equals("GET")) {
            respond(exchange, 200, revisionJSON());
        } else if (requestPath.endsWith("/script/revisions") && requestMethod.equals("GET")) {
            if (listCalls.incrementAndGet() == 1) {
                exchange.getResponseHeaders().add("X-Next-Page-Token", "page-2");
            }
            respond(exchange, 200, "[" + summaryJSON() + "]");
        } else if (requestPath.endsWith("/simulate") && requestMethod.equals("POST")) {
            respond(exchange, 200, simulationJSON());
        } else if (requestPath.endsWith("/publish") && requestMethod.equals("POST")) {
            respond(exchange, 200, listenerScriptJSON());
        } else if (requestPath.endsWith("/script/rollback") && requestMethod.equals("POST")) {
            respond(exchange, 200, listenerScriptJSON());
        } else if (requestPath.endsWith("/script/export") && requestMethod.equals("POST")) {
            respond(exchange, 200, exportedJSON());
        } else if (requestPath.endsWith("/script/body-access") && requestMethod.equals("PUT")) {
            respond(exchange, 200, listenerScriptJSON());
        } else {
            respond(exchange, 404, "{}");
        }
    }

    @Test
    void readsThePublishedScriptAndItsRevision() throws Exception {
        ListenerScript script = scripts.get();
        assertEquals("/v2/tenants/acme/listeners/l1/script", path.get());
        assertTrue(script.bodyAccess());
        assertEquals(ListenerType.MTA, script.listenerType());
        assertNotNull(script.published());
        assertEquals(ScriptRevisionStatus.PUBLISHED, script.published().status());
        assertEquals(1, script.published().dependencies().endpoint().size());

        ScriptRevision revision = scripts.getRevision(3);
        assertEquals(3, revision.revision());
        assertTrue(revision.publishable());
        assertEquals("error", revision.diagnostics().get(0).severity().wire());
    }

    @Test
    void savesDraftsAndPagesTheHistory() throws Exception {
        scripts.createRevision("function handle(mail, mx) end");
        assertEquals("POST", method.get());
        assertTrue(body.get().contains("\"source\":\"function handle(mail, mx) end\""));

        List<com.mxraven.admin.model.ScriptRevisionSummary> summaries = scripts.listRevisions().toList();
        assertEquals(2, summaries.size());
        assertEquals(2, listCalls.get());
        assertEquals(ScriptRevisionStatus.DRAFT, summaries.get(0).status());
    }

    @Test
    void deletesThePublishedScript() throws Exception {
        scripts.delete();
        assertEquals("DELETE", method.get());
        assertEquals("/v2/tenants/acme/listeners/l1/script", path.get());
    }

    @Test
    void simulatesPublishesRollsBackExportsAndSetsBodyAccess() throws Exception {
        ScriptSimulationResult result = scripts.simulate(3, SimulateScriptRevisionRequest.empty());
        assertEquals(ScriptIntentKind.REJECT, result.terminal().kind());
        assertEquals(1, result.intents().size());

        scripts.publish(3);
        assertEquals("/v2/tenants/acme/listeners/l1/script/revisions/3/publish", path.get());

        scripts.rollback(2);
        assertEquals("{\"revision\":2}", body.get());

        ExportedListenerScript exported = scripts.export();
        assertEquals(3, exported.revision());

        scripts.setBodyAccess(false);
        assertEquals("{\"enabled\":false}", body.get());
    }

    private static void respond(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private static String revisionJSON() {
        return "{\"id\":\"rev-id\",\"listener_id\":\"l1\",\"revision\":3,\"status\":\"published\","
                + "\"publishable\":true,\"source\":\"function handle(mail, mx) end\","
                + "\"source_sha256\":\"abc\",\"schema_version\":\"mxraven/mail/v1\","
                + "\"dependencies\":{\"forward\":[],\"relay\":[],\"storage\":[],\"endpoint\":[\"risk\"],"
                + "\"template\":[],\"pool\":[],\"sets\":[]},"
                + "\"diagnostics\":[{\"severity\":\"error\",\"line\":2,\"column\":4,"
                + "\"message\":\"boom\",\"hint\":\"fix\"}],"
                + "\"created_by\":\"alice\",\"created_at\":\"2026-01-01T00:00:00Z\"}";
    }

    private static String summaryJSON() {
        return "{\"id\":\"rev-id\",\"listener_id\":\"l1\",\"revision\":3,\"status\":\"draft\","
                + "\"publishable\":false,\"source_sha256\":\"abc\","
                + "\"schema_version\":\"mxraven/mail/v1\",\"created_by\":\"alice\","
                + "\"created_at\":\"2026-01-01T00:00:00Z\"}";
    }

    private static String listenerScriptJSON() {
        return "{\"listener_id\":\"l1\",\"listener_type\":\"mta\",\"body_access\":true,"
                + "\"published\":" + revisionJSON() + "}";
    }

    private static String simulationJSON() {
        return "{\"revision\":3,\"terminal\":{\"kind\":\"reject\",\"code\":550,"
                + "\"enhanced\":\"5.7.1\",\"text\":\"no\"},\"default_applied\":false,"
                + "\"intents\":[{\"kind\":\"header.set\",\"header\":\"X-Test\",\"value\":\"1\"}],"
                + "\"skipped\":[],\"log\":[\"checked\"],\"http_calls\":[],\"trace\":[],"
                + "\"duration_us\":12}";
    }

    private static String exportedJSON() {
        return "{\"listener_id\":\"l1\",\"revision\":3,\"source\":\"function handle(mail, mx) end\","
                + "\"dependencies\":{\"forward\":[],\"relay\":[],\"storage\":[],\"endpoint\":[\"risk\"],"
                + "\"template\":[],\"pool\":[],\"sets\":[]}}";
    }
}
