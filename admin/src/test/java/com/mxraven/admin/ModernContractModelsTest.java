package com.mxraven.admin;

import com.mxraven.admin.model.EffectiveTenantQuotas;
import com.mxraven.admin.model.InboundRoutingOutcome;
import com.mxraven.admin.model.InboundRoutingOutcomeDisposition;
import com.mxraven.admin.model.ListenerData;
import com.mxraven.admin.model.ListenerType;
import com.mxraven.admin.model.SimulationFixture;
import com.mxraven.admin.model.TenantQuotaUsage;
import com.mxraven.admin.model.TenantSuppressionData;
import com.mxraven.admin.model.WebhookEndpointData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModernContractModelsTest {
    private final AdminClient client = new AdminClient("http://localhost:1", "token");

    @Test
    void mapsTheListenerScriptFields() throws Exception {
        ListenerData listener = client.json().readValue(
                "{\"id\":\"l1\",\"tenant_id\":\"t1\",\"display_name\":\"Inbound\","
                        + "\"listener_type\":\"mta\",\"stream_type\":\"transactional\","
                        + "\"rspamd_scanning_enabled\":true,"
                        + "\"active_script_revision_id\":\"rev-1\",\"script_body_access\":true}",
                ListenerData.class);
        assertEquals(ListenerType.MTA, listener.listenerType());
        assertEquals("rev-1", listener.activeScriptRevisionId());
        assertTrue(listener.scriptBodyAccess());
    }

    @Test
    void mapsTheSuppressionCanonicalIdentity() throws Exception {
        TenantSuppressionData suppression = client.json().readValue(
                "{\"tenant_id\":\"t1\",\"email_address\":\"User+tag@example.com\","
                        + "\"canonical_email_address\":\"user@example.com\",\"reason\":\"bounce\","
                        + "\"system_managed\":true,\"source\":\"one_click_unsubscribe\","
                        + "\"feedback_count\":4,\"added_at\":\"2026-01-01T00:00:00Z\","
                        + "\"last_feedback_at\":\"2026-01-02T00:00:00Z\","
                        + "\"updated_at\":\"2026-01-02T00:00:00Z\"}",
                TenantSuppressionData.class);
        assertEquals("user@example.com", suppression.canonicalEmailAddress());
        assertTrue(suppression.systemManaged());
        assertEquals(4, suppression.feedbackCount());
    }

    @Test
    void mapsTheRenamedQuotas() throws Exception {
        EffectiveTenantQuotas effective = client.json().readValue(
                "{\"profile_id\":\"p1\",\"profile_ref\":\"default\","
                        + "\"profile_display_name\":\"Default\",\"profile_is_active\":true,"
                        + "\"max_script_source_bytes\":131072,"
                        + "\"max_script_revisions_per_listener\":20,\"max_http_endpoints\":10}",
                EffectiveTenantQuotas.class);
        assertEquals(131072, effective.maxScriptSourceBytes());
        assertEquals(20, effective.maxScriptRevisionsPerListener());
        assertEquals(10, effective.maxHttpEndpoints());

        TenantQuotaUsage usage = client.json().readValue(
                "{\"submission_listeners\":1,\"mta_listeners\":2,\"domains\":3,"
                        + "\"inbound_routes\":4,\"api_keys\":5,\"script_source_bytes\":4096,"
                        + "\"smtp_relays\":6,\"storage_integrations\":7,\"http_endpoints\":8,"
                        + "\"auto_reply_templates\":9,\"identity_providers\":10,"
                        + "\"manual_tenant_suppressions\":11,\"recipient_sets\":12,"
                        + "\"recipient_set_members\":13,\"dedicated_ip_leases\":14,"
                        + "\"dedicated_ip_addresses\":15}",
                TenantQuotaUsage.class);
        assertEquals(4096, usage.scriptSourceBytes());
        assertEquals(8, usage.httpEndpoints());
    }

    @Test
    void mapsTheSimulationContentAndCredentialHeader() throws Exception {
        SimulationFixture fixture = client.json().readValue(
                "{\"mail_from\":\"sender@example.com\",\"body_text\":\"plain\","
                        + "\"body_html\":\"<a href=\\\"https://x\\\">x</a>\","
                        + "\"attachments\":[{\"filename\":\"a.pdf\","
                        + "\"content_type\":\"application/pdf\",\"size_bytes\":12,\"inline\":false}]}",
                SimulationFixture.class);
        assertEquals(1, fixture.attachments().size());
        assertEquals("a.pdf", fixture.attachments().get(0).filename());

        WebhookEndpointData endpoint = client.json().readValue(
                "{\"id\":\"e1\",\"tenant_id\":\"t1\",\"webhook_ref\":\"risk\","
                        + "\"display_name\":\"Risk\",\"target_url\":\"https://risk.example/check\","
                        + "\"signing_kid\":\"whk_1\",\"has_signing_secret\":true,"
                        + "\"credential_header\":\"X-API-Key\",\"is_active\":true,"
                        + "\"created_at\":\"2026-01-01T00:00:00Z\","
                        + "\"updated_at\":\"2026-01-01T00:00:00Z\"}",
                WebhookEndpointData.class);
        assertEquals("X-API-Key", endpoint.credentialHeader());
    }

    @Test
    void mapsTheInboundRoutingOutcomeRevision() throws Exception {
        InboundRoutingOutcome outcome = client.json().readValue(
                "{\"id\":\"o1\",\"listener_id\":\"l1\",\"message_ref\":\"m1\","
                        + "\"recipient_ordinal\":0,\"recipient_domain\":\"example.com\","
                        + "\"script_revision_id\":\"rev-1\",\"used_listener_default\":false,"
                        + "\"terminal_action\":\"REJECT\",\"disposition\":\"smtp_rejected\","
                        + "\"smtp_status_code\":550,\"enhanced_status_code\":\"5.7.1\","
                        + "\"audit_reason\":\"dmarc\",\"created_at\":\"2026-01-01T00:00:00Z\"}",
                InboundRoutingOutcome.class);
        assertEquals("rev-1", outcome.scriptRevisionId());
        assertEquals(InboundRoutingOutcomeDisposition.SMTP_REJECTED, outcome.disposition());
        assertFalse(outcome.usedListenerDefault());
    }
}
