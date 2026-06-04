package com.carhire.service.validation;

import com.carhire.model.Customer;

public class CustomerValidatorImpl implements CustomerValidator {
    @Override
    public boolean isValid(Customer customer) {
        if (customer == null) {
            return false;
        }
        return isAgeValid(customer.getAge()) &&
                isExperiencedValid(customer.getDrivingExperienceYears()) &&
                isNameValid(customer.getFullName());
    }

    @Override
    public boolean isYoungOrInexperienced(Customer customer) {
        return customer.getAge() < 25 || customer.getDrivingExperienceYears() < 5;
    }

    private boolean isAgeValid(int age) {
        return age >= 18;
    }

    private boolean isExperiencedValid(int experienceYears) {
        return experienceYears >= 0;
    }

    private boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }
}


