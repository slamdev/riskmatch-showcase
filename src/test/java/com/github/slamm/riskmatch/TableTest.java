package com.github.slamm.riskmatch;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class TableTest {

    @Test
    public void testApp() {
        ITable table = new Table();
        table.set("A1", 2);
        table.set("B1000000000", 3);
        assertEquals(table.query("GET A1"), 2L);
        assertEquals(table.query("GET B1"), 0L);
        assertEquals(table.query("SUM C1 C1"), 0L);
        assertEquals(table.query("MULT C1 C1"), 1L); // see https://en.wikipedia.org/wiki/Empty_product
        assertEquals(table.query("SUM A1 A1"), 2L);
        assertEquals(table.query("SUM A1 B3"), 2L);
        assertEquals(table.query("SUM A1 B1000000000"), 5L);
        assertEquals(table.query("MULT B1000000000 A1"), 6L);
        assertEquals(table.query("MULT B100000000 B1000000000"), 3L);
        table.set("B2", 5);
        assertEquals(table.query("SUM A1 B1000000000"), 10L);
    }
}