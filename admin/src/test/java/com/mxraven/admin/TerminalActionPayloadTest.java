package com.mxraven.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxraven.admin.model.TerminalActionPayload;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TerminalActionPayloadTest {
    private static final String POOL_ID = "123e4567-e89b-12d3-a456-426614174000";

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void serializesActionSpecificShapes() throws Exception {
        assertEquals("{\"relay_ref\":\"primary\"}", mapper.writeValueAsString(TerminalActionPayload.relay("primary")));
        assertEquals("{\"template_ref\":\"welcome\"}",
                mapper.writeValueAsString(TerminalActionPayload.autoReply("welcome")));
        assertEquals("{\"pool_id\":\"" + POOL_ID + "\"}",
                mapper.writeValueAsString(TerminalActionPayload.deliverDedicated(POOL_ID)));
        assertEquals("{}", mapper.writeValueAsString(TerminalActionPayload.drop()));
        assertEquals("{\"audit_reason\":\"policy\"}",
                mapper.writeValueAsString(TerminalActionPayload.drop("policy")));
        assertEquals("{\"smtp_status_code\":550,\"enhanced_status_code\":\"5.7.1\",\"message\":\"blocked\"}",
                mapper.writeValueAsString(TerminalActionPayload.reject(550, "5.7.1", "blocked")));
    }

    @Test
    void roundTripsThroughJackson() throws Exception {
        TerminalActionPayload payload = TerminalActionPayload.reject(550, "5.7.1", "blocked", "policy");
        TerminalActionPayload parsed = mapper.readValue(mapper.writeValueAsString(payload), TerminalActionPayload.class);
        assertEquals(payload.values(), parsed.values());
        assertEquals(550, parsed.smtpStatusCode());
        assertEquals("policy", parsed.auditReason());
    }

    @Test
    void rejectsMalformedInputs() {
        assertThrows(IllegalArgumentException.class, () -> TerminalActionPayload.relay(" "));
        assertThrows(IllegalArgumentException.class, () -> TerminalActionPayload.deliverDedicated("not-a-uuid"));
        assertThrows(IllegalArgumentException.class, () -> TerminalActionPayload.reject(450, "5.7.1", "blocked"));
        assertThrows(IllegalArgumentException.class, () -> TerminalActionPayload.reject(550, "nope", "blocked"));
        assertThrows(IllegalArgumentException.class, () -> TerminalActionPayload.reject(550, "5.7.1", " "));
    }
}
