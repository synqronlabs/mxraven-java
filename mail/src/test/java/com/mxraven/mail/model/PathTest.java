package com.mxraven.mail.model;

import com.mxraven.mail.internal.Java8;

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
        assertEquals(Java8.list(), path.sourceRoutes());
    }

    @Test
    void nullPathAlwaysRendersTheEmptyReversePath() {
        Path path = Path.nullPath();
        assertTrue(path.isNull());
        assertEquals("<>", path.toString());
    }

    @Test
    void keepsSourceRoutes() {
        Path path = new Path(MailboxAddress.of("a@b.com"), Java8.list("route.example"));
        assertEquals(Java8.list("route.example"), path.sourceRoutes());
    }

    @Test
    void defaultsNullComponents() {
        Path path = new Path(null, null);
        assertTrue(path.isNull());
        assertEquals(Java8.list(), path.sourceRoutes());
    }
}
