package com.mxraven.mail.examples;

import com.mxraven.mail.Cancellation;
import com.mxraven.mail.notify.AcceptedMessage;
import com.mxraven.mail.notify.AttemptEvent;
import com.mxraven.mail.notify.FaultEvent;
import com.mxraven.mail.notify.HandlerResult;
import com.mxraven.mail.notify.NotificationCallbacks;
import com.mxraven.mail.notify.NotificationClient;
import com.mxraven.mail.notify.NotificationClientOptions;

/**
 * Consumes one listener's client notification events over NATS JetStream.
 *
 * <p>Set the listener API key prefix in {@code MXRAVEN_USERNAME}, the secret in
 * {@code MXRAVEN_SECRET}, and the listener ID in {@code MXRAVEN_LISTENER}.
 * Enable notifications for the listener with the administrative API first. The
 * client connects to the production notification tier; override
 * {@code NotificationClientOptions.Builder#url(String)} for staging. Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.NotificationsExample}
 */
public final class NotificationsExample {

    private NotificationsExample() {
    }

    public static void main(String[] args) throws Exception {
        String username = System.getenv("MXRAVEN_USERNAME");
        String secret = System.getenv("MXRAVEN_SECRET");
        String listenerId = System.getenv("MXRAVEN_LISTENER");

        try (NotificationClient client = NotificationClient.connect(
                NotificationClientOptions.builder()
                        .url("wss://notifications.mxraven.com")
                        .username(username)
                        .secret(secret)
                        .listener(listenerId)
                        .build())) {
            Cancellation cancellation = Cancellation.create();
            Runtime.getRuntime().addShutdownHook(new Thread(cancellation::cancel));

            client.consume(new NotificationCallbacks() {
                @Override
                public HandlerResult onAccepted(AcceptedMessage message) {
                    System.out.println("accepted " + message.taskId()
                            + " " + message.message().subject()
                            + " terminal=" + message.routingDecision().terminalAction());
                    if (message.rawEmail() != null) {
                        // MTA listeners can download the raw message; a
                        // submission listener's accepted event carries no raw
                        // message access.
                        System.out.println("raw message: " + message.rawEmail().url());
                    }
                    return HandlerResult.CONTINUE;
                }

                @Override
                public HandlerResult onAttempt(AttemptEvent event) {
                    String reason = event.reason() == null ? "" : " (" + event.reason() + ")";
                    System.out.println(event.channel() + " " + event.taskId() + " "
                            + event.status() + reason);
                    return HandlerResult.CONTINUE;
                }

                @Override
                public HandlerResult onFault(FaultEvent event) {
                    System.out.println("script fault revision=" + event.scriptRevision()
                            + " reason=" + event.fault().reason()
                            + " suppressed=" + event.suppressedFaults());
                    return HandlerResult.CONTINUE;
                }
            }, cancellation);
        }
    }
}
