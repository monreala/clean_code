package com.carhire.service.validation;

import com.carhire.model.Customer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerValidatorImplTest {

    private final CustomerValidator validator = new CustomerValidatorImpl();



    @Test
    void isValid_PerfectCustomer_ReturnsTrue() {
        Customer customer = new Customer("1", "John Doe", 30, 5);
        assertTrue(validator.isValid(customer));
    }

    @Test
    void isValid_NullCustomer_ReturnsFalse() {
        assertFalse(validator.isValid(null));
    }

    @Test
    void isValid_UnderageCustomer_ReturnsFalse() {
        Customer underage = new Customer("2", "John", 17, 1);
        assertFalse(validator.isValid(underage));
    }

    @Test
    void isValid_NegativeExperience_ReturnsFalse() {
        Customer badExperience = new Customer("3", "John", 30, -1);
        assertFalse(validator.isValid(badExperience));
    }

    @Test
    void isValid_EmptyName_ReturnsFalse() {
        Customer emptyName = new Customer("4", "   ", 30, 5);
        assertFalse(validator.isValid(emptyName));
    }

    @Test
    void isValid_NullName_ReturnsFalse() {
        Customer nullName = new Customer("5", null, 30, 5);
        assertFalse(validator.isValid(nullName));
    }



    @Test
    void isYoungOrInexperienced_AgeUnder25_ReturnsTrue() {
        Customer young = new Customer("6", "Alex", 20, 5);
        assertTrue(validator.isYoungOrInexperienced(young));
    }

    @Test
    void isYoungOrInexperienced_ExperienceUnder3_ReturnsTrue() {
        Customer inexperienced = new Customer("7", "Bob", 30, 1);
        assertTrue(validator.isYoungOrInexperienced(inexperienced));
    }

    @Test
    void isYoungOrInexperienced_AdultAndExperienced_ReturnsFalse() {
        Customer perfect = new Customer("8", "Alice", 28, 6);
        assertFalse(validator.isYoungOrInexperienced(perfect));
    }
}