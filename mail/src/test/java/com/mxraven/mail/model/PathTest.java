package com.mxraven.mail.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathTest {

    @Test
    void rendersAngleBrackets() {
        Path path = Path.of("alice@example.com");
        assertFalse(path.isNull());
        assertEquals("<alice@example.com>", path.toString());
        assertEquals(List.of(), path.sourceRoutes());
    }

    @Test
    void nullPathAlwaysRendersTheEmptyReversePath() {
        Path path = Path.nullPath();
        assertTrue(path.isNull());
        assertEquals("<>", path.toString());
    }

    @Test
    void keepsSourceRoutes() {
        Path path = new Path(MailboxAddress.of("a@b.com"), List.of("route.example"));
        assertEquals(List.of("route.example"), path.sourceRoutes());
    }

    @Test
    void defaultsNullComponents() {
        Path path = new Path(null, null);
        assertTrue(path.isNull());
        assertEquals(List.of(), path.sourceRoutes());
    }
}
