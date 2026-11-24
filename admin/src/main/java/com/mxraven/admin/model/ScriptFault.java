package com.mxraven.admin.model;

/**
 * Why a routing script faulted and the listener-type default applied.
 *
 * @param reason stable fault reason
 * @param message human-readable failure description
 * @param line source line that faulted, when known
 */
public record ScriptFault(
        ScriptFaultReason reason,
        String message,
        Integer line) {
}
