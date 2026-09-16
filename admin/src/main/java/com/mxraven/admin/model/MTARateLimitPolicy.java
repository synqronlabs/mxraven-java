package com.mxraven.admin.model;

/**
 * Writable MTA rate-limit policy used for global, tenant, and listener
 * overrides. Every field must be positive, and an override must not be looser
 * than the limits it inherits from.
 *
 * @param messageRatePerMinute   message rate limit per minute
 * @param recipientRatePerMinute recipient rate limit per minute
 * @param taskRatePerMinute      task rate limit per minute
 * @param burst                  burst allowance
 * @param maxConcurrency         maximum number of concurrent deliveries
 */
public record MTARateLimitPolicy(
        double messageRatePerMinute,
        double recipientRatePerMinute,
        double taskRatePerMinute,
        int burst,
        int maxConcurrency) {

    /**
     * Validates and creates a rate-limit policy.
     *
     * @param messageRatePerMinute message rate limit per minute
     * @param recipientRatePerMinute recipient rate limit per minute
     * @param taskRatePerMinute task rate limit per minute
     * @param burst burst allowance
     * @param maxConcurrency maximum number of concurrent deliveries
     * @throws IllegalArgumentException if a rate is not positive, or burst or concurrency is
     *         below 1
     */
    public MTARateLimitPolicy {
        requirePositive(messageRatePerMinute, "message_rate_per_minute");
        requirePositive(recipientRatePerMinute, "recipient_rate_per_minute");
        requirePositive(taskRatePerMinute, "task_rate_per_minute");
        if (burst < 1) {
            throw new IllegalArgumentException("burst must be at least 1");
        }
        if (maxConcurrency < 1) {
            throw new IllegalArgumentException("max_concurrency must be at least 1");
        }
    }

    /**
     * Throws when this override is looser than {@code inherited} on any limit.
     * Overrides may only tighten the inherited limits.
     *
     * @param inherited limits to compare against; {@code null} is ignored
     * @throws IllegalArgumentException when a limit is looser than the inherited limit
     */
    public void validateStricterThan(MTARateLimitPolicy inherited) {
        if (inherited == null) {
            return;
        }
        if (messageRatePerMinute > inherited.messageRatePerMinute) {
            throw new IllegalArgumentException(
                    "message_rate_per_minute must be less than or equal to the inherited limit");
        }
        if (recipientRatePerMinute > inherited.recipientRatePerMinute) {
            throw new IllegalArgumentException(
                    "recipient_rate_per_minute must be less than or equal to the inherited limit");
        }
        if (taskRatePerMinute > inherited.taskRatePerMinute) {
            throw new IllegalArgumentException(
                    "task_rate_per_minute must be less than or equal to the inherited limit");
        }
        if (burst > inherited.burst) {
            throw new IllegalArgumentException("burst must be less than or equal to the inherited limit");
        }
        if (maxConcurrency > inherited.maxConcurrency) {
            throw new IllegalArgumentException(
                    "max_concurrency must be less than or equal to the inherited limit");
        }
    }

    private static void requirePositive(double value, String field) {
        if (!(value > 0) || Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(field + " must be greater than 0");
        }
    }
}
