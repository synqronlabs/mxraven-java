package com.mxraven.admin.model;

/**
 * The published routing script rendered for export.
 *
 * @param listenerId owning listener identifier
 * @param revision published revision number
 * @param source Lua source
 * @param dependencies resolved dependency manifest
 */
public record ExportedListenerScript(
        String listenerId,
        int revision,
        String source,
        ScriptDependencies dependencies) {
}
