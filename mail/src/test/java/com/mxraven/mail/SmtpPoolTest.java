package com.mxraven.mail;

import com.mxraven.mail.model.Mail;
import com.mxraven.mail.model.MailBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmtpPoolTest {

    private static SmtpConfig config(FakeSmtpServer server) {
        return SmtpConfig.builder()
                .host("127.0.0.1")
                .port(server.port())
                .localName("test.local")
                .noTls()
                .build();
    }

    private static Mail simpleMail() {
        return MailBuilder.create()
                .from("sender@example.com")
                .to("to@example.com")
                .subject("Hello")
                .textBody("Body line")
                .build();
    }

    @Test
    void reusesAnIdleConnectionForSequentialSends() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer();
             SmtpPool pool = new SmtpPool(config(server), 1)) {
            assertEquals(1, pool.maxSize());
            assertTrue(pool.send(simpleMail()).success());
            assertTrue(pool.send(simpleMail()).success());

            long mailCommands = server.commands.stream()
                    .filter(command -> command.startsWith("MAIL FROM:"))
                    .count();
            long greetings = server.commands.stream()
                    .filter(command -> command.startsWith("EHLO"))
                    .count();
            assertEquals(2, mailCommands);
            assertEquals(1, greetings, "the second send should reuse the pooled connection");
        }
    }

    @Test
    void rejectsInvalidConfigurationAndUseAfterClose() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer()) {
            assertThrows(IllegalArgumentException.class, () -> new SmtpPool(null));
            assertThrows(IllegalArgumentException.class, () -> new SmtpPool(config(server), 0));

            SmtpPool pool = new SmtpPool(config(server), 1);
            pool.close();
            assertThrows(SmtpException.class, () -> pool.send(simpleMail()));
        }
    }
}
