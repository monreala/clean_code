package com.carhire.service.booking;

import com.carhire.model.Booking;
import com.carhire.model.BookingStatus;
import com.carhire.service.validation.BookingValidator;

public class BookingServiceImpl implements BookingService {

    private final BookingValidator bookingValidator;

    public BookingServiceImpl(BookingValidator bookingValidator) {
        this.bookingValidator = bookingValidator;
    }

    @Override
    public void confirmBooking(Booking booking) {
        if (bookingValidator.isValid(booking)) {
            booking.setStatus(BookingStatus.CONFIRMED);
        } else {
            throw new IllegalStateException("Booking failed validation");
        }
    }
}