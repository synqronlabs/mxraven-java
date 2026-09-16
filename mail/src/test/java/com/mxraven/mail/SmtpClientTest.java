package com.mxraven.mail;

import com.mxraven.mail.model.BodyType;
import com.mxraven.mail.model.DSNEnvelopeParams;
import com.mxraven.mail.model.DSNRecipientParams;
import com.mxraven.mail.model.Envelope;
import com.mxraven.mail.model.Mail;
import com.mxraven.mail.model.MailBuilder;
import com.mxraven.mail.model.Path;
import com.mxraven.mail.model.Recipient;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmtpClientTest {

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
    void connectsAndParsesEhloCapabilities() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer();
             SmtpClient client = SmtpClient.connect(config(server))) {
            assertTrue(client.isEsmtp());
            assertFalse(client.isTls());
            assertTrue(client.greeting().contains("fake"));
            assertTrue(client.hasExtension("SIZE"));
            assertTrue(client.hasExtension("8BITMIME"));
            assertFalse(client.hasExtension("FAKE"), "the EHLO greeting is not an extension");
            assertEquals("10240000", client.extensionParam("SIZE"));
            assertEquals(List.of("EHLO test.local"), server.commands);
        }
    }

    @Test
    void sendUnwrapsEnvelopeFromAllRecipientLists() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer()) {
            Mail mail = MailBuilder.create()
                    .from("sender@example.com")
                    .to("to1@example.com", "to2@example.com")
                    .cc("cc@example.com")
                    .bcc("bcc@example.com")
                    .subject("Hello")
                    .textBody("Body line")
                    .build();

            try (SmtpClient client = SmtpClient.connect(config(server))) {
                SendResult result = client.send(mail);

                assertTrue(result.success());
                assertEquals(4, result.recipients().size());
                assertTrue(result.recipients().stream().allMatch(RecipientResult::accepted));
            }

            assertTrue(server.mailFrom.startsWith("<sender@example.com>"), server.mailFrom);
            assertEquals(List.of(
                    "<to1@example.com>",
                    "<to2@example.com>",
                    "<cc@example.com>",
                    "<bcc@example.com>"), server.recipients);

            assertNotNull(server.dataPayload);
            assertTrue(server.dataPayload.contains("From: sender@example.com"));
            assertTrue(server.dataPayload.contains("To: to1@example.com, to2@example.com"));
            assertTrue(server.dataPayload.contains("Cc: cc@example.com"));
            assertFalse(server.dataPayload.contains("Bcc"));
            assertTrue(server.dataPayload.contains("Subject: Hello"));
            assertTrue(server.dataPayload.contains("Content-Type: text/plain; charset=utf-8"));
            assertTrue(server.dataPayload.contains("Body line"));
        }
    }

    @Test
    void nullSenderUsesTheEmptyReversePath() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer()) {
            Mail mail = MailBuilder.create()
                    .nullSender()
                    .to("to@example.com")
                    .textBody("bounce")
                    .build();

            try (SmtpClient client = SmtpClient.connect(config(server))) {
                assertTrue(client.send(mail).success());
            }

            assertTrue(server.mailFrom.startsWith("<>"), server.mailFrom);
        }
    }

    @Test
    void dataIsCrlfNormalizedAndDotStuffed() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer()) {
            Mail mail = MailBuilder.create()
                    .from("a@example.com")
                    .to("b@example.com")
                    .textBody("line1\n.line2\nline3")
                    .build();

            try (SmtpClient client = SmtpClient.connect(config(server))) {
                client.send(mail);
            }

            assertNotNull(server.dataPayload);
            assertTrue(server.dataPayload.contains("line1\r\n..line2\r\nline3"));
            assertEquals(-1, server.dataPayload.replace("\r\n", "").indexOf('\n'),
                    "body must not contain bare LF line endings");
        }
    }

    @Test
    void rejectedRecipientIsReportedWithoutFailingTheTransaction() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer(false, false, false, 550)) {
            try (SmtpClient client = SmtpClient.connect(config(server))) {
                SendResult result = client.send(simpleMail());

                assertEquals(1, result.recipients().size());
                assertFalse(result.recipients().get(0).accepted());
                assertEquals("rejected", result.recipients().get(0).status());
            }
        }
    }

    @Test
    void usesBdatWhenChunkingIsAdvertised() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer(false, false, false, 250, List.of("CHUNKING"))) {
            try (SmtpClient client = SmtpClient.connect(config(server))) {
                assertTrue(client.send(simpleMail()).success());
            }

            assertTrue(server.commands.stream().anyMatch(c -> c.startsWith("BDAT")), server.commands.toString());
            assertTrue(server.commands.stream().noneMatch(c -> c.equalsIgnoreCase("DATA")));
            assertNotNull(server.dataPayload);
            assertTrue(server.dataPayload.contains("Body line"));
        }
    }

    @Test
    void batchesMailAndRecipientsWhenPipeliningIsAdvertised() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer(false, false, false, 250, List.of("PIPELINING"))) {
            Mail mail = MailBuilder.create()
                    .from("sender@example.com")
                    .to("one@example.com", "two@example.com", "three@example.com")
                    .subject("Hello")
                    .textBody("Body")
                    .build();

            try (SmtpClient client = SmtpClient.connect(config(server))) {
                SendResult result = client.send(mail);
                assertTrue(result.success());
                assertEquals(3, result.recipients().size());
            }

            assertEquals(3, server.recipients.size());
        }
    }

    @Test
    void assumesRfc822WhenOrcptHasNoAddressType() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer(false, false, false, 250, List.of("DSN"))) {
            Envelope envelope = Envelope.builder()
                    .from(Path.of("sender@example.com"))
                    .to(List.of(new Recipient(Path.of("to@example.com"),
                            new DSNRecipientParams(List.of("SUCCESS"), "orig@example.com"))))
                    .build();

            try (SmtpClient client = SmtpClient.connect(config(server))) {
                client.sendRaw(envelope, "Subject: raw\r\n\r\nbody\r\n".getBytes(StandardCharsets.UTF_8));
            }

            assertTrue(server.recipients.get(0).contains("ORCPT=rfc822;orig@example.com"),
                    server.recipients.get(0));
        }
    }

    @Test
    void rejectsNeverCombinedWithOtherNotifyFlags() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer(false, false, false, 250, List.of("DSN"))) {
            Envelope envelope = Envelope.builder()
                    .from(Path.of("sender@example.com"))
                    .to(List.of(new Recipient(Path.of("to@example.com"),
                            new DSNRecipientParams(List.of("NEVER", "FAILURE"), null))))
                    .build();

            try (SmtpClient client = SmtpClient.connect(config(server))) {
                assertThrows(SmtpException.class, () ->
                        client.sendRaw(envelope, "x".getBytes(StandardCharsets.UTF_8)));
            }
        }
    }

    @Test
    void rejectsCommandInjectionInLowLevelAddresses() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer();
             SmtpClient client = SmtpClient.connect(config(server))) {
            assertThrows(SmtpException.class, () -> client.rcpt("a@example.com\r\nDATA"));
            assertThrows(SmtpException.class, () -> client.mail("a@example.com\nRSET"));
        }
    }

    @Test
    void cancelAbortsTheClient() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer();
             SmtpClient client = SmtpClient.connect(config(server))) {
            client.cancel();
            assertThrows(SmtpException.class, () -> client.send(simpleMail()));
        }
    }

    @Test
    void streamsRawMessageOverData() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer()) {
            Envelope envelope = Envelope.builder()
                    .from(Path.of("sender@example.com"))
                    .to(List.of(Recipient.of("to@example.com")))
                    .build();
            byte[] raw = "Subject: raw\r\n\r\nline1\r\n.line2\r\n".getBytes(StandardCharsets.UTF_8);

            try (SmtpClient client = SmtpClient.connect(config(server))) {
                assertTrue(client.sendRaw(envelope, new ByteArrayInputStream(raw)).success());
            }

            assertNotNull(server.dataPayload);
            assertTrue(server.dataPayload.contains("line1\r\n..line2"), server.dataPayload);
        }
    }

    @Test
    void streamsRawMessageOverBdat() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer(false, false, false, 250, List.of("CHUNKING"))) {
            byte[] raw = "Subject: raw\r\n\r\nstreamed body\r\n".getBytes(StandardCharsets.UTF_8);
            Envelope envelope = Envelope.builder()
                    .from(Path.of("sender@example.com"))
                    .to(List.of(Recipient.of("to@example.com")))
                    // A known size lets the stream use a single BDAT ... LAST.
                    .size(raw.length)
                    .build();

            try (SmtpClient client = SmtpClient.connect(config(server))) {
                assertTrue(client.sendRaw(envelope, new ByteArrayInputStream(raw)).success());
            }

            assertTrue(server.commands.stream().anyMatch(c -> c.startsWith("BDAT")), server.commands.toString());
            assertNotNull(server.dataPayload);
            assertTrue(server.dataPayload.contains("streamed body"), server.dataPayload);
        }
    }

    @Test
    void streamsRawMessageOverDataWhenChunkingIsAdvertisedButSizeIsUnknown() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer(false, false, false, 250, List.of("CHUNKING"))) {
            byte[] raw = "Subject: raw\r\n\r\nunknown size body\r\n".getBytes(StandardCharsets.UTF_8);
            Envelope envelope = Envelope.builder()
                    .from(Path.of("sender@example.com"))
                    .to(List.of(Recipient.of("to@example.com")))
                    .build();

            try (SmtpClient client = SmtpClient.connect(config(server))) {
                assertTrue(client.sendRaw(envelope, new ByteArrayInputStream(raw)).success());
            }

            assertTrue(server.commands.stream().noneMatch(c -> c.startsWith("BDAT")), server.commands.toString());
            assertNotNull(server.dataPayload);
            assertTrue(server.dataPayload.contains("unknown size body"), server.dataPayload);
        }
    }

    @Test
    void doesNotSendRecipientsWhenMailFromIsRejected() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer()) {
            try (SmtpClient client = SmtpClient.connect(config(server))) {
                server.mailFromCode = 550;
                SendResult result = client.send(simpleMail());
                assertFalse(result.success());
                assertTrue(result.recipients().isEmpty(), "no recipient was attempted");
            }
            assertTrue(server.recipients.isEmpty(), "no RCPT TO should follow a rejected MAIL FROM");
        }
    }

    @Test
    void perSendTimeoutAbortsAStalledServer() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer()) {
            SmtpClient client = SmtpClient.connect(config(server));
            try {
                server.stallOnMail = true;
                long start = System.nanoTime();
                assertThrows(SocketTimeoutException.class, () -> client.send(simpleMail(),
                        SendOptions.builder().timeout(Duration.ofMillis(300)).build()));
                long elapsedMillis = (System.nanoTime() - start) / 1_000_000;
                assertTrue(elapsedMillis < 5_000, "should abort quickly but took " + elapsedMillis + "ms");
            } finally {
                server.releaseStall();
                client.close();
            }
        }
    }

    @Test
    void perSendCancellationAbortsAStalledServer() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer()) {
            SmtpClient client = SmtpClient.connect(config(server));
            try {
                server.stallOnMail = true;
                Cancellation cancellation = Cancellation.create();
                Thread canceller = new Thread(() -> {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    cancellation.cancel();
                });
                canceller.setDaemon(true);
                canceller.start();
                assertThrows(SmtpException.class, () -> client.send(simpleMail(),
                        SendOptions.builder().cancellation(cancellation).build()));
            } finally {
                server.releaseStall();
                client.close();
            }
        }
    }

    @Test
    void authenticatesWithXoauth2() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer(false, false, false, 250, List.of("AUTH XOAUTH2"))) {
            SmtpConfig config = SmtpConfig.builder()
                    .host("127.0.0.1")
                    .port(server.port())
                    .noTls()
                    .credentials("user@example.com", "ignored")
                    .oauthToken("ya29.token")
                    .build();

            try (SmtpClient client = SmtpClient.connect(config)) {
                assertTrue(client.isAuthenticated());
            }

            assertEquals("user@example.com", server.authUser);
        }
    }

    @Test
    void startTlsRequiredButNotAdvertisedFails() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer()) {
            SmtpConfig config = SmtpConfig.builder()
                    .host("127.0.0.1")
                    .port(server.port())
                    .startTls()
                    .build();

            SmtpException error = assertThrows(SmtpException.class, () -> SmtpClient.connect(config));
            assertTrue(error.getMessage().contains("STARTTLS"));
        }
    }

    @Test
    void authenticatesWithPlainMechanism() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer(false, true, false, 250)) {
            SmtpConfig config = SmtpConfig.builder()
                    .host("127.0.0.1")
                    .port(server.port())
                    .noTls()
                    .credentials("user", "secret")
                    .build();

            try (SmtpClient client = SmtpClient.connect(config)) {
                assertTrue(client.isAuthenticated());
            }

            assertEquals("user", server.authUser);
            assertEquals("secret", server.authPassword);
        }
    }

    @Test
    void authenticatesWithLoginMechanism() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer(false, false, true, 250)) {
            SmtpConfig config = SmtpConfig.builder()
                    .host("127.0.0.1")
                    .port(server.port())
                    .noTls()
                    .credentials("user", "secret")
                    .build();

            try (SmtpClient client = SmtpClient.connect(config)) {
                assertTrue(client.isAuthenticated());
            }

            assertEquals("user", server.authUser);
            assertEquals("secret", server.authPassword);
        }
    }

    @Test
    void quitReturnsTheServerFarewell() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer()) {
            try (SmtpClient client = SmtpClient.connect(config(server))) {
                SmtpResponse response = client.quit();
                assertEquals(221, response.code());
                assertTrue(response.isSuccess());
            }
        }
    }

    @Test
    void includesConfiguredSmtpExtensionParameters() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer(false, false, false, 250, List.of("DSN"))) {
            Envelope envelope = Envelope.builder()
                    .from(Path.of("sender@example.com"))
                    .to(List.of(new Recipient(Path.of("to@example.com"),
                            new DSNRecipientParams(List.of("success", "failure"), "rfc822;orig@example.com"))))
                    .size(1234)
                    .bodyType(BodyType.EIGHT_BIT_MIME)
                    .smtpUtf8(true)
                    .dsnParams(new DSNEnvelopeParams("full"))
                    .envId("abc123")
                    .build();

            try (SmtpClient client = SmtpClient.connect(config(server))) {
                assertTrue(client.sendRaw(envelope,
                        "Subject: raw\r\n\r\nbody\r\n".getBytes(StandardCharsets.UTF_8)).success());
            }

            assertTrue(server.mailFrom.startsWith("<sender@example.com> "), server.mailFrom);
            assertTrue(server.mailFrom.contains("SIZE=1234"), server.mailFrom);
            assertTrue(server.mailFrom.contains("BODY=8BITMIME"), server.mailFrom);
            assertTrue(server.mailFrom.contains("SMTPUTF8"), server.mailFrom);
            assertTrue(server.mailFrom.contains("RET=FULL"), server.mailFrom);
            assertTrue(server.mailFrom.contains("ENVID=abc123"), server.mailFrom);

            assertEquals(1, server.recipients.size());
            assertTrue(server.recipients.get(0).contains("NOTIFY=SUCCESS,FAILURE"), server.recipients.get(0));
            assertTrue(server.recipients.get(0).contains("ORCPT=rfc822;orig@example.com"), server.recipients.get(0));
        }
    }

    @Test
    void omitsExtensionParametersTheServerDoesNotAdvertise() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer()) {
            Envelope envelope = Envelope.builder()
                    .from(Path.of("sender@example.com"))
                    .to(List.of(new Recipient(Path.of("to@example.com"),
                            new DSNRecipientParams(List.of("SUCCESS"), "rfc822;x@example.com"))))
                    .dsnParams(new DSNEnvelopeParams("FULL"))
                    .build();

            try (SmtpClient client = SmtpClient.connect(config(server))) {
                client.sendRaw(envelope, "Subject: raw\r\n\r\nbody\r\n".getBytes(StandardCharsets.UTF_8));
            }

            assertEquals("<sender@example.com>", server.mailFrom);
            assertEquals(List.of("<to@example.com>"), server.recipients);
        }
    }

    @Test
    void requireTlsWithoutAnActiveTlsSessionFails() throws Exception {
        try (FakeSmtpServer server = new FakeSmtpServer()) {
            Envelope envelope = Envelope.builder()
                    .from(Path.of("sender@example.com"))
                    .to(List.of(Recipient.of("to@example.com")))
                    .requireTls(true)
                    .build();

            try (SmtpClient client = SmtpClient.connect(config(server))) {
                assertThrows(SmtpException.class, () ->
                        client.sendRaw(envelope, "x".getBytes(StandardCharsets.UTF_8)));
            }
        }
    }
}
