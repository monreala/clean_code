package com.carhire.service.validation;

import com.carhire.model.Booking;
import com.carhire.model.Customer;
import com.carhire.model.Vehicle;
import com.carhire.model.VehicleCategory;

public class BookingValidatorImpl implements BookingValidator {
    public static final int PREMIUM_MIN_AGE = 25;
    public static final int PREMIUM_MIN_EXPERIENCE = 5;
    private final CustomerValidator customerValidator;
    private final VehicleValidator vehicleValidator;

    public BookingValidatorImpl(CustomerValidator customerValidator, VehicleValidator vehicleValidator) {
        this.customerValidator = customerValidator;
        this.vehicleValidator = vehicleValidator;
    }

    @Override
    public boolean isValid(Booking booking) {
        if (booking == null || booking.getRentDays() <= 0) {
            return false;
        }
        Customer customer = booking.getCustomer();
        Vehicle vehicle = booking.getVehicle();
        if (!customerValidator.isValid(customer) || !vehicleValidator.isValid(vehicle)) {
            return false;
        }
        if (vehicleValidator.needsMaintenance(vehicle)) {
            return false;
        }
        return isPremiumCarRulesSatisfied(customer, vehicle);
    }

    private boolean isPremiumCarRulesSatisfied(Customer customer, Vehicle vehicle) {
        if (vehicle.getCategory() == VehicleCategory.PREMIUM) {
            return customer.getAge() >= PREMIUM_MIN_AGE
                    && customer.getDrivingExperienceYears() >= PREMIUM_MIN_EXPERIENCE;
        }
        return true;
    }

}
