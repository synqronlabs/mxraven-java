package com.mxraven.admin.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxraven.admin.Problem;
import com.mxraven.admin.ProblemError;

import java.time.Duration;
import java.util.List;

/**
 * A non-successful control-plane API response.
 *
 * <p>Branch on {@link #code()} rather than on the human-readable title or
 * detail. The full decoded {@link Problem} is available from {@link #problem()}.
 *
 * <p>Common HTTP statuses are surfaced as typed subclasses so callers can catch
 * the category they care about without inspecting the status:
 *
 * <pre>{@code
 * try {
 *     ws.domains().create(new CreateDomainRequest("example.com"));
 * } catch (NotFoundException e) {
 *     // 404
 * } catch (ConflictException e) {
 *     // 409
 * } catch (ValidationException e) {
 *     e.errors().forEach(err -> System.err.println(err.pointer() + " -> " + err.code()));
 * } catch (ApiException e) {
 *     // anything else
 * }
 * }</pre>
 */
public class ApiException extends MxRavenException {
    /** HTTP status code of the response. */
    private final int status;
    /** Stable machine-readable error code, or {@code null} when absent. */
    private final String code;
    /** Short human-readable problem title, or {@code null} when absent. */
    private final String title;
    /** Human-readable problem detail, or the raw response body as fallback. */
    private final String detail;
    /** Support correlation identifier, or {@code null} when absent. */
    private final String traceId;
    /** Actionable validation issues; empty when none were provided. */
    private final List<ProblemError> errors;
    /** Full decoded problem, or {@code null} when the body could not be decoded. */
    private final Problem problem;
    /** Server-requested retry delay, or {@code null} when unavailable. */
    private final Duration retryAfter;

    /**
     * Construct an exception from a raw response status and body. When the body
     * carries a decodable problem its fields are used; otherwise the raw body is
     * kept as the fallback detail.
     *
     * @param status HTTP status code
     * @param body raw response body, or {@code null}
     */
    public ApiException(int status, String body) {
        this(status, null, problemOrNull(status, body));
    }

    ApiException(int status, String fallbackDetail, Problem problem) {
        this(status, fallbackDetail, problem, null);
    }

    ApiException(int status, String fallbackDetail, Problem problem, Duration retryAfter) {
        super(message(status, problem, fallbackDetail));
        this.status = status;
        this.problem = problem;
        this.retryAfter = retryAfter;
        this.code = problem == null ? null : problem.code();
        this.title = problem == null ? null : problem.title();
        this.detail = problem == null ? fallbackDetail : problem.detail();
        this.traceId = problem == null ? null : problem.traceId();
        this.errors = problem == null || problem.errors() == null ? List.of() : List.copyOf(problem.errors());
    }

    /**
     * Decodes an exception from a response. The decoded body is treated as an
     * RFC 9457 problem when it is an object with a {@code code} field, and a
     * typed subclass is chosen from the status.
     *
     * @param status HTTP status code
     * @param rawBody raw response body, or {@code null}
     * @param body decoded response body, or {@code null}
     * @param mapper mapper used to convert the body to a {@link Problem}
     * @return an exception carrying the decoded problem details
     */
    public static ApiException from(int status, String rawBody, JsonNode body, ObjectMapper mapper) {
        return from(status, rawBody, body, mapper, null);
    }

    /**
     * Decodes an exception from a response, including the server-requested retry
     * delay.
     *
     * @param status HTTP status code
     * @param rawBody raw response body, or {@code null}
     * @param body decoded response body, or {@code null}
     * @param mapper mapper used to convert the body to a {@link Problem}
     * @param retryAfter server-requested retry delay, or {@code null}
     * @return an exception carrying the decoded problem details
     */
    public static ApiException from(int status, String rawBody, JsonNode body, ObjectMapper mapper,
                                    Duration retryAfter) {
        Problem problem = null;
        if (body != null && body.isObject() && body.hasNonNull("code")) {
            try {
                problem = mapper.convertValue(body, Problem.class);
            } catch (IllegalArgumentException ignored) {
                problem = null;
            }
        }
        String fallback = rawBody == null || rawBody.isBlank() ? null : rawBody;
        return forStatus(status, fallback, problem, retryAfter);
    }

    private static ApiException forStatus(int status, String fallbackDetail, Problem problem, Duration retryAfter) {
        return switch (status) {
            case 400 -> new BadRequestException(fallbackDetail, problem);
            case 401 -> new AuthenticationException(fallbackDetail, problem);
            case 403 -> new PermissionDeniedException(fallbackDetail, problem);
            case 404 -> new NotFoundException(fallbackDetail, problem);
            case 409 -> new ConflictException(fallbackDetail, problem);
            case 422 -> new ValidationException(fallbackDetail, problem);
            case 429 -> new RateLimitException(fallbackDetail, problem, retryAfter);
            default -> status >= 500
                    ? new ServerException(status, fallbackDetail, problem, retryAfter)
                    : new ApiException(status, fallbackDetail, problem, retryAfter);
        };
    }

    private static Problem problemOrNull(int status, String body) {
        if (body == null || body.isBlank()) {
            return null;
        }
        try {
            return new ObjectMapper().readValue(body, Problem.class);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String message(int status, Problem problem, String fallbackDetail) {
        String code = problem == null ? null : problem.code();
        String detail = problem == null ? fallbackDetail : problem.detail();
        StringBuilder sb = new StringBuilder("HTTP ").append(status);
        if (code != null) {
            sb.append(" ").append(code);
        }
        if (detail != null && !detail.isBlank()) {
            sb.append(": ").append(detail);
        } else if (fallbackDetail != null && !fallbackDetail.isBlank()) {
            sb.append(": ").append(fallbackDetail);
        }
        return sb.toString();
    }

    /**
     * Returns the HTTP status code.
     *
     * @return the status code
     */
    public int status() {
        return status;
    }

    /**
     * Returns the stable machine-readable error code, or {@code null} when absent.
     *
     * @return the error code
     */
    public String code() {
        return code;
    }

    /**
     * Returns the short human-readable problem title, or {@code null} when absent.
     *
     * @return the problem title
     */
    public String title() {
        return title;
    }

    /**
     * Returns the human-readable problem detail, or the raw response body when
     * no problem could be decoded.
     *
     * @return the problem detail
     */
    public String detail() {
        return detail;
    }

    /**
     * Returns the identifier suitable for support correlation, or {@code null}
     * when absent.
     *
     * @return the trace identifier
     */
    public String traceId() {
        return traceId;
    }

    /**
     * Returns the actionable validation issues, empty when none were provided.
     *
     * @return the list of problem errors
     */
    public List<ProblemError> errors() {
        return errors;
    }

    /**
     * Returns the full decoded problem, or {@code null} when the body could not
     * be decoded as one.
     *
     * @return the decoded problem
     */
    public Problem problem() {
        return problem;
    }

    /**
     * The server-requested delay before retrying, parsed from {@code Retry-After}
     * when present. {@code null} when the response carried no usable value.
     *
     * @return the retry delay, or {@code null} when unavailable
     */
    public Duration retryAfter() {
        return retryAfter;
    }
}
