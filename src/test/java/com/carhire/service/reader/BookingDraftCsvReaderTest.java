package com.carhire.service.reader;

import com.carhire.importing.BookingDraft;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingDraftCsvReaderTest {

    private final BookingDraftCsvReader reader = new BookingDraftCsvReader();

    @Test
    void readBookingDrafts_ReturnsOnlyBookingRows(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file,
                "CUSTOMER;1;Alice;30;5\n" +
                "VEHICLE;V1;Ford;Focus;STANDARD;80.0;1000\n" +
                "BOOKING;B1;1;V1;5\n" +
                "BOOKING;B2;1;V1;14\n");

        List<BookingDraft> result = reader.readBookingDrafts(file.toString());

        assertEquals(2, result.size());

        BookingDraft b1 = result.get(0);
        assertEquals("B1", b1.getBookingId());
        assertEquals("1", b1.getCustomerId());
        assertEquals("V1", b1.getVehicleRegNumber());
        assertEquals(5, b1.getRentDays());

        assertEquals(14, result.get(1).getRentDays());
    }

    @Test
    void readBookingDrafts_NoBookingRows_ReturnsEmpty(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file, "CUSTOMER;1;Alice;30;5\n");

        assertTrue(reader.readBookingDrafts(file.toString()).isEmpty());
    }

    @Test
    void readBookingDrafts_MalformedRentDays_AreSkipped(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file,
                "BOOKING;B1;1;V1;notnumber\n" +
                "BOOKING;B2;1;V1;7\n");

        List<BookingDraft> result = reader.readBookingDrafts(file.toString());

        assertEquals(1, result.size());
        assertEquals("B2", result.get(0).getBookingId());
    }

    @Test
    void readBookingDrafts_TooFewFields_LineSkipped(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.csv");
        Files.writeString(file,
                "BOOKING;B1;1\n" +
                "BOOKING;B2;1;V1;7\n");

        List<BookingDraft> result = reader.readBookingDrafts(file.toString());

        assertEquals(1, result.size());
        assertEquals("B2", result.get(0).getBookingId());
    }
}