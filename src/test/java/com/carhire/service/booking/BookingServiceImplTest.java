package com.carhire.service.booking;

import com.carhire.model.Booking;
import com.carhire.model.BookingStatus;
import com.carhire.model.Customer;
import com.carhire.model.Vehicle;
import com.carhire.model.VehicleCategory;
import com.carhire.repository.BookingRepository;
import com.carhire.service.validation.BookingValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingValidator bookingValidatorMock;

    @Mock
    private BookingRepository bookingRepositoryMock;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void confirmBooking_ValidBooking_ChangesStatusToConfirmedAndSaves() {
        Booking booking = new Booking("B1",
                new Customer("1", "A", 30, 5),
                new Vehicle("V1", "A", "B", VehicleCategory.ECONOMY, 100, 0), 5);

        assertEquals(BookingStatus.PENDING, booking.getStatus());
        Mockito.when(bookingValidatorMock.isValid(booking)).thenReturn(true);

        bookingService.confirmBooking(booking);

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        Mockito.verify(bookingRepositoryMock).save(booking);
    }

    @Test
    void confirmBooking_InvalidBooking_ThrowsExceptionAndDoesNotSave() {
        Booking invalidBooking = new Booking("B2", null, null, 0);
        Mockito.when(bookingValidatorMock.isValid(invalidBooking)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> bookingService.confirmBooking(invalidBooking));
        Mockito.verifyNoInteractions(bookingRepositoryMock);
    }
}