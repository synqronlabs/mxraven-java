package com.mxraven.mail.examples;

import com.mxraven.mail.feedback.FeedbackClient;
import com.mxraven.mail.feedback.LearningResult;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Reports spam/ham feedback for a message mxRaven processed, and handles a
 * one-click unsubscribe token.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.FeedbackExample}
 */
public final class FeedbackExample {

    private FeedbackExample() {
    }

    public static void main(String[] args) throws Exception {
        FeedbackClient feedback = FeedbackClient.builder()
                .baseUrl("https://feedback.mxraven.example")
                .credentials("feedback-key", "feedback-secret")
                .build();

        byte[] rawMime = Files.readAllBytes(Paths.get("message.eml"));

        LearningResult learned = feedback.learnSpam(rawMime);
        System.out.println("status=" + learned.status()
                + " disposition=" + learned.disposition()
                + " tenant=" + learned.tenantId());

        feedback.learnHam(rawMime);
        feedback.unsubscribe("unsubscribe-token");
    }
}
