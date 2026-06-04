package com.carhire.service.reader;

import com.carhire.model.Customer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerCsvReader implements CustomerReader {

    private static final String CSV_DELIMITER = ";";

    @Override
    public List<Customer> readCustomers(String filePath) {
        List<Customer> customers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                Customer customer = parseLineToCustomer(line);
                if (customer != null) {
                    customers.add(customer);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении CSV-файла: " + e.getMessage(), e);
        }

        return customers;
    }

    private Customer parseLineToCustomer(String line) {
        try {
            String[] data = line.split(CSV_DELIMITER);
            if (data.length < 4) {
                return null;
            }

            String id = data[0].trim();
            String fullName = data[1].trim();
            int age = Integer.parseInt(data[2].trim());
            int experience = Integer.parseInt(data[3].trim());

            return new Customer(id, fullName, age, experience);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Пропущена некорректная строка в CSV: " + line);
            return null;
        }
    }
}