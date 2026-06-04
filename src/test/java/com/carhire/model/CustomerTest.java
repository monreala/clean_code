package com.carhire.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CustomerTest {

    @Test
    void equals_SameId_AreEqual() {
        Customer a = new Customer("1", "Alice", 30, 5);
        Customer b = new Customer("1", "Bob", 50, 20);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_DifferentId_AreNotEqual() {
        Customer a = new Customer("1", "Alice", 30, 5);
        Customer b = new Customer("2", "Alice", 30, 5);
        assertNotEquals(a, b);
    }
}