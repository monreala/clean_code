package com.carhire.model;

public class Booking {
    private String bookingId;
    private Customer customer;
    private Vehicle vehicle;
    private int rentDays;
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



    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
