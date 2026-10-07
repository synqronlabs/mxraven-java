package com.mxraven.mail.examples;

import com.mxraven.mail.SmtpClient;
import com.mxraven.mail.SmtpConfig;

/**
 * Connects and prints the ESMTP capabilities the server advertises, plus the
 * ones that change the optimized send path.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.SessionCapabilitiesExample}
 */
public final class SessionCapabilitiesExample {

    private SessionCapabilitiesExample() {
    }

    public static void main(String[] args) throws Exception {
        SmtpConfig config = SmtpConfig.builder()
                .host("smtp.mxraven.email")
                .port(587)
                .startTls()
                .credentials("smtp-user", "smtp-secret")
                .build();

        try (SmtpClient client = SmtpClient.connect(config)) {
            System.out.println("greeting = " + client.greeting());
            System.out.println("esmtp=" + client.isEsmtp()
                    + " tls=" + client.isTls() + " auth=" + client.isAuthenticated());

            System.out.println("extensions:");
            for (java.util.Map.Entry<String, String> entry : client.extensions().entrySet()) {
                System.out.println("  " + entry.getKey()
                        + (entry.getValue().isEmpty() ? "" : " " + entry.getValue()));
            }

            System.out.println("CHUNKING/BDAT = " + client.hasExtension("CHUNKING")
                    + ", PIPELINING = " + client.hasExtension("PIPELINING")
                    + ", SMTPUTF8 = " + client.hasExtension("SMTPUTF8"));
            System.out.println("SIZE limit = " + client.extensionParam("SIZE")
                    + ", AUTH = " + client.extensionParam("AUTH"));
        }
    }
}
