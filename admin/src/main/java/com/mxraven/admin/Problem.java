package com.mxraven.admin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * RFC 9457 Problem Details as returned by the v2 control plane with the
 * {@code application/problem+json} media type.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Problem {
    private final String type;
    private final String title;
    private final Integer status;
    private final String code;
    private final String detail;
    private final String instance;
    private final String traceId;
    private final List<ProblemError> errors;

    /** stable URI identifying the problem category */
    public String type() {
        return type;
    }

    /** short human-readable summary of the problem category */
    public String title() {
        return title;
    }

    /** HTTP status code returned for this occurrence */
    public Integer status() {
        return status;
    }

    /** stable machine-readable mxRaven error code; branch on this */
    public String code() {
        return code;
    }

    /** safe human-readable detail for this occurrence */
    public String detail() {
        return detail;
    }

    /** URI reference identifying this occurrence, when available */
    public String instance() {
        return instance;
    }

    /** identifier suitable for support correlation */
    public String traceId() {
        return traceId;
    }

    /** optional actionable validation issues */
    public List<ProblemError> errors() {
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Problem that = (Problem) o;
        return Objects.equals(this.type, that.type)
                && Objects.equals(this.title, that.title)
                && Objects.equals(this.status, that.status)
                && Objects.equals(this.code, that.code)
                && Objects.equals(this.detail, that.detail)
                && Objects.equals(this.instance, that.instance)
                && Objects.equals(this.traceId, that.traceId)
                && Objects.equals(this.errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.title, this.status, this.code, this.detail, this.instance, this.traceId, this.errors);
    }

    @Override
    public String toString() {
        return "Problem[" + "type=" + this.type + ", " + "title=" + this.title + ", " + "status=" + this.status + ", " + "code=" + this.code + ", " + "detail=" + this.detail + ", " + "instance=" + this.instance + ", " + "traceId=" + this.traceId + ", " + "errors=" + this.errors + "]";
    }

    /**
     * Creates a new Problem.
     *
     * @param type stable URI identifying the problem category
     * @param title short human-readable summary of the problem category
     * @param status HTTP status code returned for this occurrence
     * @param code stable machine-readable mxRaven error code; branch on this
     * @param detail safe human-readable detail for this occurrence
     * @param instance URI reference identifying this occurrence, when available
     * @param traceId identifier suitable for support correlation
     * @param errors optional actionable validation issues
     */
    @JsonCreator
    public Problem(String type, String title, Integer status, String code, String detail, String instance, String traceId, List<ProblemError> errors) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.code = code;
        this.detail = detail;
        this.instance = instance;
        this.traceId = traceId;
        this.errors = errors;
    }
}
