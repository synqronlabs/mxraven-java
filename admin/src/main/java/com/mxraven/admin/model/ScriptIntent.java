package com.mxraven.admin.model;

/**
 * One validated routing effect. Only the fields its kind uses are present.
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
 * @param reason machine-readable reason recorded for the effect
 */
public record ScriptIntent(
        ScriptIntentKind kind,
        String header,
        String value,
        String address,
        String ref,
        String pool,
        String key,
        String template,
        Integer code,
        String enhanced,
        String text,
        String reason) {
}
