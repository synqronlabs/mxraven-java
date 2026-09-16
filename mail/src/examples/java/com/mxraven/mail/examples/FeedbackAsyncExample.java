package com.mxraven.mail.examples;

import com.mxraven.mail.feedback.FeedbackClient;
import com.mxraven.mail.feedback.LearningResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Submits feedback with a bounded request read and a non-blocking call.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.FeedbackAsyncExample}
 */
public final class FeedbackAsyncExample {

    private FeedbackAsyncExample() {
    }

    public static void main(String[] args) throws Exception {
        FeedbackClient feedback = FeedbackClient.builder()
                .baseUrl("https://feedback.mxraven.example")
                .credentials("feedback-key", "feedback-secret")
                .requestTimeout(Duration.ofSeconds(30))
                .maxRequestBytes(64L * 1024 * 1024)   // cap on a streamed raw message
                .build();

        byte[] rawMime = Files.readAllBytes(Path.of("message.eml"));

        // Blocking call.
        LearningResult learned = feedback.learnSpam(rawMime);
        System.out.println("learned " + learned.disposition() + " matched " + learned.matchedHashKind());

        // Non-blocking call; cancelling the future aborts just this request.
        CompletableFuture<LearningResult> future = feedback.learnHamAsync(rawMime);
        try {
            LearningResult ham = future.get(30, TimeUnit.SECONDS);
            System.out.println("ham " + ham.status());
        } catch (Exception e) {
            future.cancel(true);
            System.out.println("learning cancelled: " + e.getMessage());
        }
    }
}
