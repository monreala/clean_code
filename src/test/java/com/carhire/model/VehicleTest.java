package com.carhire.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VehicleTest {

    @Test
    void updateMileage_PositiveValue_Updates() {
        Vehicle vehicle = new Vehicle("V1", "Ford", "Focus", VehicleCategory.STANDARD, 100, 1000);
        vehicle.updateMileageSinceLastService(2500);
        assertEquals(2500, vehicle.getMileageSinceLastService());
    }

    @Test
    void updateMileage_Negative_Throws() {
        Vehicle vehicle = new Vehicle("V1", "Ford", "Focus", VehicleCategory.STANDARD, 100, 1000);
        assertThrows(IllegalArgumentException.class, () -> vehicle.updateMileageSinceLastService(-1));
    }

    @Test
    void resetMileageAfterService_SetsToZero() {
        Vehicle vehicle = new Vehicle("V1", "Ford", "Focus", VehicleCategory.STANDARD, 100, 20000);
        vehicle.resetMileageAfterService();
        assertEquals(0, vehicle.getMileageSinceLastService());
    }

    @Test
    void equals_SameRegNumber_AreEqual() {
        Vehicle a = new Vehicle("V1", "Ford", "Focus", VehicleCategory.STANDARD, 100, 0);
        Vehicle b = new Vehicle("V1", "BMW", "M3", VehicleCategory.PREMIUM, 500, 100);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_DifferentRegNumber_AreNotEqual() {
        Vehicle a = new Vehicle("V1", "Ford", "Focus", VehicleCategory.STANDARD, 100, 0);
        Vehicle b = new Vehicle("V2", "Ford", "Focus", VehicleCategory.STANDARD, 100, 0);
        assertNotEquals(a, b);
    }
}