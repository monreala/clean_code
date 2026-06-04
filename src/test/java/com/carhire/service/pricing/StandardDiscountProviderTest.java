package com.carhire.service.pricing;

import com.carhire.model.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardDiscountProviderTest {

    private final StandardDiscountProvider provider = new StandardDiscountProvider();

    private Customer customerWithExperience(int years) {
        return new Customer("1", "Test", 40, years);
    }

    @Test
    void shortRent_NewDriver_NoDiscount() {
        assertEquals(0.0, provider.getDiscountPercentage(customerWithExperience(2), 3));
    }

    @Test
    void rentBelowLongRentThreshold_NoRentDiscount() {
        assertEquals(0.0, provider.getDiscountPercentage(customerWithExperience(1), 6));
    }

    @Test
    void longRent_BelowVeryLongThreshold_GivesLongRentDiscount() {
        assertEquals(StandardDiscountProvider.LONG_RENT_DISCOUNT_PERCENT,
                provider.getDiscountPercentage(customerWithExperience(0), 7));
    }

    @Test
    void veryLongRent_GivesVeryLongRentDiscount() {
        assertEquals(StandardDiscountProvider.VERY_LONG_RENT_DISCOUNT_PERCENT,
                provider.getDiscountPercentage(customerWithExperience(0), 14));
    }

    @Test
    void experiencedDriver_GetsExperienceDiscountOnly() {
        assertEquals(StandardDiscountProvider.EXPERIENCED_DRIVER_DISCOUNT_PERCENT,
                provider.getDiscountPercentage(customerWithExperience(10), 1));
    }

    @Test
    void veteranDriver_GetsVeteranDiscountOnly() {
        assertEquals(StandardDiscountProvider.VETERAN_DRIVER_DISCOUNT_PERCENT,
                provider.getDiscountPercentage(customerWithExperience(20), 1));
    }

    @Test
    void veryLongRent_PlusVeteranDriver_SumsBothDiscounts() {
        double expected = StandardDiscountProvider.VERY_LONG_RENT_DISCOUNT_PERCENT
                + StandardDiscountProvider.VETERAN_DRIVER_DISCOUNT_PERCENT;
        assertEquals(expected, provider.getDiscountPercentage(customerWithExperience(20), 14));
    }

    @Test
    void totalDiscount_CappedAtMaximum() {
        Customer veteran = customerWithExperience(50);
        double result = provider.getDiscountPercentage(veteran, 365);
        assertEquals(StandardDiscountProvider.MAX_DISCOUNT_PERCENT, result);
    }

    @Test
    void nullCustomer_OnlyRentDiscountApplied() {
        assertEquals(StandardDiscountProvider.LONG_RENT_DISCOUNT_PERCENT,
                provider.getDiscountPercentage(null, 7));
    }

    @Test
    void nullCustomer_ShortRent_NoDiscount() {
        assertEquals(0.0, provider.getDiscountPercentage(null, 3));
    }
}