package com.carhire.service.validation;
import com.carhire.model.Vehicle;
public interface VehicleValidator {
    boolean isValid(Vehicle vehicle);
    boolean needsMaintenance(Vehicle vehicle);
}
