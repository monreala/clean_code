package com.carhire.service.validation;

import com.carhire.model.Vehicle;
import com.carhire.model.VehicleCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleValidatorImplTest {

    private final VehicleValidator validator = new VehicleValidatorImpl();


    @Test
    void isValid_PerfectVehicle_ReturnsTrue() {
        Vehicle vehicle = new Vehicle("A123", "Ford", "Focus", VehicleCategory.STANDARD, 100.0, 0);
        assertTrue(validator.isValid(vehicle));
    }

    @Test
    void isValid_NullVehicle_ReturnsFalse() {
        assertFalse(validator.isValid(null));
    }

    @Test
    void isValid_ZeroBaseRate_ReturnsFalse() {
        Vehicle freeVehicle = new Vehicle("A123", "Ford", "Focus", VehicleCategory.STANDARD, 0.0, 0);
        assertFalse(validator.isValid(freeVehicle));
    }

    @Test
    void isValid_EmptyRegNumber_ReturnsFalse() {
        Vehicle noNumber = new Vehicle("   ", "Ford", "Focus", VehicleCategory.STANDARD, 100.0, 0);
        assertFalse(validator.isValid(noNumber));
    }

    @Test
    void isValid_NullRegNumber_ReturnsFalse() {
        Vehicle nullNumber = new Vehicle(null, "Ford", "Focus", VehicleCategory.STANDARD, 100.0, 0);
        assertFalse(validator.isValid(nullNumber));
    }


    @Test
    void needsMaintenance_MileageExactlyThreshold_ReturnsTrue() {
        Vehicle car = new Vehicle("1", "A", "B", VehicleCategory.STANDARD, 100, 15000);
        assertTrue(validator.needsMaintenance(car));
    }

    @Test
    void needsMaintenance_MileageOverThreshold_ReturnsTrue() {
        Vehicle car = new Vehicle("1", "A", "B", VehicleCategory.STANDARD, 100, 20000);
        assertTrue(validator.needsMaintenance(car));
    }

    @Test
    void needsMaintenance_MileageBelowThreshold_ReturnsFalse() {
        Vehicle car = new Vehicle("1", "A", "B", VehicleCategory.STANDARD, 100, 14999);
        assertFalse(validator.needsMaintenance(car));
    }
}