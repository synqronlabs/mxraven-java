package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One validated routing effect. Only the fields its kind uses are present.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ScriptIntent {
    private final ScriptIntentKind kind;
    private final String header;
    private final String value;
    private final String address;
    private final String ref;
    private final String pool;
    private final String key;
    private final String template;
    private final Integer code;
    private final String enhanced;
    private final String text;
    private final String reason;

    /** the effect kind */
    public ScriptIntentKind kind() {
        return kind;
    }

    /** header name for header effects */
    public String header() {
        return header;
    }

    /** header value for set and append */
    public String value() {
        return value;
    }

    /** recipient address for {@code recipients.add} */
    public String address() {
        return address;
    }

    /** destination reference for forward, relay, smarthost, and archive */
    public String ref() {
        return ref;
    }

    /** dedicated IP pool reference */
    public String pool() {
        return pool;
    }

    /** object key for {@code archive.s3} */
    public String key() {
        return key;
    }

    /** auto-reply template reference */
    public String template() {
        return template;
    }

    /** SMTP reply code for reject and defer */
    public Integer code() {
        return code;
    }

    /** RFC 3463 enhanced status code */
    public String enhanced() {
        return enhanced;
    }

    /** SMTP reply text */
    public String text() {
        return text;
    }

    public String reason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScriptIntent that = (ScriptIntent) o;
        return Objects.equals(this.kind, that.kind) && Objects.equals(this.header, that.header) && Objects.equals(this.value, that.value) && Objects.equals(this.address, that.address) && Objects.equals(this.ref, that.ref) && Objects.equals(this.pool, that.pool) && Objects.equals(this.key, that.key) && Objects.equals(this.template, that.template) && Objects.equals(this.code, that.code) && Objects.equals(this.enhanced, that.enhanced) && Objects.equals(this.text, that.text) && Objects.equals(this.reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.kind, this.header, this.value, this.address, this.ref, this.pool, this.key, this.template, this.code, this.enhanced, this.text, this.reason);
    }

    @Override
    public String toString() {
        return "ScriptIntent[" + "kind=" + this.kind + ", " + "header=" + this.header + ", " + "value=" + this.value + ", " + "address=" + this.address + ", " + "ref=" + this.ref + ", " + "pool=" + this.pool + ", " + "key=" + this.key + ", " + "template=" + this.template + ", " + "code=" + this.code + ", " + "enhanced=" + this.enhanced + ", " + "text=" + this.text + ", " + "reason=" + this.reason + "]";
    }

    /**
     * Creates a new ScriptIntent.
     *
     * @param kind the effect kind
     * @param header header name for header effects
     * @param value header value for set and append
     * @param address recipient address for {@code recipients.add}
     * @param ref destination reference for forward, relay, smarthost, and archive
     * @param pool dedicated IP pool reference
     * @param key object key for {@code archive.s3}
     * @param template auto-reply template reference
     * @param code SMTP reply code for reject and defer
     * @param enhanced RFC 3463 enhanced status code
     * @param text SMTP reply text
     * @param reason reason
     */
    @JsonCreator
    public ScriptIntent(ScriptIntentKind kind, String header, String value, String address, String ref, String pool, String key, String template, Integer code, String enhanced, String text, String reason) {
        this.kind = kind;
        this.header = header;
        this.value = value;
        this.address = address;
        this.ref = ref;
        this.pool = pool;
        this.key = key;
        this.template = template;
        this.code = code;
        this.enhanced = enhanced;
        this.text = text;
        this.reason = reason;
    }
}
