package com.carhire.service.reader;

import com.carhire.model.Vehicle;
import com.carhire.model.VehicleCategory;

import java.util.ArrayList;
import java.util.List;

public class VehicleCsvReader implements VehicleReader {

    private static final String ROW_TYPE = "VEHICLE";
    private static final int EXPECTED_FIELDS = 7;

    @Override
    public List<Vehicle> readVehicles(String filePath) {
        List<Vehicle> vehicles = new ArrayList<>();
        for (String[] row : CsvRowReader.readRowsOfType(filePath, ROW_TYPE)) {
            Vehicle vehicle = parseRow(row);
            if (vehicle != null) {
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }

    private Vehicle parseRow(String[] fields) {
        if (fields.length < EXPECTED_FIELDS) {
            System.err.println("Пропущена некорректная строка VEHICLE (мало полей)");
            return null;
        }
        try {
            String regNumber = fields[1].trim();
            String brand = fields[2].trim();
            String model = fields[3].trim();
            VehicleCategory category = VehicleCategory.valueOf(fields[4].trim());
            double baseDailyRate = Double.parseDouble(fields[5].trim());
            int mileage = Integer.parseInt(fields[6].trim());
            return new Vehicle(regNumber, brand, model, category, baseDailyRate, mileage);
        } catch (NumberFormatException | IllegalArgumentException e) {
            System.err.println("Пропущена некорректная строка VEHICLE: " + String.join(";", fields));
            return null;
        }
    }
}