package com.mxraven.admin.examples;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Workspace;
import com.mxraven.admin.model.Listener;
import com.mxraven.admin.model.ListenerType;
import com.mxraven.admin.model.StreamType;
import com.mxraven.admin.model.TerminalActionType;

/**
 * Creates and manages listeners, and reaches the child clients that hang off a
 * listener.
 *
 * <p>Run with:
 * {@code ./gradlew :admin:runExample -Pexample=com.mxraven.admin.examples.ManageListenersExample}
 */
public final class ManageListenersExample {

    private ManageListenersExample() {
    }

    public static void main(String[] args) throws Exception {
        try (AdminClient admin = new AdminClient("your-access-token")) {
            Workspace ws = admin.workspace("my-workspace");

            Listener listener = ws.listeners().create(l -> l
                    .displayName("Outbound transactional")
                    .listenerType(ListenerType.SUBMISSION)
                    .streamType(StreamType.TRANSACTIONAL)
                    .defaultTerminalAction(TerminalActionType.DELIVER));

            for (Listener l : ws.listeners().list()) {
                System.out.println(l.id() + " " + l.displayName() + " " + l.listenerType());
            }

            listener.rename("Outbound primary");
            listener.updateRspamdScanning(true);

            // Child clients scoped to this listener:
            listener.routingRules().list();
            listener.apiKeys().list();

            listener.delete();
        }
    }
}
