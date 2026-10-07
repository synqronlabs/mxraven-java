package com.mxraven.mail.examples;

import com.mxraven.mail.SmtpClient;
import com.mxraven.mail.SmtpConfig;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Configures OAuth 2.0 {@code XOAUTH2} authentication and a SOCKS proxy.
 *
 * <p>With {@code oauthToken(...)} the client uses {@code XOAUTH2}; the username
 * from {@code credentials(...)} is the authorization identity and the password is
 * ignored. With {@code proxy(...)} the connection is routed through an HTTP or
 * SOCKS proxy.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.OAuthAndProxyExample}
 */
public final class OAuthAndProxyExample {

    private OAuthAndProxyExample() {
    }

    public static void main(String[] args) throws Exception {
        // OAuth 2.0 XOAUTH2, as used by Gmail and Microsoft 365.
        SmtpConfig oauth = SmtpConfig.builder()
                .host("smtp.gmail.com")
                .port(587)
                .startTls()
                .credentials("user@example.com", "")   // identity; password ignored
                .oauthToken("ya29.access-token")       // a fresh OAuth 2.0 bearer token
                .build();

        // Route through a SOCKS5 proxy (use Proxy.Type.HTTP for a CONNECT proxy).
        SmtpConfig proxied = SmtpConfig.builder()
                .host("smtp.mxraven.email")
                .port(587)
                .startTls()
                .credentials("smtp-user", "smtp-secret")
                .proxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1080)))
                .build();

        try (SmtpClient client = SmtpClient.connect(oauth)) {
            System.out.println("authenticated=" + client.isAuthenticated());
        }
    }
}
