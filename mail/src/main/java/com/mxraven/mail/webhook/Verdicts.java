package com.mxraven.mail.webhook;

import java.util.List;

/**
 * Spam and malware scan results for an inbound message.
 *
 * @param action        the scanner action description
 * @param score         the spam score that was assigned
 * @param requiredScore the threshold the message was compared against
 * @param isSpam        whether the message was classified as spam
 * @param hasMalware    whether malware was detected
 * @param malwareNames  the detected malware signatures
 * @param isSkipped     whether scanning was skipped
 * @param error         the scanner error, when scanning failed
 */
public record Verdicts(
        String action,
        double score,
        double requiredScore,
        boolean isSpam,
        boolean hasMalware,
        List<String> malwareNames,
        boolean isSkipped,
        String error) {

    /**
     * Creates verdicts, copying the malware name list.
     */
    public Verdicts {
        malwareNames = malwareNames == null ? List.of() : List.copyOf(malwareNames);
    }
}
