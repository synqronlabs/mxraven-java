package com.mxraven.admin.client;

import com.mxraven.admin.internal.Java8;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.MailAnalyticsActivityHeatmap;
import com.mxraven.admin.model.MailAnalyticsBreakdown;
import com.mxraven.admin.model.MailAnalyticsComparison;
import com.mxraven.admin.model.MailAnalyticsDimension;
import com.mxraven.admin.model.MailAnalyticsDomainLifecycle;
import com.mxraven.admin.model.MailAnalyticsLifecycle;
import com.mxraven.admin.model.MailAnalyticsMetric;
import com.mxraven.admin.model.MailAnalyticsOverview;
import com.mxraven.admin.model.MailAnalyticsSeries;
import com.mxraven.admin.model.MailAnalyticsTaskSizeStatistics;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

/**
 * Tenant-scoped mail analytics for the control-plane v2 API.
 *
 * <p>Every operation is bound to a tenant slug and requires the
 * {@code audit.read} scope. Each endpoint exposes a {@link QueryParams} overload
 * for arbitrary combinations of filters and typed convenience overloads for the
 * fixed parameter sets declared by the contract. All intervals are
 * start-inclusive and end-exclusive.
 */
public final class MailAnalyticsClient {
    private final AdminClient client;
    private final String tenantSlug;

    /**
     * Create a client for the tenant-scoped mail-analytics endpoints.
     *
     * @param client underlying admin client
     * @param tenantSlug tenant slug whose analytics are read
     */
    public MailAnalyticsClient(AdminClient client, String tenantSlug) {
        this.client = client;
        this.tenantSlug = tenantSlug;
    }

    /**
     * Overview of the fixed Phase 1 analytics catalog for the interval.
     *
     * @param startAt inclusive UTC start of the analytics interval
     * @param endAt   exclusive UTC end of the analytics interval
     * @return the overview metrics for the interval
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is missing, malformed,
     *                                  non-UTC, reversed, longer than 31 days, or
     *                                  not aligned to the required boundary
     */
    public MailAnalyticsOverview getMailAnalyticsOverview(String startAt, String endAt)
            throws IOException {
        return getMailAnalyticsOverview(QueryParams.create()
                .put("start_at", startAt)
                .put("end_at", endAt));
    }

    /**
     * Overview of the fixed Phase 1 analytics catalog for the interval.
     *
     * @param params required {@code start_at} and {@code end_at}; the interval may
     *               span at most 31 days
     * @return the overview metrics for the interval
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is missing, malformed,
     *                                  non-UTC, reversed, longer than 31 days, or
     *                                  not aligned to the required boundary
     */
    public MailAnalyticsOverview getMailAnalyticsOverview(QueryParams params)
            throws IOException {
        validateInterval(value(params, "start_at"), value(params, "end_at"));
        return client.get(base() + "/overview", params == null ? null : params.toMap())
                .as(MailAnalyticsOverview.class);
    }

    /**
     * Task lifecycle analytics for the task-origin cohort.
     *
     * @param startAt          inclusive UTC task-origin cohort start
     * @param endAt            exclusive UTC task-origin cohort end
     * @param observationEndAt exclusive UTC upper bound for observed facts; must
     *                         not precede {@code endAt}
     * @return the lifecycle analytics for the cohort
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is invalid, the
     *                                  observation end precedes the cohort end,
     *                                  the cohort exceeds seven days, or the
     *                                  observation horizon exceeds eight days
     */
    public MailAnalyticsLifecycle getMailAnalyticsLifecycle(String startAt, String endAt,
                                                            String observationEndAt) throws IOException {
        return getMailAnalyticsLifecycle(QueryParams.create()
                .put("start_at", startAt)
                .put("end_at", endAt)
                .put("observation_end_at", observationEndAt));
    }

    /**
     * Task lifecycle analytics for the task-origin cohort.
     *
     * @param params required {@code start_at}, {@code end_at}, and
     *               {@code observation_end_at}; the cohort may span at most seven
     *               days and the observation horizon at most eight days
     * @return the lifecycle analytics for the cohort
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is invalid, the
     *                                  observation end precedes the cohort end,
     *                                  the cohort exceeds seven days, or the
     *                                  observation horizon exceeds eight days
     */
    public MailAnalyticsLifecycle getMailAnalyticsLifecycle(QueryParams params)
            throws IOException {
        validateLifecycle(value(params, "start_at"), value(params, "end_at"),
                value(params, "observation_end_at"));
        return client.get(base() + "/lifecycle", params == null ? null : params.toMap())
                .as(MailAnalyticsLifecycle.class);
    }

    /**
     * Bounded breakdown grouping one additive metric by one ledger dimension.
     *
     * @param startAt   inclusive UTC start of the analytics interval
     * @param endAt     exclusive UTC end of the analytics interval
     * @param metric    an allow-listed additive metric
     * @param dimension a compatible ledger dimension
     * @return the ranked breakdown for the interval
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is missing, malformed,
     *                                  non-UTC, reversed, longer than 31 days, or
     *                                  not aligned to the required boundary
     */
    public MailAnalyticsBreakdown getMailAnalyticsBreakdown(String startAt, String endAt,
                                                            MailAnalyticsMetric metric,
                                                            MailAnalyticsDimension dimension) throws IOException {
        return getMailAnalyticsBreakdown(startAt, endAt, metric, dimension, null);
    }

    /**
     * Bounded breakdown grouping one additive metric by one ledger dimension.
     *
     * @param startAt   inclusive UTC start of the analytics interval
     * @param endAt     exclusive UTC end of the analytics interval
     * @param metric    an allow-listed additive metric
     * @param dimension a compatible ledger dimension
     * @param limit     maximum ranked items to return, between 1 and 20
     * @return the ranked breakdown for the interval
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is invalid or {@code limit}
     *                                  is outside 1–20
     */
    public MailAnalyticsBreakdown getMailAnalyticsBreakdown(String startAt, String endAt,
                                                            MailAnalyticsMetric metric,
                                                            MailAnalyticsDimension dimension, Integer limit)
            throws IOException {
        return getMailAnalyticsBreakdown(QueryParams.create()
                .put("start_at", startAt)
                .put("end_at", endAt)
                .put("metric", metric == null ? null : metric.wire())
                .put("dimension", dimension == null ? null : dimension.wire())
                .put("limit", limit));
    }

    /**
     * Bounded breakdown grouping one additive metric by one ledger dimension.
     *
     * @param params required {@code start_at}, {@code end_at}, {@code metric},
     *               and {@code dimension}; optional {@code limit}
     * @return the ranked breakdown for the interval
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is invalid or {@code limit}
     *                                  is outside 1–20
     */
    public MailAnalyticsBreakdown getMailAnalyticsBreakdown(QueryParams params)
            throws IOException {
        validateInterval(value(params, "start_at"), value(params, "end_at"));
        validateLimit(integerValue(params, "limit"));
        return client.get(base() + "/breakdown", params == null ? null : params.toMap())
                .as(MailAnalyticsBreakdown.class);
    }

    /**
     * Overview metrics for the requested interval and the immediately preceding
     * interval of equal duration.
     *
     * @param startAt inclusive UTC start of the analytics interval
     * @param endAt   exclusive UTC end of the analytics interval
     * @return the current and preceding interval metrics
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is missing, malformed,
     *                                  non-UTC, reversed, longer than 31 days, or
     *                                  not aligned to the required boundary
     */
    public MailAnalyticsComparison getMailAnalyticsComparison(String startAt, String endAt)
            throws IOException {
        return getMailAnalyticsComparison(QueryParams.create()
                .put("start_at", startAt)
                .put("end_at", endAt));
    }

    /**
     * Overview metrics for the requested interval and the immediately preceding
     * interval of equal duration.
     *
     * @param params required {@code start_at} and {@code end_at}
     * @return the current and preceding interval metrics
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is missing, malformed,
     *                                  non-UTC, reversed, longer than 31 days, or
     *                                  not aligned to the required boundary
     */
    public MailAnalyticsComparison getMailAnalyticsComparison(QueryParams params)
            throws IOException {
        validateInterval(value(params, "start_at"), value(params, "end_at"));
        return client.get(base() + "/comparison", params == null ? null : params.toMap())
                .as(MailAnalyticsComparison.class);
    }

    /**
     * UTC task activity heatmap with exactly 168 zero-filled weekday/hour cells.
     *
     * @param startAt inclusive UTC start of the analytics interval
     * @param endAt   exclusive UTC end of the analytics interval
     * @return the zero-filled task activity heatmap
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is missing, malformed,
     *                                  non-UTC, reversed, longer than 31 days, or
     *                                  not aligned to the required boundary
     */
    public MailAnalyticsActivityHeatmap getMailAnalyticsActivityHeatmap(String startAt,
                                                                        String endAt) throws IOException {
        return getMailAnalyticsActivityHeatmap(QueryParams.create()
                .put("start_at", startAt)
                .put("end_at", endAt));
    }

    /**
     * UTC task activity heatmap with exactly 168 zero-filled weekday/hour cells.
     *
     * @param params required {@code start_at} and {@code end_at}
     * @return the zero-filled task activity heatmap
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is missing, malformed,
     *                                  non-UTC, reversed, longer than 31 days, or
     *                                  not aligned to the required boundary
     */
    public MailAnalyticsActivityHeatmap getMailAnalyticsActivityHeatmap(QueryParams params)
            throws IOException {
        validateInterval(value(params, "start_at"), value(params, "end_at"));
        return client.get(base() + "/activity-heatmap", params == null ? null : params.toMap())
                .as(MailAnalyticsActivityHeatmap.class);
    }

    /**
     * Bounded additive time series for one allow-listed metric and dimension.
     *
     * @param startAt   inclusive UTC start of the analytics interval
     * @param endAt     exclusive UTC end of the analytics interval
     * @param metric    an allow-listed additive metric
     * @param dimension a compatible ledger dimension
     * @return the additive time series for the interval
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is missing, malformed,
     *                                  non-UTC, reversed, longer than 31 days, or
     *                                  not aligned to the required boundary
     */
    public MailAnalyticsSeries getMailAnalyticsSeries(String startAt, String endAt,
                                                      MailAnalyticsMetric metric,
                                                      MailAnalyticsDimension dimension) throws IOException {
        return getMailAnalyticsSeries(startAt, endAt, metric, dimension, null);
    }

    /**
     * Bounded additive time series for one allow-listed metric and dimension.
     *
     * @param startAt   inclusive UTC start of the analytics interval
     * @param endAt     exclusive UTC end of the analytics interval
     * @param metric    an allow-listed additive metric
     * @param dimension a compatible ledger dimension
     * @param limit     number of top dimension values to select, between 1 and 20
     * @return the additive time series for the interval
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is invalid or {@code limit}
     *                                  is outside 1–20
     */
    public MailAnalyticsSeries getMailAnalyticsSeries(String startAt, String endAt,
                                                      MailAnalyticsMetric metric,
                                                      MailAnalyticsDimension dimension, Integer limit)
            throws IOException {
        return getMailAnalyticsSeries(QueryParams.create()
                .put("start_at", startAt)
                .put("end_at", endAt)
                .put("metric", metric == null ? null : metric.wire())
                .put("dimension", dimension == null ? null : dimension.wire())
                .put("limit", limit));
    }

    /**
     * Bounded additive time series for one allow-listed metric and dimension.
     *
     * @param params required {@code start_at}, {@code end_at}, {@code metric},
     *               and {@code dimension}; optional {@code limit}
     * @return the additive time series for the interval
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is invalid or {@code limit}
     *                                  is outside 1–20
     */
    public MailAnalyticsSeries getMailAnalyticsSeries(QueryParams params) throws IOException {
        validateInterval(value(params, "start_at"), value(params, "end_at"));
        validateLimit(integerValue(params, "limit"));
        return client.get(base() + "/series", params == null ? null : params.toMap())
                .as(MailAnalyticsSeries.class);
    }

    /**
     * Bounded recipient-domain lifecycle analytics for a task-origin cohort.
     *
     * @param startAt          inclusive UTC task-origin cohort start
     * @param endAt            exclusive UTC task-origin cohort end
     * @param observationEndAt exclusive UTC end of the observation horizon
     * @return the recipient-domain lifecycle analytics
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is invalid, the
     *                                  observation end precedes the cohort end,
     *                                  the cohort exceeds seven days, or the
     *                                  observation horizon exceeds eight days
     */
    public MailAnalyticsDomainLifecycle getMailAnalyticsDomainLifecycle(String startAt,
                                                                        String endAt, String observationEndAt)
            throws IOException {
        return getMailAnalyticsDomainLifecycle(startAt, endAt, observationEndAt, null, null);
    }

    /**
     * Bounded recipient-domain lifecycle analytics for a task-origin cohort.
     *
     * @param startAt          inclusive UTC task-origin cohort start
     * @param endAt            exclusive UTC task-origin cohort end
     * @param observationEndAt exclusive UTC end of the observation horizon
     * @param recipientDomain  optional recipient-domain filter
     * @param limit            maximum ranked domains to return, between 1 and 20
     * @return the recipient-domain lifecycle analytics
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is invalid, the
     *                                  observation end precedes the cohort end,
     *                                  the cohort exceeds seven days, the
     *                                  observation horizon exceeds eight days, or
     *                                  {@code limit} is outside 1–20
     */
    public MailAnalyticsDomainLifecycle getMailAnalyticsDomainLifecycle(String startAt,
                                                                        String endAt, String observationEndAt,
                                                                        String recipientDomain, Integer limit)
            throws IOException {
        return getMailAnalyticsDomainLifecycle(QueryParams.create()
                .put("start_at", startAt)
                .put("end_at", endAt)
                .put("observation_end_at", observationEndAt)
                .put("recipient_domain", recipientDomain)
                .put("limit", limit));
    }

    /**
     * Bounded recipient-domain lifecycle analytics for a task-origin cohort.
     *
     * @param params required {@code start_at}, {@code end_at}, and
     *               {@code observation_end_at}; optional {@code recipient_domain}
     *               and {@code limit}
     * @return the recipient-domain lifecycle analytics
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is invalid, the
     *                                  observation bounds are exceeded, or
     *                                  {@code limit} is outside 1–20
     */
    public MailAnalyticsDomainLifecycle getMailAnalyticsDomainLifecycle(QueryParams params)
            throws IOException {
        validateLifecycle(value(params, "start_at"), value(params, "end_at"),
                value(params, "observation_end_at"));
        validateLimit(integerValue(params, "limit"));
        return client.get(base() + "/domain-lifecycle", params == null ? null : params.toMap())
                .as(MailAnalyticsDomainLifecycle.class);
    }

    /**
     * Zero-filled UTC buckets with averages and approximate t-digest percentiles
     * over task-created message sizes.
     *
     * @param startAt inclusive UTC start of the analytics interval
     * @param endAt   exclusive UTC end of the analytics interval
     * @return the task-size statistics for the interval
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is missing, malformed,
     *                                  non-UTC, reversed, longer than 31 days, or
     *                                  not aligned to the required boundary
     */
    public MailAnalyticsTaskSizeStatistics getMailAnalyticsTaskSizeStatistics(String startAt,
                                                                              String endAt) throws IOException {
        return getMailAnalyticsTaskSizeStatistics(QueryParams.create()
                .put("start_at", startAt)
                .put("end_at", endAt));
    }

    /**
     * Zero-filled UTC buckets with averages and approximate t-digest percentiles
     * over task-created message sizes.
     *
     * @param params required {@code start_at} and {@code end_at}
     * @return the task-size statistics for the interval
     * @throws IOException if the request fails or is interrupted
     * @throws IllegalArgumentException if the interval is missing, malformed,
     *                                  non-UTC, reversed, longer than 31 days, or
     *                                  not aligned to the required boundary
     */
    public MailAnalyticsTaskSizeStatistics getMailAnalyticsTaskSizeStatistics(QueryParams params)
            throws IOException {
        validateInterval(value(params, "start_at"), value(params, "end_at"));
        return client.get(base() + "/task-size-statistics", params == null ? null : params.toMap())
                .as(MailAnalyticsTaskSizeStatistics.class);
    }

    private static final Duration MAX_INTERVAL = Duration.ofDays(31);
    private static final Duration HOURLY_THRESHOLD = Duration.ofHours(72);
    private static final Duration LIFECYCLE_MAX_COHORT = Duration.ofDays(7);
    private static final Duration LIFECYCLE_MAX_OBSERVATION = Duration.ofDays(8);

    /** Validates the shared {@code start_at}/{@code end_at} interval contract. */
    static void validateInterval(String startAt, String endAt) {
        Instant start = parseUtcInstant(startAt, "start_at");
        Instant end = parseUtcInstant(endAt, "end_at");
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("start_at must be before end_at");
        }
        Duration duration = Duration.between(start, end);
        if (duration.compareTo(MAX_INTERVAL) > 0) {
            throw new IllegalArgumentException("the analytics interval may not exceed 31 days");
        }
        Duration step = duration.compareTo(HOURLY_THRESHOLD) <= 0 ? Duration.ofHours(1) : Duration.ofDays(1);
        if (!alignsTo(start, step) || !alignsTo(end, step)) {
            throw new IllegalArgumentException("start_at and end_at must align to UTC "
                    + (step.toHours() == 1 ? "hour" : "day") + " boundaries");
        }
    }

    /** Validates an interval plus the lifecycle observation horizon. */
    static void validateLifecycle(String startAt, String endAt, String observationEndAt) {
        validateInterval(startAt, endAt);
        Instant start = parseUtcInstant(startAt, "start_at");
        Instant end = parseUtcInstant(endAt, "end_at");
        Instant observationEnd = parseUtcInstant(observationEndAt, "observation_end_at");
        if (observationEnd.isBefore(end)) {
            throw new IllegalArgumentException("lifecycle observation end must not precede the cohort end");
        }
        if (Duration.between(start, end).compareTo(LIFECYCLE_MAX_COHORT) > 0) {
            throw new IllegalArgumentException("lifecycle cohort may not exceed 7 days");
        }
        if (Duration.between(start, observationEnd).compareTo(LIFECYCLE_MAX_OBSERVATION) > 0) {
            throw new IllegalArgumentException("lifecycle observation horizon may not exceed 8 days");
        }
    }

    /** Validates the optional ranked-item limit (1-20). */
    static void validateLimit(Integer limit) {
        if (limit != null && (limit < 1 || limit > 20)) {
            throw new IllegalArgumentException("limit must be between 1 and 20");
        }
    }

    static Instant parseUtcInstant(String value, String field) {
        if (value == null || Java8.isBlank(value)) {
            throw new IllegalArgumentException(field + " is required");
        }
        try {
            OffsetDateTime parsed = OffsetDateTime.parse(value.trim());
            if (parsed.getOffset().getTotalSeconds() != 0) {
                throw new IllegalArgumentException(field + " must use UTC");
            }
            return parsed.toInstant();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    field + " must be an ISO-8601 timestamp with a UTC offset");
        }
    }

    private static boolean alignsTo(Instant instant, Duration step) {
        long seconds = step.getSeconds();
        return seconds > 0 && instant.getEpochSecond() % seconds == 0;
    }

    private static String value(QueryParams params, String key) {
        return params == null ? null : params.toMap().get(key);
    }

    private static Integer integerValue(QueryParams params, String key) {
        String raw = value(params, key);
        if (raw == null) {
            return null;
        }
        try {
            return Integer.valueOf(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(key + " must be an integer");
        }
    }

    private String base() {
        return "/tenants/" + seg(tenantSlug) + "/mail-analytics";
    }

    private String seg(String value) {
        return Java8.urlEncode(value);
    }
}
