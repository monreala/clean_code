package com.carhire.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingTest {

    private Booking newPendingBooking(String id) {
        Customer customer = new Customer("C1", "Alice", 30, 5);
        Vehicle vehicle = new Vehicle("V1", "Ford", "Focus", VehicleCategory.STANDARD, 100, 0);
        return new Booking(id, customer, vehicle, 5);
    }

    @Test
    void newBooking_StartsInPendingStatus() {
        assertEquals(BookingStatus.PENDING, newPendingBooking("B1").getStatus());
    }

    @Test
    void confirm_FromPending_MovesToConfirmed() {
        Booking booking = newPendingBooking("B1");
        booking.confirm();
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    void confirm_FromConfirmed_ThrowsIllegalState() {
        Booking booking = newPendingBooking("B1");
        booking.confirm();
        assertThrows(IllegalStateException.class, booking::confirm);
    }

    @Test
    void complete_FromConfirmed_MovesToCompleted() {
        Booking booking = newPendingBooking("B1");
        booking.confirm();
        booking.complete();
        assertEquals(BookingStatus.COMPLETED, booking.getStatus());
    }

    @Test
    void complete_FromPending_ThrowsIllegalState() {
        Booking booking = newPendingBooking("B1");
        assertThrows(IllegalStateException.class, booking::complete);
    }

    @Test
    void cancel_FromPending_MovesToCancelled() {
        Booking booking = newPendingBooking("B1");
        booking.cancel();
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    void cancel_FromConfirmed_MovesToCancelled() {
        Booking booking = newPendingBooking("B1");
        booking.confirm();
        booking.cancel();
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    void cancel_FromCompleted_ThrowsIllegalState() {
        Booking booking = newPendingBooking("B1");
        booking.confirm();
        booking.complete();
        assertThrows(IllegalStateException.class, booking::cancel);
    }

    @Test
    void equals_SameId_AreEqual() {
        assertEquals(newPendingBooking("B1"), newPendingBooking("B1"));
    }

    @Test
    void equals_DifferentId_AreNotEqual() {
        assertNotEquals(newPendingBooking("B1"), newPendingBooking("B2"));
    }

    @Test
    void hashCode_SameId_AreEqual() {
        assertEquals(newPendingBooking("B1").hashCode(), newPendingBooking("B1").hashCode());
    }
}