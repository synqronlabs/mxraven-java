package com.mxraven.mail.webhook;

/**
 * Optional idempotency store used by {@link WebhookHandler} to deduplicate
 * at-least-once deliveries.
 *
 * <p>{@code DELIVER_WEBHOOK} is delivered at least once: the same task can
 * arrive more than once. A handler configured with a store calls
 * {@link #seen(String)} before dispatching and {@link #remember(String)} after
 * the listener returns normally. A task is therefore never marked processed if
 * handling failed, so the sender's retry is still dispatched.
 *
 * <p>Implementations typically back this with a cache or database and bound
 * retained entries by age. Both methods may be called concurrently.
 */
public interface WebhookStore {
    /**
     * Whether the task has already been processed.
     *
     * @param taskId the delivery task ID
     * @return {@code true} when the task was seen before
     */
    boolean seen(String taskId);

    /**
     * Records a task as processed, after the listener returned normally.
     *
     * @param taskId the delivery task ID
     */
    void remember(String taskId);
}
