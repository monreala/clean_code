package com.carhire.importing;

import com.carhire.model.Booking;
import com.carhire.model.Customer;
import com.carhire.model.Vehicle;

import java.util.List;
import java.util.Map;

public interface BookingImportService {
    List<Booking> importAndConfirmBookings(String filePath,
                                           Map<String, Customer> customersById,
                                           Map<String, Vehicle> vehiclesByRegNumber);
}