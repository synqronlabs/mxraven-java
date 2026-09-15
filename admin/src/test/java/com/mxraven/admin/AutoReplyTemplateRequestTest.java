package com.mxraven.admin;

import com.mxraven.admin.model.AutoReplyTemplateHeader;
import com.mxraven.admin.model.CreateAutoReplyTemplateRequest;
import com.mxraven.admin.model.UpdateAutoReplyTemplateRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AutoReplyTemplateRequestTest {
    @Test
    void createRequiresEverySchemaRequiredField() {
        assertThrows(IllegalArgumentException.class, () -> CreateAutoReplyTemplateRequest.builder()
                .displayName("hello")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateAutoReplyTemplateRequest.builder()
                .templateRef("_bad")
                .displayName("hello")
                .fromAddress("noreply@example.com")
                .subject("Thanks")
                .textBody("Hi")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateAutoReplyTemplateRequest.builder()
                .templateRef("welcome")
                .displayName("hello")
                .fromAddress("noreply@example.com")
                .subject("Thanks")
                .build());
    }

    @Test
    void createRejectsLineBreaksAndAcceptsAValidBody() {
        assertThrows(IllegalArgumentException.class, () -> CreateAutoReplyTemplateRequest.builder()
                .templateRef("welcome")
                .displayName("hello")
                .fromAddress("noreply@example.com")
                .subject("line\nbreak")
                .textBody("Hi")
                .build());
        assertDoesNotThrow(() -> CreateAutoReplyTemplateRequest.builder()
                .templateRef("welcome")
                .displayName("hello")
                .fromAddress("noreply@example.com")
                .subject("Thanks")
                .textBody("Hi")
                .build());
    }

    @Test
    void updateRequiresHeadersAndABody() {
        assertThrows(IllegalArgumentException.class, () -> UpdateAutoReplyTemplateRequest.builder()
                .displayName("hello")
                .fromAddress("noreply@example.com")
                .subject("Thanks")
                .textBody("Hi")
                .build());
        assertThrows(IllegalArgumentException.class, () -> UpdateAutoReplyTemplateRequest.builder()
                .displayName("hello")
                .fromAddress("noreply@example.com")
                .subject("Thanks")
                .headers(List.of())
                .build());
        assertDoesNotThrow(() -> UpdateAutoReplyTemplateRequest.builder()
                .displayName("hello")
                .fromAddress("noreply@example.com")
                .subject("Thanks")
                .htmlBody("<p>Hi</p>")
                .headers(List.of())
                .build());
    }

    @Test
    void validatesHeaderNames() {
        assertThrows(IllegalArgumentException.class, () -> CreateAutoReplyTemplateRequest.builder()
                .templateRef("welcome")
                .displayName("hello")
                .fromAddress("noreply@example.com")
                .subject("Thanks")
                .textBody("Hi")
                .headers(List.of(new AutoReplyTemplateHeader("Bad:Name", "v")))
                .build());
        assertDoesNotThrow(() -> CreateAutoReplyTemplateRequest.builder()
                .templateRef("welcome")
                .displayName("hello")
                .fromAddress("noreply@example.com")
                .subject("Thanks")
                .textBody("Hi")
                .headers(List.of(new AutoReplyTemplateHeader("X-Tag", "v")))
                .build());
    }
}
