package com.carhire.service.validation;

import com.carhire.model.Vehicle;

public class VehicleValidatorImpl implements VehicleValidator {
    public static final int MAINTENANCE_THRESHOLD_KM = 15000;

    @Override
    public boolean isValid(Vehicle vehicle) {
        if (vehicle == null) {
            return false;
        }
        return isRateValid(vehicle.getBaseDailyRate()) && isRegNumberValid(vehicle.getRegNumber());
    }

    @Override
    public boolean needsMaintenance(Vehicle vehicle) {
        return vehicle.getMileageSinceLastService() >= MAINTENANCE_THRESHOLD_KM;
    }

    private boolean isRateValid(double rate) {
        return rate > 0;
    }

    private boolean isRegNumberValid(String regNumber) {
        return regNumber != null && !regNumber.trim().isEmpty();
    }
}
