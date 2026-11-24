package com.mxraven.mail.notify;

/**
 * A non-terminal effect an execution-time check skipped.
 *
 * @param intent the skipped effect
 * @param reason why it was skipped, for example a header covered by a passing signature
 */
public record RoutingSkip(RoutingIntent intent, String reason) {
}
