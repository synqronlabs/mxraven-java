package com.mxraven.admin;

import com.mxraven.admin.client.ConnectedAppsClient;
import com.mxraven.admin.model.ConnectedAppAuthorizationRequest;
import com.mxraven.admin.model.ConnectedAppDeviceAuthorization;
import com.mxraven.admin.model.ConnectedAppInstallation;
import com.mxraven.admin.model.ConnectedAppInstallationStatus;
import com.mxraven.admin.model.CreateConnectedAppInstallationRequest;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConnectedAppsClientTest {
    private HttpServer server;
    private ConnectedAppsClient apps;

    private final AtomicReference<String> method = new AtomicReference<>();
    private final AtomicReference<String> path = new AtomicReference<>();
    private final AtomicReference<String> query = new AtomicReference<>();
    private final AtomicReference<String> body = new AtomicReference<>();

    @BeforeEach
    void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", this::handle);
        server.start();
        AdminClient client = new AdminClient("http://127.0.0.1:" + server.getAddress().getPort(), "token");
        apps = client.workspace("acme").connectedApps();
    }

    @AfterEach
    void stop() {
        server.stop(0);
    }

    private void handle(HttpExchange exchange) throws IOException {
        method.set(exchange.getRequestMethod());
        path.set(exchange.getRequestURI().getPath());
        query.set(exchange.getRequestURI().getRawQuery());
        body.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
        String requestPath = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        if (requestPath.equals("/v2/tenants/acme/connected-apps") && requestMethod.equals("GET")) {
            respond(exchange, 200, "[" + installationJSON() + "]");
        } else if (requestPath.equals("/v2/tenants/acme/connected-apps") && requestMethod.equals("POST")) {
            respond(exchange, 201, installationJSON());
        } else if (requestPath.equals("/v2/tenants/acme/connected-apps/i1") && requestMethod.equals("GET")) {
            respond(exchange, 200, installationJSON());
        } else if (requestPath.equals("/v2/tenants/acme/connected-apps/i1") && requestMethod.equals("DELETE")) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
        } else if (requestPath.endsWith("/device-authorization/ABCD-1234") && requestMethod.equals("GET")) {
            respond(exchange, 200, deviceAuthorizationJSON());
        } else if (requestPath.endsWith("/authorization-request") && requestMethod.equals("GET")) {
            respond(exchange, 200, authorizationRequestJSON());
        } else {
            respond(exchange, 404, "{}");
        }
    }

    @Test
    void listsCreatesGetsAndRevokesInstallations() throws Exception {
        List<ConnectedAppInstallation> installations = apps.list().toList();
        assertEquals(1, installations.size());
        assertEquals(ConnectedAppInstallationStatus.ACTIVE, installations.get(0).status());
        assertEquals("connected_apps.read", installations.get(0).grantedScopes().get(0));

        ConnectedAppInstallation created = apps.create(
                CreateConnectedAppInstallationRequest.of("a1", List.of("connected_apps.read")));
        assertEquals("POST", method.get());
        assertTrue(body.get().contains("\"app_id\":\"a1\""));
        assertEquals("a1", created.appId());

        assertEquals("i1", apps.get("i1").id());
        apps.revoke("i1");
        assertEquals("DELETE", method.get());
    }

    @Test
    void previewsDeviceAndBrowserAuthorizations() throws Exception {
        ConnectedAppDeviceAuthorization device = apps.getDeviceAuthorization("ABCD-1234");
        assertEquals("/v2/tenants/acme/connected-apps/device-authorization/ABCD-1234", path.get());
        assertEquals("ABCD-1234", device.userCode());
        assertEquals("connected_apps.write", device.requestedScopes().get(0).scope());

        ConnectedAppAuthorizationRequest browser = apps.getAuthorizationRequest(
                "client-1", "https://app.example/callback");
        assertEquals("/v2/tenants/acme/connected-apps/authorization-request", path.get());
        assertTrue(query.get().contains("client_id=client-1"));
        assertTrue(query.get().contains("redirect_uri=https%3A%2F%2Fapp.example%2Fcallback"));
        assertEquals("https://app.example/callback", browser.redirectUri());
    }

    private static void respond(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private static String installationJSON() {
        return """
                {"id":"i1","tenant_id":"t1","app_id":"a1","client_id":"client-1",
                 "display_name":"Risk bot","homepage_url":"https://app.example",
                 "granted_scopes":["connected_apps.read"],"status":"active",
                 "granted_by":{"issuer":"https://idp","subject":"s1","email":"owner@example.com"},
                 "granted_at":"2026-01-01T00:00:00Z"}""";
    }

    private static String deviceAuthorizationJSON() {
        return """
                {"user_code":"ABCD-1234","app_id":"a1","client_id":"client-1",
                 "display_name":"Risk bot","requested_scopes":[
                   {"scope":"connected_apps.write","description":"Manage apps"}]}""";
    }

    private static String authorizationRequestJSON() {
        return """
                {"app_id":"a1","client_id":"client-1",
                 "redirect_uri":"https://app.example/callback","display_name":"Risk bot",
                 "requested_scopes":[{"scope":"connected_apps.read","description":"Read apps"}]}""";
    }
}
