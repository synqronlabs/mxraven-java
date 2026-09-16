package com.mxraven.mail.webhook;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * Spam and malware scan results for an inbound message.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Verdicts {
    private final String action;
    private final double score;
    private final double requiredScore;
    private final boolean isSpam;
    private final boolean hasMalware;
    private final List<String> malwareNames;
    private final boolean isSkipped;
    private final String error;

    /** the scanner action description */
    public String action() {
        return action;
    }

    /** the spam score that was assigned */
    public double score() {
        return score;
    }

    /** the threshold the message was compared against */
    public double requiredScore() {
        return requiredScore;
    }

    /** whether the message was classified as spam */
    public boolean isSpam() {
        return isSpam;
    }

    /** whether malware was detected */
    public boolean hasMalware() {
        return hasMalware;
    }

    /** the detected malware signatures */
    public List<String> malwareNames() {
        return malwareNames;
    }

    /** whether scanning was skipped */
    public boolean isSkipped() {
        return isSkipped;
    }

    /** the scanner error, when scanning failed */
    public String error() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Verdicts that = (Verdicts) o;
        return Objects.equals(this.action, that.action)
                && Double.compare(this.score, that.score) == 0
                && Double.compare(this.requiredScore, that.requiredScore) == 0
                && this.isSpam == that.isSpam
                && this.hasMalware == that.hasMalware
                && Objects.equals(this.malwareNames, that.malwareNames)
                && this.isSkipped == that.isSkipped
                && Objects.equals(this.error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.action, this.score, this.requiredScore, this.isSpam, this.hasMalware, this.malwareNames, this.isSkipped, this.error);
    }

    @Override
    public String toString() {
        return "Verdicts[" + "action=" + this.action + ", " + "score=" + this.score + ", " + "requiredScore=" + this.requiredScore + ", " + "isSpam=" + this.isSpam + ", " + "hasMalware=" + this.hasMalware + ", " + "malwareNames=" + this.malwareNames + ", " + "isSkipped=" + this.isSkipped + ", " + "error=" + this.error + "]";
    }

    /**
     * Creates verdicts, copying the malware name list.
     */
    @JsonCreator
    public Verdicts(String action, double score, double requiredScore, boolean isSpam, boolean hasMalware, List<String> malwareNames, boolean isSkipped, String error) {

        malwareNames = malwareNames == null ? Java8.list() : Java8.copyList(malwareNames);
    
        this.action = action;
        this.score = score;
        this.requiredScore = requiredScore;
        this.isSpam = isSpam;
        this.hasMalware = hasMalware;
        this.malwareNames = malwareNames;
        this.isSkipped = isSkipped;
        this.error = error;
    }
}
