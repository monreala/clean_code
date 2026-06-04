package com.carhire.service.validation;

import com.carhire.model.Booking;
import com.carhire.model.Customer;
import com.carhire.model.Vehicle;
import com.carhire.model.VehicleCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingValidatorImplTest {

    @Mock
    private CustomerValidator customerValidatorMock;

    @Mock
    private VehicleValidator vehicleValidatorMock;

    @InjectMocks
    private BookingValidatorImpl bookingValidator;


    private final Customer validCustomer = new Customer("1", "Alice", 30, 8);
    private final Vehicle standardVehicle = new Vehicle("A1", "Ford", "Focus", VehicleCategory.STANDARD, 100, 0);


    @Test
    void isValid_NullBooking_ReturnsFalse() {
        assertFalse(bookingValidator.isValid(null));
    }

    @Test
    void isValid_ZeroRentDays_ReturnsFalse() {
        Booking zeroDaysBooking = new Booking("B1", validCustomer, standardVehicle, 0);
        assertFalse(bookingValidator.isValid(zeroDaysBooking));
    }

    @Test
    void isValid_NegativeRentDays_ReturnsFalse() {
        Booking negativeDaysBooking = new Booking("B2", validCustomer, standardVehicle, -5);
        assertFalse(bookingValidator.isValid(negativeDaysBooking));
    }


    @Test
    void isValid_CustomerIsInvalid_ReturnsFalse() {
        Booking booking = new Booking("B3", validCustomer, standardVehicle, 5);
        Mockito.when(customerValidatorMock.isValid(validCustomer)).thenReturn(false);
        assertFalse(bookingValidator.isValid(booking));
    }

    @Test
    void isValid_VehicleIsInvalid_ReturnsFalse() {
        Booking booking = new Booking("B4", validCustomer, standardVehicle, 5);

        Mockito.when(customerValidatorMock.isValid(validCustomer)).thenReturn(true);
        Mockito.when(vehicleValidatorMock.isValid(standardVehicle)).thenReturn(false);

        assertFalse(bookingValidator.isValid(booking));
    }

    @Test
    void isValid_VehicleNeedsMaintenance_ReturnsFalse() {
        Booking booking = new Booking("B5", validCustomer, standardVehicle, 5);

        Mockito.when(customerValidatorMock.isValid(validCustomer)).thenReturn(true);
        Mockito.when(vehicleValidatorMock.isValid(standardVehicle)).thenReturn(true);
        Mockito.when(vehicleValidatorMock.needsMaintenance(standardVehicle)).thenReturn(true);

        assertFalse(bookingValidator.isValid(booking));
    }

    @Test
    void isValid_PremiumCar_CustomerMeetsRequirements_ReturnsTrue() {
        Vehicle premiumVehicle = new Vehicle("P1", "BMW", "X5", VehicleCategory.PREMIUM, 200, 0);

        Booking booking = new Booking("B6", validCustomer, premiumVehicle, 5);

        Mockito.when(customerValidatorMock.isValid(validCustomer)).thenReturn(true);
        Mockito.when(vehicleValidatorMock.isValid(premiumVehicle)).thenReturn(true);
        Mockito.when(vehicleValidatorMock.needsMaintenance(premiumVehicle)).thenReturn(false);

        assertTrue(bookingValidator.isValid(booking));
    }

    @Test
    void isValid_PremiumCar_CustomerTooYoung_ReturnsFalse() {
        Customer youngCustomer = new Customer("2", "Bob", 24, 6);
        Vehicle premiumVehicle = new Vehicle("P2", "Audi", "A8", VehicleCategory.PREMIUM, 200, 0);
        Booking booking = new Booking("B7", youngCustomer, premiumVehicle, 5);

        Mockito.when(customerValidatorMock.isValid(youngCustomer)).thenReturn(true);
        Mockito.when(vehicleValidatorMock.isValid(premiumVehicle)).thenReturn(true);
        Mockito.when(vehicleValidatorMock.needsMaintenance(premiumVehicle)).thenReturn(false);

        assertFalse(bookingValidator.isValid(booking));
    }

    @Test
    void isValid_PremiumCar_CustomerNotExperienced_ReturnsFalse() {
        Customer inexperiencedCustomer = new Customer("3", "Charlie", 30, 4);
        Vehicle premiumVehicle = new Vehicle("P3", "Mercedes", "S-Class", VehicleCategory.PREMIUM, 200, 0);
        Booking booking = new Booking("B8", inexperiencedCustomer, premiumVehicle, 5);

        Mockito.when(customerValidatorMock.isValid(inexperiencedCustomer)).thenReturn(true);
        Mockito.when(vehicleValidatorMock.isValid(premiumVehicle)).thenReturn(true);
        Mockito.when(vehicleValidatorMock.needsMaintenance(premiumVehicle)).thenReturn(false);

        assertFalse(bookingValidator.isValid(booking));
    }
}