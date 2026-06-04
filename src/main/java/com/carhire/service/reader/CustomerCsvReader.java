package com.carhire.service.reader;

import com.carhire.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerCsvReader implements CustomerReader {

    private static final String ROW_TYPE = "CUSTOMER";
    private static final int EXPECTED_FIELDS = 5;

    @Override
    public List<Customer> readCustomers(String filePath) {
        List<Customer> customers = new ArrayList<>();
        for (String[] row : CsvRowReader.readRowsOfType(filePath, ROW_TYPE)) {
            Customer customer = parseRow(row);
            if (customer != null) {
                customers.add(customer);
            }
        }
        return customers;
    }

    private Customer parseRow(String[] fields) {
        if (fields.length < EXPECTED_FIELDS) {
            System.err.println("Пропущена некорректная строка CUSTOMER (мало полей)");
            return null;
        }
        try {
            String id = fields[1].trim();
            String fullName = fields[2].trim();
            int age = Integer.parseInt(fields[3].trim());
            int experience = Integer.parseInt(fields[4].trim());
            return new Customer(id, fullName, age, experience);
        } catch (NumberFormatException e) {
            System.err.println("Пропущена некорректная строка CUSTOMER: " + String.join(";", fields));
            return null;
        }
    }
}