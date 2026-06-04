package com.carhire.service.pricing;
import com.carhire.model.Booking;
import com.carhire.model.VehicleCategory;
import com.carhire.service.validation.BookingValidator;
import com.carhire.service.validation.CustomerValidator;
import com.carhire.model.Customer;

public class PriceCalculatorImpl implements PriceCalculator {
    private final CustomerValidator customerValidator;
    private final BookingValidator bookingValidator;
    private final DiscountProvider discountProvider;
    private final static double YOUNG_DRIVER_FEE_MULTIPLIER = 1.20;

    public PriceCalculatorImpl(BookingValidator bookingValidator, CustomerValidator customerValidator, DiscountProvider discountProvider) {
        this.customerValidator = customerValidator;
        this.bookingValidator = bookingValidator;
        this.discountProvider = discountProvider;
    }

    @Override
    public double calculateTotalPrice(Booking booking) {
        validateBookingBeforeCalculation(booking);
        double baseCost = calculateBaseCost(booking);
        double costWithCategory = applyCategoryMultiplier(baseCost,booking.getVehicle().getCategory());
        double costWithRisk = applyYoungDriverFee(costWithCategory,booking.getCustomer());
        return applyDiscount(costWithRisk,booking);
    }

    private void validateBookingBeforeCalculation(Booking booking) {
        if(!bookingValidator.isValid(booking)) {
            throw new IllegalStateException("Cannot calculate price because booking is invalid");
        }
    }

    private double calculateBaseCost(Booking booking) {
        return booking.getVehicle().getBaseDailyRate()*booking.getRentDays();
    }

    private double applyCategoryMultiplier(double currentCost, VehicleCategory category) {
        double multiplier = switch (category){
            case PREMIUM -> 2.0;
            case ECONOMY -> 1.0;
            case STANDARD -> 1.2;
            case SUV -> 1.5;
        };
        return currentCost * multiplier;
    }

    private double applyYoungDriverFee(double currentCost, Customer customer) {
        if(customerValidator.isYoungOrInexperienced(customer)) {
            return currentCost * YOUNG_DRIVER_FEE_MULTIPLIER;
        }
        return currentCost;
    }

    private double applyDiscount(double currentCost, Booking booking) {
        double discountPrecent= discountProvider.getDiscountPercentage(booking.getCustomer(),booking.getRentDays());
        return currentCost - (currentCost*(discountPrecent/100.0));
    }
}
