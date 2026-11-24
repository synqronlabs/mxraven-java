package com.mxraven.admin.client;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.QueryParams;
import com.mxraven.admin.model.CreateScriptRevisionRequest;
import com.mxraven.admin.model.ExportedListenerScript;
import com.mxraven.admin.model.ListenerScript;
import com.mxraven.admin.model.ListenerType;
import com.mxraven.admin.model.RollbackListenerScriptRequest;
import com.mxraven.admin.model.ScriptRevision;
import com.mxraven.admin.model.ScriptRevisionSummary;
import com.mxraven.admin.model.ScriptSimulationResult;
import com.mxraven.admin.model.SimulateScriptRevisionRequest;
import com.mxraven.admin.model.UpdateListenerScriptBodyAccessRequest;

import java.io.IOException;

/**
 * A single listener's Lua routing script, obtained from
 * {@link com.mxraven.admin.model.Listener#routingScripts()}.
 *
 * <p>Each listener has at most one published revision. A listener without a
 * published script uses its listener-type default: submission listeners
 * deliver through the shared pool, and MTA listeners drop after acceptance.
 *
 * <p>Drafts are compiled and validated immediately. A draft is saved even when
 * it has errors so work is never lost; inspect
 * {@link ScriptRevision#publishable()} and {@link ScriptRevision#diagnostics()}
 * before publishing.
 */
public final class RoutingScriptsClient {
    private final AdminClient client;
    private final String basePath;
    private final ListenerType listenerType;

    /**
     * Creates a client for one listener's routing script.
     *
     * @param client underlying admin client
     * @param basePath client-relative script path
     * @param listenerType owning listener type
     */
    public RoutingScriptsClient(AdminClient client, String basePath, ListenerType listenerType) {
        this.client = client;
        this.basePath = basePath;
        this.listenerType = listenerType;
    }

    /**
     * Returns the listener's routing script: its published revision, when one
     * exists, and its body-access setting.
     *
     * @return the listener routing script
     * @throws IOException if the request fails or is interrupted
     */
    public ListenerScript get() throws IOException {
        return client.get(basePath).as(ListenerScript.class);
    }

    /**
     * Removes the listener's published routing script.
     *
     * <p>The listener runs its listener-type default again: submission delivers
     * through the shared pool, and an MTA listener drops every message it
     * accepts. The published revision becomes superseded and the revision
     * history is kept, so {@link #rollback(int)} can restore it. The operation
     * is idempotent.
     *
     * @return the listener routing script with no published revision
     * @throws IOException if the request fails or is interrupted
     */
    public ListenerScript delete() throws IOException {
        return client.delete(basePath).as(ListenerScript.class);
    }

    /**
     * Saves a new draft revision and validates it immediately.
     *
     * @param source Lua 5.4 source defining {@code function handle(mail, mx)}
     * @return the saved draft
     * @throws IOException if the request fails or is interrupted
     */
    public ScriptRevision createRevision(String source) throws IOException {
        return createRevision(CreateScriptRevisionRequest.of(source));
    }

    /**
     * Saves a new draft revision and validates it immediately.
     *
     * @param request draft request
     * @return the saved draft
     * @throws IOException if the request fails or is interrupted
     */
    public ScriptRevision createRevision(CreateScriptRevisionRequest request) throws IOException {
        return client.post(basePath + "/revisions", request).as(ScriptRevision.class);
    }

    /**
     * Returns one revision's source, dependencies, and save-time diagnostics.
     *
     * @param revision revision number, starting at 1
     * @return the revision
     * @throws IOException if the request fails or is interrupted
     */
    public ScriptRevision getRevision(int revision) throws IOException {
        return client.get(revisionPath(revision)).as(ScriptRevision.class);
    }

    /**
     * Starts a typed list request over the retained revision history.
     *
     * @return a new revision query
     */
    public ScriptRevisionQuery query() {
        return new ScriptRevisionQuery(this);
    }

    /**
     * Lists retained revisions, newest first.
     *
     * @return a lazily paginated collection of revision summaries
     * @throws IOException if the first page request fails or is interrupted
     */
    public Paged<ScriptRevisionSummary> listRevisions() throws IOException {
        return listRevisions(null);
    }

    Paged<ScriptRevisionSummary> listRevisions(QueryParams params) throws IOException {
        return client.paged(basePath + "/revisions", params == null ? null : params.toMap(),
                ScriptRevisionSummary.class);
    }

    /**
     * Runs a revision in the production sandbox against a synthesized fixture.
     *
     * <p>No real HTTP request is made: {@code mx.http} answers from the supplied
     * stubs, and recipient sets come from the request. Nothing is delivered or
     * stored.
     *
     * @param revision revision number to simulate
     * @param request simulation fixture, stubs, and recipient sets
     * @return the simulation result
     * @throws IOException if the request fails or is interrupted
     */
    public ScriptSimulationResult simulate(int revision, SimulateScriptRevisionRequest request) throws IOException {
        return client.post(revisionPath(revision) + "/simulate", request).as(ScriptSimulationResult.class);
    }

    /**
     * Makes a publishable draft the listener's live script.
     *
     * <p>Dependencies are re-resolved before the active pointer moves, and
     * runtimes pick up the change within 30 seconds.
     *
     * @param revision revision number to publish
     * @return the updated listener script
     * @throws IOException if the request fails or is interrupted
     */
    public ListenerScript publish(int revision) throws IOException {
        return client.post(revisionPath(revision) + "/publish", null).as(ListenerScript.class);
    }

    /**
     * Points the listener at a previously published, now superseded revision.
     *
     * <p>The revision's dependencies are re-resolved before it becomes live
     * again.
     *
     * @param revision revision number to make live
     * @return the updated listener script
     * @throws IOException if the request fails or is interrupted
     */
    public ListenerScript rollback(int revision) throws IOException {
        return client.post(basePath + "/rollback", new RollbackListenerScriptRequest(revision))
                .as(ListenerScript.class);
    }

    /**
     * Renders the currently published revision's source and dependency manifest.
     *
     * @return the exported script
     * @throws IOException if the request fails or is interrupted
     */
    public ExportedListenerScript export() throws IOException {
        return client.post(basePath + "/export", null).as(ExportedListenerScript.class);
    }

    /**
     * Opts an MTA listener's script in or out of reading the spooled
     * text/plain body with {@code mail.body:text()}.
     *
     * <p>Submission listeners cannot enable body access; the control plane
     * rejects the request.
     *
     * @param enabled whether body access is enabled
     * @return the updated listener script
     * @throws IOException if the request fails or is interrupted
     */
    public ListenerScript setBodyAccess(boolean enabled) throws IOException {
        return client.put(basePath + "/body-access", new UpdateListenerScriptBodyAccessRequest(enabled))
                .as(ListenerScript.class);
    }

    /**
     * Returns the type of the listener that owns this script.
     *
     * @return the listener type
     */
    public ListenerType listenerType() {
        return listenerType;
    }

    private String revisionPath(int revision) {
        if (revision < 1) {
            throw new IllegalArgumentException("revision must be at least 1");
        }
        return basePath + "/revisions/" + revision;
    }
}
