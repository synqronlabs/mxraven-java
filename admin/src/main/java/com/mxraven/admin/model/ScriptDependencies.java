package com.mxraven.admin.model;

import java.util.List;

/**
 * A script revision's dependency manifest: the only resource references its
 * effects may use. References are resolved and ownership-checked when the
 * revision is saved.
 *
 * @param forward SMTP forward-destination references
 * @param relay SMTP relay references
 * @param storage storage-integration references
 * @param endpoint webhook-endpoint references used by {@code mx.http}
 * @param template auto-reply-template references
 * @param pool dedicated IP pool references
 * @param sets recipient-set names
 */
public record ScriptDependencies(
        List<String> forward,
        List<String> relay,
        List<String> storage,
        List<String> endpoint,
        List<String> template,
        List<String> pool,
        List<String> sets) {

    /**
     * Creates a dependency manifest, copying every list.
     */
    public ScriptDependencies {
        forward = forward == null ? List.of() : List.copyOf(forward);
        relay = relay == null ? List.of() : List.copyOf(relay);
        storage = storage == null ? List.of() : List.copyOf(storage);
        endpoint = endpoint == null ? List.of() : List.copyOf(endpoint);
        template = template == null ? List.of() : List.copyOf(template);
        pool = pool == null ? List.of() : List.copyOf(pool);
        sets = sets == null ? List.of() : List.copyOf(sets);
    }
}
