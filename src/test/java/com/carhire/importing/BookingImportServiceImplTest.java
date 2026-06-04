package com.carhire.importing;

import com.carhire.model.Booking;
import com.carhire.model.Customer;
import com.carhire.model.Vehicle;
import com.carhire.model.VehicleCategory;
import com.carhire.service.booking.BookingService;
import com.carhire.service.reader.BookingDraftReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingImportServiceImplTest {

    @Mock
    private BookingDraftReader readerMock;

    @Mock
    private BookingService bookingServiceMock;

    @InjectMocks
    private BookingImportServiceImpl service;

    private Customer customer;
    private Vehicle vehicle;
    private Map<String, Customer> customersById;
    private Map<String, Vehicle> vehiclesByRegNumber;

    @BeforeEach
    void setUp() {
        customer = new Customer("C1", "Alice", 30, 5);
        vehicle = new Vehicle("V1", "Ford", "Focus", VehicleCategory.STANDARD, 100, 0);
        customersById = Map.of("C1", customer);
        vehiclesByRegNumber = Map.of("V1", vehicle);
    }

    @Test
    void importAndConfirm_ResolvableAndValid_ConfirmsAndReturnsBooking() {
        BookingDraft draft = new BookingDraft("B1", "C1", "V1", 5);
        when(readerMock.readBookingDrafts("file.csv")).thenReturn(List.of(draft));

        List<Booking> result = service.importAndConfirmBookings("file.csv", customersById, vehiclesByRegNumber);

        assertEquals(1, result.size());
        Booking booking = result.get(0);
        assertEquals("B1", booking.getBookingId());
        assertEquals(customer, booking.getCustomer());
        assertEquals(vehicle, booking.getVehicle());
        verify(bookingServiceMock).confirmBooking(booking);
    }

    @Test
    void importAndConfirm_UnknownCustomer_Skipped() {
        BookingDraft draft = new BookingDraft("B1", "MISSING", "V1", 5);
        when(readerMock.readBookingDrafts("file.csv")).thenReturn(List.of(draft));

        List<Booking> result = service.importAndConfirmBookings("file.csv", customersById, vehiclesByRegNumber);

        assertTrue(result.isEmpty());
        verify(bookingServiceMock, never()).confirmBooking(any());
    }

    @Test
    void importAndConfirm_UnknownVehicle_Skipped() {
        BookingDraft draft = new BookingDraft("B1", "C1", "MISSING", 5);
        when(readerMock.readBookingDrafts("file.csv")).thenReturn(List.of(draft));

        List<Booking> result = service.importAndConfirmBookings("file.csv", customersById, vehiclesByRegNumber);

        assertTrue(result.isEmpty());
        verify(bookingServiceMock, never()).confirmBooking(any());
    }

    @Test
    void importAndConfirm_ConfirmFails_BookingNotInResult() {
        BookingDraft draft = new BookingDraft("B1", "C1", "V1", 5);
        when(readerMock.readBookingDrafts("file.csv")).thenReturn(List.of(draft));
        doThrow(new IllegalStateException("invalid")).when(bookingServiceMock).confirmBooking(any());

        List<Booking> result = service.importAndConfirmBookings("file.csv", customersById, vehiclesByRegNumber);

        assertTrue(result.isEmpty());
    }

    @Test
    void importAndConfirm_MixedValidAndInvalid_OnlyValidReturned() {
        BookingDraft good = new BookingDraft("B1", "C1", "V1", 5);
        BookingDraft missingCustomer = new BookingDraft("B2", "X", "V1", 5);
        when(readerMock.readBookingDrafts("file.csv")).thenReturn(List.of(good, missingCustomer));

        List<Booking> result = service.importAndConfirmBookings("file.csv", customersById, vehiclesByRegNumber);

        assertEquals(1, result.size());
        assertEquals("B1", result.get(0).getBookingId());
    }
}