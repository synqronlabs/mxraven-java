package com.mxraven.admin;

import com.mxraven.admin.internal.Java8;

import com.mxraven.admin.model.CreateRecipientSetRequest;
import com.mxraven.admin.model.RecipientSetMemberRequest;
import com.mxraven.admin.model.RecipientSetMembersBatchRequest;
import com.mxraven.admin.model.UpdateRecipientSetRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecipientSetRequestTest {
    @Test
    void createRequiresAValidReference() {
        assertThrows(IllegalArgumentException.class, () -> CreateRecipientSetRequest.builder()
                .displayName("Named Set")
                .description("Open")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateRecipientSetRequest.builder()
                .setRef("_invalid")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateRecipientSetRequest.builder()
                .setRef("ref")
                .description(Java8.repeat("x", 4097))
                .build());
        assertDoesNotThrow(() -> CreateRecipientSetRequest.builder()
                .setRef("VIP_Customers-1")
                .displayName("Named Set")
                .description("Open")
                .build());
    }

    @Test
    void updateEnforcesMaximumLengthsButAllowsNull() {
        assertThrows(IllegalArgumentException.class, () -> UpdateRecipientSetRequest.builder()
                .displayName(Java8.repeat("x", 256))
                .build());
        assertDoesNotThrow(() -> UpdateRecipientSetRequest.builder()
                .displayName(null)
                .description(null)
                .build());
    }

    @Test
    void memberRequiresAValidEmail() {
        assertThrows(IllegalArgumentException.class, () -> RecipientSetMemberRequest.builder().build());
        assertThrows(IllegalArgumentException.class, () -> RecipientSetMemberRequest.builder()
                .emailAddress("not-an-email")
                .build());
        assertDoesNotThrow(() -> RecipientSetMemberRequest.builder()
                .emailAddress("person@example.com")
                .build());
    }

    @Test
    void batchEnforcesSizeAndMemberValidity() {
        assertThrows(IllegalArgumentException.class, () -> RecipientSetMembersBatchRequest.builder()
                .emailAddresses(Java8.list())
                .build());
        assertThrows(IllegalArgumentException.class, () -> RecipientSetMembersBatchRequest.builder()
                .emailAddresses(Java8.list("good@example.com", "bad"))
                .build());
        List<String> tooMany = new ArrayList<>();
        for (int index = 0; index <= 1000; index++) {
            tooMany.add("user" + index + "@example.com");
        }
        assertThrows(IllegalArgumentException.class, () -> RecipientSetMembersBatchRequest.builder()
                .emailAddresses(tooMany)
                .build());
    }
}
