package com.mxraven.admin.model;

/**
 * A listener's routing script.
 *
 * <p>A listener without a published revision uses the listener-type default:
 * submission listeners deliver through the shared pool, and MTA listeners drop
 * after acceptance.
 *
 * @param listenerId owning listener identifier
 * @param listenerType submission or MTA
 * @param bodyAccess whether {@code mail.body:text()} may read the spooled body (MTA only)
 * @param published the currently published revision, when one exists
 */
public record ListenerScript(
        String listenerId,
        ListenerType listenerType,
        boolean bodyAccess,
        ScriptRevision published) {
}
