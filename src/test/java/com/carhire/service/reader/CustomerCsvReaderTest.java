package com.carhire.service.reader;

import com.carhire.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerCsvReaderTest {

    private final CustomerCsvReader reader = new CustomerCsvReader();

    @Test
    void readCustomers_ReturnsOnlyCustomerRows(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file,
                "CUSTOMER;1;Alice;30;5\n" +
                "CUSTOMER;2;Bob;22;1\n" +
                "VEHICLE;V1;Ford;Focus;STANDARD;100;0\n" +
                "BOOKING;B1;1;V1;5\n");

        List<Customer> result = reader.readCustomers(file.toString());

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Alice", result.get(0).getFullName());
        assertEquals(30, result.get(0).getAge());
        assertEquals(5, result.get(0).getDrivingExperienceYears());
        assertEquals("Bob", result.get(1).getFullName());
    }

    @Test
    void readCustomers_SkipsCommentsAndBlankLines(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file,
                "# header line describing format\n" +
                "\n" +
                "CUSTOMER;1;Alice;30;5\n" +
                "  \n" +
                "CUSTOMER;2;Bob;22;1\n");

        List<Customer> result = reader.readCustomers(file.toString());

        assertEquals(2, result.size());
    }

    @Test
    void readCustomers_NoCustomerRows_ReturnsEmpty(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file,
                "VEHICLE;V1;Ford;Focus;STANDARD;100;0\n" +
                "BOOKING;B1;1;V1;5\n");

        List<Customer> result = reader.readCustomers(file.toString());

        assertTrue(result.isEmpty());
    }

    @Test
    void readCustomers_MalformedLines_AreSkipped(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file,
                "CUSTOMER;1;Alice;30;5\n" +
                "CUSTOMER;2;Bob;abc;3\n" +
                "CUSTOMER;3;Carl\n" +
                "CUSTOMER;4;Dave;28;7\n");

        List<Customer> result = reader.readCustomers(file.toString());

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getFullName());
        assertEquals("Dave", result.get(1).getFullName());
    }

    @Test
    void readCustomers_TrimsWhitespace(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file, "CUSTOMER ;  1 ;  Alice  ; 30 ;5\n");

        List<Customer> result = reader.readCustomers(file.toString());

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Alice", result.get(0).getFullName());
    }

    @Test
    void readCustomers_NonExistentFile_ThrowsRuntimeException() {
        assertThrows(RuntimeException.class, () -> reader.readCustomers("does-not-exist.csv"));
    }
}