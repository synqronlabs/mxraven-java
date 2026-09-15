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
    private final int status;
    private final String code;
    private final String title;
    private final String detail;
    private final String traceId;
    private final List<ProblemError> errors;
    private final Problem problem;
    private final Duration retryAfter;

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

    public static ApiException from(int status, String rawBody, JsonNode body, ObjectMapper mapper) {
        return from(status, rawBody, body, mapper, null);
    }

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

    public int status() {
        return status;
    }

    public String code() {
        return code;
    }

    public String title() {
        return title;
    }

    public String detail() {
        return detail;
    }

    public String traceId() {
        return traceId;
    }

    public List<ProblemError> errors() {
        return errors;
    }

    public Problem problem() {
        return problem;
    }

    /**
     * The server-requested delay before retrying, parsed from {@code Retry-After}
     * when present. {@code null} when the response carried no usable value.
     */
    public Duration retryAfter() {
        return retryAfter;
    }
}
