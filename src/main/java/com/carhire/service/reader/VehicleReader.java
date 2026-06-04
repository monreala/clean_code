package com.carhire.service.reader;

import com.carhire.model.Vehicle;

import java.util.List;

public interface VehicleReader {
    List<Vehicle> readVehicles(String filePath);
}