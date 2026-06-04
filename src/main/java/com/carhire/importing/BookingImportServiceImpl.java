package com.carhire.importing;

import com.carhire.model.Booking;
import com.carhire.model.Customer;
import com.carhire.model.Vehicle;
import com.carhire.service.booking.BookingService;
import com.carhire.service.reader.BookingDraftReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookingImportServiceImpl implements BookingImportService {

    private final BookingDraftReader bookingDraftReader;
    private final BookingService bookingService;

    public BookingImportServiceImpl(BookingDraftReader bookingDraftReader, BookingService bookingService) {
        this.bookingDraftReader = bookingDraftReader;
        this.bookingService = bookingService;
    }

    @Override
    public List<Booking> importAndConfirmBookings(String filePath,
                                                  Map<String, Customer> customersById,
                                                  Map<String, Vehicle> vehiclesByRegNumber) {
        List<BookingDraft> drafts = bookingDraftReader.readBookingDrafts(filePath);
        List<Booking> confirmed = new ArrayList<>();
        for (BookingDraft draft : drafts) {
            Booking booking = tryConfirm(draft, customersById, vehiclesByRegNumber);
            if (booking != null) {
                confirmed.add(booking);
            }
        }
        System.out.println("подтверждено бронирований: " + confirmed.size());
        return confirmed;
    }

    private Booking tryConfirm(BookingDraft draft,
                               Map<String, Customer> customersById,
                               Map<String, Vehicle> vehiclesByRegNumber) {
        Customer customer = customersById.get(draft.getCustomerId());
        Vehicle vehicle = vehiclesByRegNumber.get(draft.getVehicleRegNumber());
        if (customer == null || vehicle == null) {
            System.out.println("Бронирование " + draft.getBookingId()
                    + " отклонено: клиент или автомобиль не найдены среди импортированных");
            return null;
        }
        Booking booking = new Booking(draft.getBookingId(), customer, vehicle, draft.getRentDays());
        try {
            bookingService.confirmBooking(booking);
            return booking;
        } catch (IllegalStateException e) {
            System.out.println("Бронирование " + draft.getBookingId() + " отклонено: " + e.getMessage());
            return null;
        }
    }
}