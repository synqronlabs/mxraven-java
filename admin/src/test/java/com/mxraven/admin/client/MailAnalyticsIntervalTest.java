package com.mxraven.admin.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MailAnalyticsIntervalTest {
    @Test
    void acceptsAlignedHourlyAndDailyIntervals() {
        assertDoesNotThrow(() -> MailAnalyticsClient.validateInterval(
                "2026-09-01T01:00:00Z", "2026-09-02T01:00:00Z"));
        assertDoesNotThrow(() -> MailAnalyticsClient.validateInterval(
                "2026-09-01T00:00:00Z", "2026-09-05T00:00:00Z"));
    }

    @Test
    void rejectsUnalignedNonUtcReversedAndOversizedIntervals() {
        assertThrows(IllegalArgumentException.class, () -> MailAnalyticsClient.validateInterval(
                "2026-09-01T01:00:01Z", "2026-09-02T01:00:00Z"));
        assertThrows(IllegalArgumentException.class, () -> MailAnalyticsClient.validateInterval(
                "2026-09-01T01:00:00+01:00", "2026-09-02T01:00:00+01:00"));
        assertThrows(IllegalArgumentException.class, () -> MailAnalyticsClient.validateInterval(
                "2026-09-02T01:00:00Z", "2026-09-01T01:00:00Z"));
        assertThrows(IllegalArgumentException.class, () -> MailAnalyticsClient.validateInterval(
                "2026-09-01T00:00:00Z", "2026-10-03T00:00:00Z"));
        assertThrows(IllegalArgumentException.class, () -> MailAnalyticsClient.validateInterval(
                "2026-09-01T01:00:00Z", "2026-09-05T00:00:00Z"));
    }

    @Test
    void validatesLifecycleObservationHorizon() {
        assertDoesNotThrow(() -> MailAnalyticsClient.validateLifecycle(
                "2026-09-01T00:00:00Z", "2026-09-04T00:00:00Z", "2026-09-05T00:00:00Z"));
        assertThrows(IllegalArgumentException.class, () -> MailAnalyticsClient.validateLifecycle(
                "2026-09-01T00:00:00Z", "2026-09-04T00:00:00Z", "2026-09-03T00:00:00Z"));
        assertThrows(IllegalArgumentException.class, () -> MailAnalyticsClient.validateLifecycle(
                "2026-09-01T00:00:00Z", "2026-09-09T00:00:00Z", "2026-09-10T00:00:00Z"));
        assertThrows(IllegalArgumentException.class, () -> MailAnalyticsClient.validateLifecycle(
                "2026-09-01T00:00:00Z", "2026-09-04T00:00:00Z", "2026-09-10T00:00:00Z"));
    }

    @Test
    void validatesLimitBounds() {
        assertDoesNotThrow(() -> MailAnalyticsClient.validateLimit(null));
        assertDoesNotThrow(() -> MailAnalyticsClient.validateLimit(1));
        assertDoesNotThrow(() -> MailAnalyticsClient.validateLimit(20));
        assertThrows(IllegalArgumentException.class, () -> MailAnalyticsClient.validateLimit(0));
        assertThrows(IllegalArgumentException.class, () -> MailAnalyticsClient.validateLimit(21));
    }
}
