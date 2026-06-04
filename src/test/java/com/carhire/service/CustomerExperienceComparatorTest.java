package com.carhire.service;

import com.carhire.model.Customer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerExperienceComparatorTest {

    private final CustomerExperienceComparator comparator = new CustomerExperienceComparator();

    @Test
    void compare_FirstHasMoreExperience_ReturnsNegative() {
        Customer more = new Customer("1", "A", 30, 10);
        Customer less = new Customer("2", "B", 30, 5);
        assertTrue(comparator.compare(more, less) < 0);
    }

    @Test
    void compare_SecondHasMoreExperience_ReturnsPositive() {
        Customer less = new Customer("1", "A", 30, 2);
        Customer more = new Customer("2", "B", 30, 9);
        assertTrue(comparator.compare(less, more) > 0);
    }

    @Test
    void compare_EqualExperience_ReturnsZero() {
        Customer a = new Customer("1", "A", 30, 7);
        Customer b = new Customer("2", "B", 30, 7);
        assertEquals(0, comparator.compare(a, b));
    }

    @Test
    void sort_OrdersCustomersByExperienceDescending() {
        List<Customer> list = new ArrayList<>();
        list.add(new Customer("1", "A", 30, 1));
        list.add(new Customer("2", "B", 30, 10));
        list.add(new Customer("3", "C", 30, 5));

        list.sort(comparator);

        assertEquals(10, list.get(0).getDrivingExperienceYears());
        assertEquals(5, list.get(1).getDrivingExperienceYears());
        assertEquals(1, list.get(2).getDrivingExperienceYears());
    }
}