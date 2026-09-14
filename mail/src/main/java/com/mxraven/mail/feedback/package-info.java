/**
 * Submits recipient feedback to the mxRaven feedback service.
 *
 * <p>Tenants can teach the mxRaven spam filter by submitting messages that were
 * misclassified. A message is matched to its stored evidence by SHA-256, so the
 * exact raw RFC 822 bytes mxRaven processed must be submitted. Use
 * {@link com.mxraven.mail.feedback.FeedbackClient#learnSpam(byte[])} and
 * {@link com.mxraven.mail.feedback.FeedbackClient#learnHam(byte[])}.
 *
 * <p>The package also exposes the RFC 8058 one-click unsubscribe endpoint that
 * recipient mail clients call. Suppression-list management and analytics live in
 * the {@code admin} module.
 */
package com.mxraven.mail.feedback;
