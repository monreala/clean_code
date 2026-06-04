package com.carhire.service.reader;

import com.carhire.importing.BookingDraft;

import java.util.ArrayList;
import java.util.List;

public class BookingDraftCsvReader implements BookingDraftReader {

    private static final String ROW_TYPE = "BOOKING";
    private static final int EXPECTED_FIELDS = 5;

    @Override
    public List<BookingDraft> readBookingDrafts(String filePath) {
        List<BookingDraft> drafts = new ArrayList<>();
        for (String[] row : CsvRowReader.readRowsOfType(filePath, ROW_TYPE)) {
            BookingDraft draft = parseRow(row);
            if (draft != null) {
                drafts.add(draft);
            }
        }
        return drafts;
    }

    private BookingDraft parseRow(String[] fields) {
        if (fields.length < EXPECTED_FIELDS) {
            System.err.println("Пропущена некорректная строка BOOKING (мало полей)");
            return null;
        }
        try {
            String bookingId = fields[1].trim();
            String customerId = fields[2].trim();
            String vehicleRegNumber = fields[3].trim();
            int rentDays = Integer.parseInt(fields[4].trim());
            return new BookingDraft(bookingId, customerId, vehicleRegNumber, rentDays);
        } catch (NumberFormatException e) {
            System.err.println("Пропущена некорректная строка BOOKING: " + String.join(";", fields));
            return null;
        }
    }
}