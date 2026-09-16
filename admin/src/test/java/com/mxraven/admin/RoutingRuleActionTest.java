package com.mxraven.admin;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.mxraven.admin.model.ModifyHeaderOperation;
import com.mxraven.admin.model.RoutingRuleAction;
import com.mxraven.admin.model.RoutingRuleActionKind;
import com.mxraven.admin.model.RoutingRulePayload;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoutingRuleActionTest {
    private final ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    @Test
    void serializesTheDiscriminatedUnion() throws Exception {
        JsonNode relay = mapper.readTree(mapper.writeValueAsString(RoutingRuleAction.relay("primary")));
        assertEquals("RELAY", relay.get("action_type").asText());
        assertEquals("primary", relay.get("action_payload").get("relay_ref").asText());

        JsonNode headers = mapper.readTree(mapper.writeValueAsString(RoutingRuleAction.modifyHeader(Java8.list(
                ModifyHeaderOperation.append("X-Tag", "v1"),
                ModifyHeaderOperation.remove("X-Old")))));
        assertEquals("MODIFY_HEADER", headers.get("action_type").asText());
        JsonNode operations = headers.get("action_payload").get("operations");
        assertEquals("append", operations.get(0).get("op").asText());
        assertEquals("v1", operations.get(0).get("value").asText());
        assertEquals("remove", operations.get(1).get("op").asText());
        assertEquals(false, operations.get(1).has("value"));
    }

    @Test
    void roundTripsThroughJackson() throws Exception {
        RoutingRuleAction action = RoutingRuleAction.smtpForward("billing-dest");
        RoutingRuleAction parsed = mapper.readValue(mapper.writeValueAsString(action), RoutingRuleAction.class);
        assertEquals(RoutingRuleActionKind.SMTP_FORWARD, parsed.actionType());
        assertEquals("billing-dest", parsed.actionPayload().destinationRef());
    }

    @Test
    void builderDerivesAndChecksTheKind() {
        RoutingRuleAction action = RoutingRuleAction.builder()
                .payload(RoutingRulePayload.relay("primary"))
                .build();
        assertEquals(RoutingRuleActionKind.RELAY, action.actionType());

        assertThrows(IllegalArgumentException.class, () -> RoutingRuleAction.builder()
                .actionType(RoutingRuleActionKind.DROP)
                .payload(RoutingRulePayload.relay("primary"))
                .build());
    }

    @Test
    void validatesPayloads() {
        assertThrows(IllegalArgumentException.class, () -> RoutingRuleAction.relay(" "));
        assertThrows(IllegalArgumentException.class, () -> RoutingRuleAction.smtpForward("bad ref"));
        assertThrows(IllegalArgumentException.class, () -> RoutingRuleAction.addRecipient(Java8.list("not-an-email")));
        assertThrows(IllegalArgumentException.class, () -> RoutingRuleAction.s3Store("dest", "/leading/", null));
        assertThrows(IllegalArgumentException.class, () -> RoutingRuleAction.s3Store("dest", null, null));
        assertThrows(IllegalArgumentException.class, () -> RoutingRuleAction.reject(450, "5.0.0", "nope"));
    }
}
