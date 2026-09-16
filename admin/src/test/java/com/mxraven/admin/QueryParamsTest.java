package com.mxraven.admin;

import com.mxraven.admin.internal.Java8;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QueryParamsTest {
    @Test
    void pageSizeMustBeWithinTheContractRange() {
        assertThrows(IllegalArgumentException.class, () -> QueryParams.create().pageSize(0));
        assertThrows(IllegalArgumentException.class, () -> QueryParams.create().pageSize(501));
        assertDoesNotThrow(() -> QueryParams.create().pageSize(1));
        assertDoesNotThrow(() -> QueryParams.create().pageSize(500));
    }

    @Test
    void searchOmitsBlankAndRejectsShortValues() {
        assertEquals(Java8.map(), QueryParams.create().q(null).toMap());
        assertEquals(Java8.map(), QueryParams.create().q("").toMap());
        assertEquals(Java8.map(), QueryParams.create().q("   ").toMap());
        assertThrows(IllegalArgumentException.class, () -> QueryParams.create().q("ra"));
        assertEquals(Java8.map("q", "rav"), QueryParams.create().q("rav").toMap());
    }
}
