package com.carhire.service.reader;

import com.carhire.importing.BookingDraft;

import java.util.List;

public interface BookingDraftReader {
    List<BookingDraft> readBookingDrafts(String filePath);
}