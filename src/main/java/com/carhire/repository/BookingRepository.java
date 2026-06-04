package com.carhire.repository;

import com.carhire.model.Booking;

public interface BookingRepository {
    void save(Booking booking);
}