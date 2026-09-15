package com.mxraven.admin;

import com.mxraven.admin.model.MTARateLimitPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MTARateLimitPolicyTest {
    @Test
    void rejectsNonPositiveValues() {
        assertThrows(IllegalArgumentException.class, () -> new MTARateLimitPolicy(0, 1, 1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new MTARateLimitPolicy(1, 1, 1, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> new MTARateLimitPolicy(1, 1, 1, 1, 0));
    }

    @Test
    void enforcesTightening() {
        MTARateLimitPolicy inherited = new MTARateLimitPolicy(60, 600, 600, 50, 10);
        assertDoesNotThrow(() -> inherited.validateStricterThan(inherited));
        assertDoesNotThrow(() -> new MTARateLimitPolicy(30, 300, 300, 25, 5).validateStricterThan(inherited));
        assertThrows(IllegalArgumentException.class,
                () -> new MTARateLimitPolicy(61, 600, 600, 50, 10).validateStricterThan(inherited));
        assertThrows(IllegalArgumentException.class,
                () -> new MTARateLimitPolicy(60, 601, 600, 50, 10).validateStricterThan(inherited));
        assertThrows(IllegalArgumentException.class,
                () -> new MTARateLimitPolicy(60, 600, 600, 51, 10).validateStricterThan(inherited));
        assertThrows(IllegalArgumentException.class,
                () -> new MTARateLimitPolicy(60, 600, 600, 50, 11).validateStricterThan(inherited));
    }
}
