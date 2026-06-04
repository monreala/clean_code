package com.carhire.service.reader;

import com.carhire.model.Vehicle;
import com.carhire.model.VehicleCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VehicleCsvReaderTest {

    private final VehicleCsvReader reader = new VehicleCsvReader();

    @Test
    void readVehicles_ReturnsOnlyVehicleRows(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file,
                "CUSTOMER;1;Alice;30;5\n" +
                "VEHICLE;V1;Ford;Focus;STANDARD;80.0;1000\n" +
                "VEHICLE;V2;BMW;X5;PREMIUM;250.0;5000\n" +
                "BOOKING;B1;1;V1;5\n");

        List<Vehicle> result = reader.readVehicles(file.toString());

        assertEquals(2, result.size());

        Vehicle v1 = result.get(0);
        assertEquals("V1", v1.getRegNumber());
        assertEquals("Ford", v1.getBrand());
        assertEquals("Focus", v1.getModel());
        assertEquals(VehicleCategory.STANDARD, v1.getCategory());
        assertEquals(80.0, v1.getBaseDailyRate());
        assertEquals(1000, v1.getMileageSinceLastService());

        assertEquals(VehicleCategory.PREMIUM, result.get(1).getCategory());
    }

    @Test
    void readVehicles_NoVehicleRows_ReturnsEmpty(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file, "CUSTOMER;1;Alice;30;5\n");

        assertTrue(reader.readVehicles(file.toString()).isEmpty());
    }

    @Test
    void readVehicles_UnknownCategory_LineSkipped(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file,
                "VEHICLE;V1;Ford;Focus;FANCY;80.0;1000\n" +
                "VEHICLE;V2;BMW;X5;PREMIUM;250.0;5000\n");

        List<Vehicle> result = reader.readVehicles(file.toString());

        assertEquals(1, result.size());
        assertEquals("V2", result.get(0).getRegNumber());
    }

    @Test
    void readVehicles_MalformedNumbers_AreSkipped(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file,
                "VEHICLE;V1;Ford;Focus;STANDARD;not-a-number;1000\n" +
                "VEHICLE;V2;BMW;X5;PREMIUM;250.0;5000\n");

        List<Vehicle> result = reader.readVehicles(file.toString());

        assertEquals(1, result.size());
        assertEquals("V2", result.get(0).getRegNumber());
    }

    @Test
    void readVehicles_TooFewFields_LineSkipped(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file,
                "VEHICLE;V1;Ford\n" +
                "VEHICLE;V2;BMW;X5;PREMIUM;250.0;5000\n");

        List<Vehicle> result = reader.readVehicles(file.toString());

        assertEquals(1, result.size());
        assertEquals("V2", result.get(0).getRegNumber());
    }
}