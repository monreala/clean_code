package com.carhire.model;

public class Booking {
    private final String bookingId;
    private final Customer customer;
    private final Vehicle vehicle;
    private final int rentDays;
    private BookingStatus status;

    public Booking(String bookingId, Customer customer, Vehicle vehicle, int rentDays) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.rentDays = rentDays;
        this.status = BookingStatus.PENDING;
    }

    public String getBookingId() {
        return bookingId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public int getRentDays() {
        return rentDays;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void confirm() {
        requireStatus(BookingStatus.PENDING, BookingStatus.CONFIRMED);
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed booking");
        }
        this.status = BookingStatus.CANCELLED;
    }

    public void complete() {
        requireStatus(BookingStatus.CONFIRMED, BookingStatus.COMPLETED);
        this.status = BookingStatus.COMPLETED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking other)) return false;
        return bookingId != null && bookingId.equals(other.bookingId);
    }

    @Override
    public int hashCode() {
        return bookingId == null ? 0 : bookingId.hashCode();
    }

    private void requireStatus(BookingStatus expected, BookingStatus target) {
        if (status != expected) {
            throw new IllegalStateException(
                    "Cannot transition booking from " + status + " to " + target + "; expected " + expected);
        }
    }
}