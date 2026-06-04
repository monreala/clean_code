package com.carhire.importing;

import com.carhire.model.Vehicle;
import com.carhire.model.VehicleCategory;
import com.carhire.repository.VehicleRepository;
import com.carhire.service.reader.VehicleReader;
import com.carhire.service.validation.VehicleValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleImportServiceImplTest {

    @Mock
    private VehicleReader readerMock;

    @Mock
    private VehicleValidator validatorMock;

    @Mock
    private VehicleRepository repositoryMock;

    @InjectMocks
    private VehicleImportServiceImpl service;

    @Test
    void importValidVehicles_AllValid_SavesAllAndReturnsAll() {
        Vehicle v1 = new Vehicle("V1", "Ford", "Focus", VehicleCategory.STANDARD, 100, 0);
        Vehicle v2 = new Vehicle("V2", "BMW", "X5", VehicleCategory.PREMIUM, 250, 1000);
        when(readerMock.readVehicles("file.csv")).thenReturn(List.of(v1, v2));
        when(validatorMock.isValid(v1)).thenReturn(true);
        when(validatorMock.isValid(v2)).thenReturn(true);

        List<Vehicle> result = service.importValidVehicles("file.csv");

        assertEquals(List.of(v1, v2), result);
        verify(repositoryMock).save(v1);
        verify(repositoryMock).save(v2);
    }

    @Test
    void importValidVehicles_OneInvalid_OnlyValidGetSaved() {
        Vehicle good = new Vehicle("V1", "Ford", "Focus", VehicleCategory.STANDARD, 100, 0);
        Vehicle bad = new Vehicle("", "BMW", "X5", VehicleCategory.PREMIUM, 0, 0);
        when(readerMock.readVehicles("file.csv")).thenReturn(List.of(good, bad));
        when(validatorMock.isValid(good)).thenReturn(true);
        when(validatorMock.isValid(bad)).thenReturn(false);

        List<Vehicle> result = service.importValidVehicles("file.csv");

        assertEquals(List.of(good), result);
        verify(repositoryMock).save(good);
        verify(repositoryMock, never()).save(bad);
    }

    @Test
    void importValidVehicles_EmptyFile_ReturnsEmpty() {
        when(readerMock.readVehicles("file.csv")).thenReturn(Collections.emptyList());

        List<Vehicle> result = service.importValidVehicles("file.csv");

        assertTrue(result.isEmpty());
        verify(repositoryMock, never()).save(org.mockito.ArgumentMatchers.any());
    }
}