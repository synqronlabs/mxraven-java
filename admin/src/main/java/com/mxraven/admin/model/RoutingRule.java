package com.mxraven.admin.model;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Entity;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * A listener routing rule. Rules are evaluated in ascending {@code priority}.
 */
public final class RoutingRule extends Entity<RoutingRuleData> {
    public RoutingRule(AdminClient client, String path, RoutingRuleData data) {
        super(client, path, data);
    }

    private RoutingRule(AdminClient client, String path, RoutingRuleData data, boolean deleted) {
        super(client, path, data, deleted);
    }

    public String id() {
        return data.id();
    }

    public String listenerId() {
        return data.listenerId();
    }

    public int priority() {
        return data.priority();
    }

    public String expressionText() {
        return data.expressionText();
    }

    public RoutingRuleAction action() {
        return data.action();
    }

    public boolean isActive() {
        return data.isActive();
    }

    public RoutingRule reload() throws IOException {
        return new RoutingRule(client, path, client.get(path).as(RoutingRuleData.class), isDeleted());
    }

    /** Full replacement of the writable rule definition; priority is unchanged. */
    public RoutingRule replace(String expressionText, RoutingRuleAction action) throws IOException {
        return replace(new ReplaceRoutingRuleRequest(expressionText, action));
    }

    public RoutingRule replace(Consumer<ReplaceRoutingRuleRequest.Builder> configure) throws IOException {
        ReplaceRoutingRuleRequest.Builder builder = ReplaceRoutingRuleRequest.builder();
        configure.accept(builder);
        return replace(builder.build());
    }

    public RoutingRule replace(ReplaceRoutingRuleRequest request) throws IOException {
        return new RoutingRule(client, path, client.put(path, request).as(RoutingRuleData.class), isDeleted());
    }

    /** Enables or disables the rule without changing its priority. */
    public RoutingRule setActive(boolean isActive) throws IOException {
        return setActive(new SetRoutingRuleActiveRequest(isActive));
    }

    public RoutingRule setActive(Consumer<SetRoutingRuleActiveRequest.Builder> configure) throws IOException {
        SetRoutingRuleActiveRequest.Builder builder = SetRoutingRuleActiveRequest.builder();
        configure.accept(builder);
        return setActive(builder.build());
    }

    public RoutingRule setActive(SetRoutingRuleActiveRequest request) throws IOException {
        return new RoutingRule(client, path,
                client.put(path + "/active", request).as(RoutingRuleData.class), isDeleted());
    }

    /** Moves the rule to a new priority. */
    public RoutingRule reorder(int priority) throws IOException {
        return reorder(new ReorderRoutingRuleRequest(priority));
    }

    public RoutingRule reorder(Consumer<ReorderRoutingRuleRequest.Builder> configure) throws IOException {
        ReorderRoutingRuleRequest.Builder builder = ReorderRoutingRuleRequest.builder();
        configure.accept(builder);
        return reorder(builder.build());
    }

    public RoutingRule reorder(ReorderRoutingRuleRequest request) throws IOException {
        return new RoutingRule(client, path,
                client.put(path + "/priority", request).as(RoutingRuleData.class), isDeleted());
    }
}
