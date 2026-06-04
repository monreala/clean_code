package com.carhire.service.pricing;

import com.carhire.model.Customer;

public interface DiscountProvider {
    double getDiscountPercentage(Customer customer, int rentDays);
}
