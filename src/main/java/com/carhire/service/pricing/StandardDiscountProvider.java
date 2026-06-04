package com.carhire.service.pricing;

import com.carhire.model.Customer;

public class StandardDiscountProvider implements DiscountProvider {

    static final int LONG_RENT_DAYS = 7;
    static final int VERY_LONG_RENT_DAYS = 14;
    static final double LONG_RENT_DISCOUNT_PERCENT = 5.0;
    static final double VERY_LONG_RENT_DISCOUNT_PERCENT = 10.0;

    static final int EXPERIENCED_DRIVER_YEARS = 10;
    static final int VETERAN_DRIVER_YEARS = 20;
    static final double EXPERIENCED_DRIVER_DISCOUNT_PERCENT = 3.0;
    static final double VETERAN_DRIVER_DISCOUNT_PERCENT = 7.0;

    static final double MAX_DISCOUNT_PERCENT = 20.0;

    @Override
    public double getDiscountPercentage(Customer customer, int rentDays) {
        double rentDiscount = discountByRentLength(rentDays);
        double loyaltyDiscount = discountByExperience(customer);
        return capDiscount(rentDiscount + loyaltyDiscount);
    }

    private double discountByRentLength(int rentDays) {
        if (rentDays >= VERY_LONG_RENT_DAYS) {
            return VERY_LONG_RENT_DISCOUNT_PERCENT;
        }
        if (rentDays >= LONG_RENT_DAYS) {
            return LONG_RENT_DISCOUNT_PERCENT;
        }
        return 0.0;
    }

    private double discountByExperience(Customer customer) {
        if (customer == null) {
            return 0.0;
        }
        int years = customer.getDrivingExperienceYears();
        if (years >= VETERAN_DRIVER_YEARS) {
            return VETERAN_DRIVER_DISCOUNT_PERCENT;
        }
        if (years >= EXPERIENCED_DRIVER_YEARS) {
            return EXPERIENCED_DRIVER_DISCOUNT_PERCENT;
        }
        return 0.0;
    }

    private double capDiscount(double percent) {
        return Math.min(percent, MAX_DISCOUNT_PERCENT);
    }
}