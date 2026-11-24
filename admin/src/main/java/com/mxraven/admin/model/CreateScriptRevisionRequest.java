package com.mxraven.admin.model;

/**
 * Request body for {@code POST /v2/tenants/{slug}/listeners/{id}/script/revisions}.
 *
 * @param source Lua 5.4 source defining {@code function handle(mail, mx)}
 */
public record CreateScriptRevisionRequest(String source) {

    /**
     * Creates a draft request for the given source.
     *
     * @param source Lua source
     * @return the request
     */
    public static CreateScriptRevisionRequest of(String source) {
        return new CreateScriptRevisionRequest(source);
    }
}
