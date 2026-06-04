package com.carhire.service.validation;

import com.carhire.model.Customer;

public interface CustomerValidator {
    boolean isValid(Customer customer);

    boolean isYoungOrInexperienced(Customer customer);
}
