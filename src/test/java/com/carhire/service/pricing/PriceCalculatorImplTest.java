package com.carhire.service.pricing;

import com.carhire.model.Booking;
import com.carhire.model.Customer;
import com.carhire.model.Vehicle;
import com.carhire.model.VehicleCategory;
import com.carhire.service.validation.BookingValidator;
import com.carhire.service.validation.CustomerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PriceCalculatorImplTest {

    @Mock
    private BookingValidator bookingValidatorMock;

    @Mock
    private CustomerValidator customerValidatorMock;

    @Mock
    private DiscountProvider discountProviderMock;

    @InjectMocks
    private PriceCalculatorImpl priceCalculator;

    private Customer defaultCustomer;

    @BeforeEach
    void setUp() {
        defaultCustomer = new Customer("1", "Alice", 30, 8);
    }


    @Test
    void calculateTotalCost_InvalidBooking_ThrowsException() {
        Booking invalidBooking = new Booking("B1", defaultCustomer, null, 5);

        Mockito.when(bookingValidatorMock.isValid(invalidBooking)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> {
            priceCalculator.calculateTotalPrice(invalidBooking);
        });
    }


    @Test
    void calculateTotalCost_EconomyCategory_MultiplierIs1() {
        Booking booking = createBookingWithCategory(VehicleCategory.ECONOMY);
        setupDefaultMocks(booking, false, 0.0);

        assertEquals(100.0, priceCalculator.calculateTotalPrice(booking));
    }

    @Test
    void calculateTotalCost_StandardCategory_MultiplierIs1_2() {
        Booking booking = createBookingWithCategory(VehicleCategory.STANDARD);
        setupDefaultMocks(booking, false, 0.0);

        assertEquals(120.0, priceCalculator.calculateTotalPrice(booking));
    }

    @Test
    void calculateTotalCost_SUVCategory_MultiplierIs1_5() {
        Booking booking = createBookingWithCategory(VehicleCategory.SUV);
        setupDefaultMocks(booking, false, 0.0);

        assertEquals(150.0, priceCalculator.calculateTotalPrice(booking));
    }

    @Test
    void calculateTotalCost_PremiumCategory_MultiplierIs2_0() {
        Booking booking = createBookingWithCategory(VehicleCategory.PREMIUM);
        setupDefaultMocks(booking, false, 0.0);

        assertEquals(200.0, priceCalculator.calculateTotalPrice(booking));
    }


    @Test
    void calculateTotalCost_YoungDriver_Adds20PercentFee() {
        Booking booking = createBookingWithCategory(VehicleCategory.ECONOMY);
        setupDefaultMocks(booking, true, 0.0);

        assertEquals(120.0, priceCalculator.calculateTotalPrice(booking));
    }

    @Test
    void calculateTotalCost_WithDiscount_SubtractsDiscountPercentage() {
        Booking booking = createBookingWithCategory(VehicleCategory.ECONOMY);
        setupDefaultMocks(booking, false, 15.0);

        assertEquals(85.0, priceCalculator.calculateTotalPrice(booking));
    }


    private Booking createBookingWithCategory(VehicleCategory category) {
        Vehicle vehicle = new Vehicle("V1", "Brand", "Model", category, 100.0, 0);
        return new Booking("B", defaultCustomer, vehicle, 1);
    }

    private void setupDefaultMocks(Booking booking, boolean isYoung, double discount) {
        Mockito.when(bookingValidatorMock.isValid(booking)).thenReturn(true);
        Mockito.when(customerValidatorMock.isYoungOrInexperienced(booking.getCustomer())).thenReturn(isYoung);
        Mockito.when(discountProviderMock.getDiscountPercentage(booking.getCustomer(), booking.getRentDays())).thenReturn(discount);
    }
}