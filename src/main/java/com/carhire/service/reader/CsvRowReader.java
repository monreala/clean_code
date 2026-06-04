package com.carhire.service.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class CsvRowReader {

    static final String DELIMITER = ";";
    private static final String COMMENT_PREFIX = "#";

    private CsvRowReader() {
    }

    static List<String[]> readRowsOfType(String filePath, String type) {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (isSkippable(line)) {
                    continue;
                }
                String[] fields = line.split(DELIMITER);
                if (fields.length > 0 && type.equals(fields[0].trim())) {
                    rows.add(fields);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении CSV-файла: " + e.getMessage(), e);
        }
        return rows;
    }

    private static boolean isSkippable(String line) {
        String trimmed = line.trim();
        return trimmed.isEmpty() || trimmed.startsWith(COMMENT_PREFIX);
    }
}