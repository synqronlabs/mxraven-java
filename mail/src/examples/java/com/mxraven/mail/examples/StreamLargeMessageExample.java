package com.mxraven.mail.examples;

import com.mxraven.mail.SendOptions;
import com.mxraven.mail.SendResult;
import com.mxraven.mail.SmtpClient;
import com.mxraven.mail.SmtpConfig;
import com.mxraven.mail.model.Envelope;
import com.mxraven.mail.model.Path;
import com.mxraven.mail.model.Recipient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;

/**
 * Streams a large prebuilt message from disk without loading it all into memory.
 *
 * <p>Set {@link Envelope#size()} to the exact byte length: a known size lets the
 * client use a single {@code BDAT … LAST} when the server advertises
 * {@code CHUNKING}. With no size it streams through dot-stuffed {@code DATA}
 * instead. Either way the body is read in 64 KiB chunks.
 *
 * <p>Unlike {@code send(Mail)}, a raw message is not altered, so it must already
 * be a valid RFC 5322 message (including {@code Date}).
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.StreamLargeMessageExample}
 */
public final class StreamLargeMessageExample {

    private StreamLargeMessageExample() {
    }

    public static void main(String[] args) throws Exception {
        SmtpConfig config = SmtpConfig.builder()
                .host("smtp.mxraven.email")
                .port(587)
                .startTls()
                .credentials("smtp-user", "smtp-secret")
                .build();

        File file = new File("big-message.eml");

        Envelope envelope = Envelope.builder()
                .from(Path.of("sender@example.com"))
                .to(List.of(Recipient.of("recipient@example.com")))
                .size(file.length())
                .build();

        try (InputStream message = new FileInputStream(file);
             SmtpClient client = SmtpClient.connect(config)) {
            SendResult result = client.sendRaw(envelope, message, SendOptions.builder()
                    .timeout(Duration.ofMinutes(5))
                    .build());
            System.out.println("streamed " + file.length() + " bytes -> success="
                    + result.success() + " ref=" + result.messageRef());
        }
    }
}
