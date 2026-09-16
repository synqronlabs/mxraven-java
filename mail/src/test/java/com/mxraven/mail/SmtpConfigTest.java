package com.mxraven.mail;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SmtpConfigTest {

    @Test
    void appliesDocumentedDefaults() {
        SmtpConfig config = SmtpConfig.builder().host("smtp.example.com").build();

        assertEquals("smtp.example.com", config.host());
        assertEquals(587, config.port());
        assertEquals(SecurityMode.STARTTLS, config.security());
        assertEquals("localhost", config.localName());
        assertEquals(Duration.ofSeconds(30), config.connectTimeout());
        assertEquals(Duration.ofMinutes(2), config.readTimeout());
        assertEquals(Duration.ofMinutes(2), config.writeTimeout());
        assertNull(config.username());
        assertNull(config.password());
        assertNull(config.sslContext());
    }

    @Test
    void securityHelpersToggleTheMode() {
        assertEquals(SecurityMode.STARTTLS, SmtpConfig.builder().host("h").startTls().build().security());
        assertEquals(SecurityMode.IMPLICIT_TLS, SmtpConfig.builder().host("h").implicitTls().build().security());
        assertEquals(SecurityMode.NONE, SmtpConfig.builder().host("h").noTls().build().security());
    }

    @Test
    void storesCredentialsAndOverrides() {
        SmtpConfig config = SmtpConfig.builder()
                .host("h")
                .port(2525)
                .localName("relay.local")
                .credentials("user", "secret")
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(6))
                .writeTimeout(Duration.ofSeconds(7))
                .build();

        assertEquals("user", config.username());
        assertEquals("secret", config.password());
        assertEquals(2525, config.port());
        assertEquals("relay.local", config.localName());
        assertEquals(Duration.ofSeconds(5), config.connectTimeout());
        assertEquals(Duration.ofSeconds(6), config.readTimeout());
        assertEquals(Duration.ofSeconds(7), config.writeTimeout());
    }

    @Test
    void hostIsRequired() {
        assertThrows(IllegalStateException.class, () -> SmtpConfig.builder().host("  ").build());
    }

    @Test
    void localNameRejectsInjection() {
        assertThrows(IllegalStateException.class,
                () -> SmtpConfig.builder().host("h").localName("bad\r\nMAIL FROM:<x>").build());
        assertThrows(IllegalStateException.class,
                () -> SmtpConfig.builder().host("h").localName("  ").build());
    }

    @Test
    void defaultsToTheProductionHost() {
        assertEquals("smtp.mxraven.email", SmtpConfig.DEFAULT_HOST);
        assertEquals(SmtpConfig.DEFAULT_HOST, SmtpConfig.builder().build().host());
    }
}
