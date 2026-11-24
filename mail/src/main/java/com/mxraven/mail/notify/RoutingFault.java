package com.mxraven.mail.notify;

/**
 * Why a routing script faulted and the listener-type default applied.
 *
 * <p>{@link #message()} may contain tenant-authored text. Do not treat it as
 * trusted markup or interpolate it into a log line unchecked.
 *
 * @param reason stable fault reason, for example {@code quarantined}
 * @param message human-readable failure description
 * @param line script line that faulted, when known
 */
public record RoutingFault(String reason, String message, Integer line) {
}
