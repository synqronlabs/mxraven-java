package com.mxraven.mail.webhook;

import com.mxraven.mail.internal.Java8;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A small, dependency-free HTTP server that serves a {@link WebhookHandler} in a
 * standalone process or worker.
 *
 * <pre>
 * WebhookHandler handler = WebhookHandler.builder()
 *         .verifier(WebhookVerifier.builder().secret(signingSecret).build())
 *         .listener(event -&gt; app.handle(event))
 *         .build();
 *
 * try (WebhookServer server = WebhookServer.builder()
 *         .port(8080)
 *         .path("/mxraven/webhook")
 *         .handler(handler)
 *         .build()) {
 *     server.start();
 *     // ...
 * }
 * </pre>
 *
 * <p>It only owns the HTTP plumbing: it reads <pre> (method, uri, headers,
 * body)</pre>, delegates to {@link WebhookHandler#handle}, and writes the resulting
 * status. It is built on the JDK's {@link HttpServer} and adds no dependency.
 * Applications that already own an HTTP stack (Spring, Jakarta, Lambda, …)
 * should skip this class and adapt their framework directly to
 * {@link WebhookHandler}.
 *
 * <p>No threads are started until {@link #start()} and they stop on
 * {@link #close()}. When running behind a TLS-terminating proxy set
 * {@code X-Forwarded-Proto} so the signed request target is reconstructed
 * correctly.
 */
public final class WebhookServer implements AutoCloseable {
    private final InetSocketAddress address;
    private final String path;
    private final WebhookHandler handler;
    private final Executor executor;
    private final boolean ownsExecutor;
    private final boolean banner;

    private HttpServer server;

    private WebhookServer(Builder builder) {
        this.address = new InetSocketAddress(builder.host, builder.port);
        this.path = builder.path;
        this.handler = builder.handler;
        this.executor = builder.executor;
        this.ownsExecutor = builder.ownsExecutor;
        this.banner = builder.banner;
    }

    /**
     * Creates a server builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Binds the socket and starts serving.
     *
     * @throws IOException           when the address cannot be bound
     * @throws IllegalStateException when the server is already started
     */
    public synchronized void start() throws IOException {
        if (server != null) {
            throw new IllegalStateException("server is already started");
        }
        HttpServer created = HttpServer.create(address, 0);
        created.setExecutor(executor);
        created.createContext(path, this::serve);
        created.start();
        this.server = created;
        if (banner) {
            printBanner(created);
        }
    }

    private void printBanner(HttpServer server) {
        String host = server.getAddress().getHostString();
        String displayHost = host.contains(":") ? "[" + host + "]" : host;
        System.err.println(" * Running on http://" + displayHost + ":" + server.getAddress().getPort() + path
                + " (Press CTRL+C to quit)");
        System.err.println("WARNING: This is a development server. Do not use it in a production deployment.");
        System.err.println("         Terminate TLS at a reverse proxy and use a hardened server in production.");
    }

    /**
     * Returns the bound port, or the configured port before {@link #start()}.
     *
     * @return the port
     */
    public synchronized int port() {
        return server == null ? address.getPort() : server.getAddress().getPort();
    }

    /**
     * Returns the path this server serves.
     *
     * @return the request path
     */
    public String path() {
        return path;
    }

    @Override
    public synchronized void close() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
        if (ownsExecutor && executor instanceof ExecutorService) {
            ((ExecutorService) executor).shutdownNow();
        }
    }

    private void serve(HttpExchange exchange) throws IOException {
        int status;
        try {
            byte[] body = Java8.readAllBytes(exchange.getRequestBody());
            Map<String, String> headers = headers(exchange);
            URI uri = requestUri(exchange, headers);
            status = handler.handle(exchange.getRequestMethod(), uri, headers, body).status();
        } catch (RuntimeException e) {
            // The listener failed: tell the sender to retry.
            status = 500;
        }
        exchange.sendResponseHeaders(status, -1);
        exchange.close();
    }

    private static Map<String, String> headers(HttpExchange exchange) {
        Map<String, String> headers = new LinkedHashMap<>();
        exchange.getRequestHeaders().forEach((name, values) -> {
            if (name != null && !values.isEmpty()) {
                headers.put(name, values.get(0));
            }
        });
        return headers;
    }

    private static URI requestUri(HttpExchange exchange, Map<String, String> headers) {
        String scheme = first(headers, "X-Forwarded-Proto");
        if (scheme == null || Java8.isBlank(scheme)) {
            scheme = "http";
        } else if (scheme.contains(",")) {
            scheme = scheme.substring(0, scheme.indexOf(',')).trim();
        }
        String host = first(headers, "Host");
        if (host == null || Java8.isBlank(host)) {
            InetSocketAddress local = exchange.getLocalAddress();
            host = local.getHostString() + ":" + local.getPort();
        }
        return URI.create(scheme + "://" + host + exchange.getRequestURI());
    }

    private static String first(Map<String, String> headers, String name) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /** Builds a {@link WebhookServer}. */
    public static final class Builder {
        private String host = "127.0.0.1";
        private int port = 8080;
        private String path = "/mxraven/webhook";
        private WebhookHandler handler;
        private Executor executor;
        private boolean ownsExecutor;
        private boolean banner = true;

        private Builder() {
        }

        /**
         * Sets the bind host. Defaults to {@code 127.0.0.1} (loopback only).
         *
         * @param host the bind host
         * @return this builder
         * @throws IllegalArgumentException when {@code host} is {@code null} or blank
         */
        public Builder host(String host) {
            if (host == null || Java8.isBlank(host)) {
                throw new IllegalArgumentException("host must not be empty");
            }
            this.host = host;
            return this;
        }

        /**
         * Sets the bind port. Use {@code 0} for an ephemeral port. Defaults to 8080.
         *
         * @param port the bind port
         * @return this builder
         * @throws IllegalArgumentException when {@code port} is outside {@code 0} to {@code 65535}
         */
        public Builder port(int port) {
            if (port < 0 || port > 65535) {
                throw new IllegalArgumentException("port must be between 0 and 65535");
            }
            this.port = port;
            return this;
        }

        /**
         * Sets the path to serve. Defaults to {@code /mxraven/webhook}.
         *
         * @param path the request path, which must start with {@code '/'}
         * @return this builder
         * @throws IllegalArgumentException when {@code path} is not absolute
         */
        public Builder path(String path) {
            if (path == null || !path.startsWith("/")) {
                throw new IllegalArgumentException("path must start with '/'");
            }
            this.path = path;
            return this;
        }

        /**
         * Sets the handler to serve. Build one with {@link WebhookHandler#builder()}.
         *
         * @param handler the webhook handler
         * @return this builder
         */
        public Builder handler(WebhookHandler handler) {
            this.handler = Objects.requireNonNull(handler, "handler");
            return this;
        }

        /**
         * Sets the executor used to dispatch requests. Defaults to a small daemon pool.
         *
         * @param executor the executor
         * @return this builder
         */
        public Builder executor(Executor executor) {
            this.executor = Objects.requireNonNull(executor, "executor");
            this.ownsExecutor = false;
            return this;
        }

        /**
         * Whether {@link WebhookServer#start()} prints the "development server" banner
         * and warning to {@code System.err}. Enabled by default; disable for production.
         *
         * @param banner {@code true} to print the banner and warning
         * @return this builder
         */
        public Builder banner(boolean banner) {
            this.banner = banner;
            return this;
        }

        /**
         * Binds, validates, and builds the server. A handler is required.
         *
         * @return the configured server
         * @throws IllegalStateException when the handler has not been set
         */
        public WebhookServer build() {
            if (handler == null) {
                throw new IllegalStateException("a handler is required (call handler(...))");
            }
            if (executor == null) {
                int threads = Math.max(2, Runtime.getRuntime().availableProcessors());
                this.executor = Executors.newFixedThreadPool(threads, daemonFactory("mxraven-webhook"));
                this.ownsExecutor = true;
            }
            return new WebhookServer(this);
        }

        private static ThreadFactory daemonFactory(String prefix) {
            AtomicInteger counter = new AtomicInteger();
            return runnable -> {
                Thread thread = new Thread(runnable, prefix + "-" + counter.incrementAndGet());
                thread.setDaemon(true);
                return thread;
            };
        }
    }
}
