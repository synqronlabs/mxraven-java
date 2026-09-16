package com.mxraven.mail.webhook;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * Shared Jackson configuration for webhook payloads. Fields use
 * {@code snake_case} on the wire and unknown fields are ignored so the SDK
 * tolerates additive server changes.
 */
final class WebhookJson {
    static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .setDefaultPropertyInclusion(JsonInclude.Value.construct(
                    JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private WebhookJson() {
    }
}
