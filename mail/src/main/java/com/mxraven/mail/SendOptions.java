package com.mxraven.mail;

import java.time.Duration;
import java.util.Optional;

/**
 * Per-send options: an optional overall deadline and an optional cancellation
 * signal.
 *
 * <p>A deadline bounds the whole transaction (connect is already complete, so it
 * covers the SMTP exchange plus the message body). It is enforced by shortening
 * socket read timeouts and by a background monitor that closes the connection if
 * a write stalls or the deadline passes, so a large or stuck transfer cannot run
 * forever.
 *
 * <pre>{@code
 * Cancellation cancellation = Cancellation.create();
 * SendResult result = client.send(mail, SendOptions.builder()
 *         .timeout(Duration.ofSeconds(30))
 *         .cancellation(cancellation)
 *         .build());
 * }</pre>
 *
 * <p>Instances are immutable.
 */
public final class SendOptions {
    private static final SendOptions DEFAULTS = new SendOptions(null, null);

    private final Duration timeout;
    private final Cancellation cancellation;

    private SendOptions(Duration timeout, Cancellation cancellation) {
        this.timeout = timeout;
        this.cancellation = cancellation;
    }

    /**
     * Options with no deadline and no cancellation.
     *
     * @return the default options
     */
    public static SendOptions defaults() {
        return DEFAULTS;
    }

    /**
     * The overall deadline, when set.
     *
     * @return the timeout, or empty for no deadline
     */
    public Optional<Duration> timeout() {
        return Optional.ofNullable(timeout);
    }

    /**
     * The cancellation signal, when set.
     *
     * @return the cancellation, or empty
     */
    public Optional<Cancellation> cancellation() {
        return Optional.ofNullable(cancellation);
    }

    /**
     * Creates a builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link SendOptions}. */
    public static final class Builder {
        private Duration timeout;
        private Cancellation cancellation;

        private Builder() {
        }

        /**
         * Sets the overall transaction deadline.
         *
         * @param timeout the timeout; must be positive
         * @return this builder
         * @throws IllegalArgumentException when {@code timeout} is {@code null} or not positive
         */
        public Builder timeout(Duration timeout) {
            if (timeout == null || timeout.isZero() || timeout.isNegative()) {
                throw new IllegalArgumentException("timeout must be positive");
            }
            this.timeout = timeout;
            return this;
        }

        /**
         * Sets the cancellation signal.
         *
         * @param cancellation the cancellation
         * @return this builder
         * @throws IllegalArgumentException when {@code cancellation} is {@code null}
         */
        public Builder cancellation(Cancellation cancellation) {
            if (cancellation == null) {
                throw new IllegalArgumentException("cancellation must not be null");
            }
            this.cancellation = cancellation;
            return this;
        }

        /**
         * Builds the options.
         *
         * @return the options
         */
        public SendOptions build() {
            if (timeout == null && cancellation == null) {
                return DEFAULTS;
            }
            return new SendOptions(timeout, cancellation);
        }
    }
}
