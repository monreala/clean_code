package com.carhire.service.booking;

import com.carhire.model.Booking;
import com.carhire.repository.BookingRepository;
import com.carhire.service.validation.BookingValidator;

public class BookingServiceImpl implements BookingService {

    private final BookingValidator bookingValidator;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingValidator bookingValidator, BookingRepository bookingRepository) {
        this.bookingValidator = bookingValidator;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void confirmBooking(Booking booking) {
        if (!bookingValidator.isValid(booking)) {
            throw new IllegalStateException("Booking failed validation");
        }
        booking.confirm();
        bookingRepository.save(booking);
    }
}