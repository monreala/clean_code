package com.carhire.importing;

public class BookingDraft {
    private final String bookingId;
    private final String customerId;
    private final String vehicleRegNumber;
    private final int rentDays;

    public BookingDraft(String bookingId, String customerId, String vehicleRegNumber, int rentDays) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.vehicleRegNumber = vehicleRegNumber;
        this.rentDays = rentDays;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public int getRentDays() {
        return rentDays;
    }
}