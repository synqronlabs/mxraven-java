package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * A listener routing rule. Rules are evaluated in ascending {@code priority}.
 */
public final class RoutingRule extends Entity<RoutingRuleData> {
    /**
     * Creates a routing rule bound to the given client, path, and data.
     *
     * @param client the admin client
     * @param path   the resource path
     * @param data   the backing data
     */
    public RoutingRule(AdminClient client, String path, RoutingRuleData data) {
        super(client, path, data);
    }

    private RoutingRule(AdminClient client, String path, RoutingRuleData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    /**
     * Returns the rule identifier.
     *
     * @return the rule identifier
     */
    public String id() {
        return data.id();
    }

    /**
     * Returns the owning listener identifier.
     *
     * @return the owning listener identifier
     */
    public String listenerId() {
        return data.listenerId();
    }

    /**
     * Returns the evaluation priority.
     *
     * @return the evaluation priority
     */
    public int priority() {
        return data.priority();
    }

    /**
     * Returns the rule expression text.
     *
     * @return the rule expression text
     */
    public String expressionText() {
        return data.expressionText();
    }

    /**
     * Returns the action applied when the expression matches.
     *
     * @return the action applied when the expression matches
     */
    public RoutingRuleAction action() {
        return data.action();
    }

    /**
     * Returns whether the rule is enabled.
     *
     * @return {@code true} if the rule is enabled
     */
    public boolean isActive() {
        return data.isActive();
    }

    /**
     * Reloads the rule from the server.
     *
     * @return a fresh snapshot
     * @throws IOException if the request fails
     */
    public RoutingRule reload() throws IOException {
        return new RoutingRule(client, path, client.get(path).as(RoutingRuleData.class), isDeleted());
    }

    /**
     * Replaces the writable rule definition; priority is unchanged.
     *
     * @param expressionText rule expression text
     * @param action         action applied when the expression matches
     * @return the updated rule
     * @throws IOException if the request fails
     */
    public RoutingRule replace(String expressionText, RoutingRuleAction action) throws IOException {
        return replace(new ReplaceRoutingRuleRequest(expressionText, action));
    }

    /**
     * Replaces the writable rule definition using the configured builder.
     *
     * @param configure configures the replacement request
     * @return the updated rule
     * @throws IOException if the request fails
     */
    public RoutingRule replace(Consumer<ReplaceRoutingRuleRequest.Builder> configure) throws IOException {
        ReplaceRoutingRuleRequest.Builder builder = ReplaceRoutingRuleRequest.builder();
        configure.accept(builder);
        return replace(builder.build());
    }

    /**
     * Replaces the writable rule definition with a full request.
     *
     * @param request replacement request
     * @return the updated rule
     * @throws IOException if the request fails
     */
    public RoutingRule replace(ReplaceRoutingRuleRequest request) throws IOException {
        return new RoutingRule(client, path, client.put(path, request).as(RoutingRuleData.class), isDeleted());
    }

    /**
     * Enables or disables the rule without changing its priority.
     *
     * @param isActive whether the rule is enabled
     * @return the updated rule
     * @throws IOException if the request fails
     */
    public RoutingRule setActive(boolean isActive) throws IOException {
        return setActive(new SetRoutingRuleActiveRequest(isActive));
    }

    /**
     * Enables or disables the rule using the configured builder.
     *
     * @param configure configures the active-state request
     * @return the updated rule
     * @throws IOException if the request fails
     */
    public RoutingRule setActive(Consumer<SetRoutingRuleActiveRequest.Builder> configure) throws IOException {
        SetRoutingRuleActiveRequest.Builder builder = SetRoutingRuleActiveRequest.builder();
        configure.accept(builder);
        return setActive(builder.build());
    }

    /**
     * Enables or disables the rule with a full request.
     *
     * @param request active-state request
     * @return the updated rule
     * @throws IOException if the request fails
     */
    public RoutingRule setActive(SetRoutingRuleActiveRequest request) throws IOException {
        return new RoutingRule(client, path,
                client.put(path + "/active", request).as(RoutingRuleData.class), isDeleted());
    }

    /**
     * Moves the rule to a new priority.
     *
     * @param priority new evaluation order; lower values run first
     * @return the updated rule
     * @throws IOException if the request fails
     */
    public RoutingRule reorder(int priority) throws IOException {
        return reorder(new ReorderRoutingRuleRequest(priority));
    }

    /**
     * Moves the rule to a new priority using the configured builder.
     *
     * @param configure configures the reorder request
     * @return the updated rule
     * @throws IOException if the request fails
     */
    public RoutingRule reorder(Consumer<ReorderRoutingRuleRequest.Builder> configure) throws IOException {
        ReorderRoutingRuleRequest.Builder builder = ReorderRoutingRuleRequest.builder();
        configure.accept(builder);
        return reorder(builder.build());
    }

    /**
     * Moves the rule to a new priority with a full request.
     *
     * @param request reorder request
     * @return the updated rule
     * @throws IOException if the request fails
     */
    public RoutingRule reorder(ReorderRoutingRuleRequest request) throws IOException {
        return new RoutingRule(client, path,
                client.put(path + "/priority", request).as(RoutingRuleData.class), isDeleted());
    }
}
