package com.carhire.importing;

import com.carhire.model.Vehicle;
import com.carhire.repository.VehicleRepository;
import com.carhire.service.reader.VehicleReader;
import com.carhire.service.validation.VehicleValidator;

import java.util.ArrayList;
import java.util.List;

public class VehicleImportServiceImpl implements VehicleImportService {

    private final VehicleReader vehicleReader;
    private final VehicleValidator vehicleValidator;
    private final VehicleRepository vehicleRepository;

    public VehicleImportServiceImpl(VehicleReader vehicleReader,
                                    VehicleValidator vehicleValidator,
                                    VehicleRepository vehicleRepository) {
        this.vehicleReader = vehicleReader;
        this.vehicleValidator = vehicleValidator;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> importValidVehicles(String filePath) {
        List<Vehicle> raw = vehicleReader.readVehicles(filePath);
        List<Vehicle> imported = new ArrayList<>();
        for (Vehicle vehicle : raw) {
            if (vehicleValidator.isValid(vehicle)) {
                vehicleRepository.save(vehicle);
                imported.add(vehicle);
            } else {
                System.out.println("Автомобиль отклонен валидатором: " + vehicle.getRegNumber());
            }
        }
        System.out.println("импортировано автомобилей: " + imported.size());
        return imported;
    }
}