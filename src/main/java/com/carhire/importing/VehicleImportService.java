package com.carhire.importing;

import com.carhire.model.Vehicle;

import java.util.List;

public interface VehicleImportService {
    List<Vehicle> importValidVehicles(String filePath);
}