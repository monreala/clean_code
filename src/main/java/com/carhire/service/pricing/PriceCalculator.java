package com.carhire.service.pricing;

import com.carhire.model.Booking;

public interface PriceCalculator {
    double calculateTotalPrice(Booking booking);
}
