package com.carhire.importing;

import com.carhire.model.Customer;
import com.carhire.repository.CustomerRepository;
import com.carhire.service.reader.CustomerReader;
import com.carhire.service.validation.CustomerValidator;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerImportServiceImplTest {

    @Mock
    private CustomerReader readerMock;

    @Mock
    private CustomerValidator validatorMock;

    @Mock
    private CustomerRepository repositoryMock;

    @InjectMocks
    private CustomerImportServiceImpl service;

    @Test
    void importValidCustomer_AllValid_SavesAllAndReturnsAll() {
        Customer a = new Customer("1", "Alice", 30, 5);
        Customer b = new Customer("2", "Bob", 25, 3);
        when(readerMock.readCustomers("file.csv")).thenReturn(List.of(a, b));
        when(validatorMock.isValid(a)).thenReturn(true);
        when(validatorMock.isValid(b)).thenReturn(true);

        List<Customer> result = service.importValidCustomer("file.csv");

        assertEquals(List.of(a, b), result);
        verify(repositoryMock).save(a);
        verify(repositoryMock).save(b);
    }

    @Test
    void importValidCustomer_SomeInvalid_OnlyValidGetSaved() {
        Customer good = new Customer("1", "Alice", 30, 5);
        Customer bad = new Customer("2", "Bob", 15, 0);
        when(readerMock.readCustomers("file.csv")).thenReturn(List.of(good, bad));
        when(validatorMock.isValid(good)).thenReturn(true);
        when(validatorMock.isValid(bad)).thenReturn(false);

        List<Customer> result = service.importValidCustomer("file.csv");

        assertEquals(List.of(good), result);
        verify(repositoryMock).save(good);
        verify(repositoryMock, never()).save(bad);
    }

    @Test
    void importValidCustomer_EmptyFile_ReturnsEmptyAndSavesNothing() {
        when(readerMock.readCustomers("file.csv")).thenReturn(Collections.emptyList());

        List<Customer> result = service.importValidCustomer("file.csv");

        assertTrue(result.isEmpty());
        verify(repositoryMock, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void importValidCustomer_AllInvalid_SavesNone() {
        Customer bad = new Customer("2", "Bob", 15, 0);
        when(readerMock.readCustomers("file.csv")).thenReturn(List.of(bad));
        when(validatorMock.isValid(bad)).thenReturn(false);

        List<Customer> result = service.importValidCustomer("file.csv");

        assertTrue(result.isEmpty());
        verify(repositoryMock, times(0)).save(bad);
    }
}