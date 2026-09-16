package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A labelled histogram bin with a stable key and a non-negative count.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsCountBin {
    private final String key;
    private final String label;
    private final long count;

    /** stable bin key */
    public String key() {
        return key;
    }

    /** human-readable bin label */
    public String label() {
        return label;
    }

    /** number of samples in the bin */
    public long count() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsCountBin that = (MailAnalyticsCountBin) o;
        return Objects.equals(this.key, that.key)
                && Objects.equals(this.label, that.label)
                && this.count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.label, this.count);
    }

    @Override
    public String toString() {
        return "MailAnalyticsCountBin[" + "key=" + this.key + ", " + "label=" + this.label + ", " + "count=" + this.count + "]";
    }

    /**
     * Creates a new MailAnalyticsCountBin.
     *
     * @param key stable bin key
     * @param label human-readable bin label
     * @param count number of samples in the bin
     */
    @JsonCreator
    public MailAnalyticsCountBin(String key, String label, long count) {
        this.key = key;
        this.label = label;
        this.count = count;
    }
}
