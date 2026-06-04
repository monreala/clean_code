package com.carhire;

import com.carhire.model.Customer;
import com.carhire.repository.CustomerRepository;
import com.carhire.repository.MySqlCustomerRepository;
import com.carhire.importing.CustomerImportService;
import com.carhire.importing.CustomerImportServiceImpl;
import com.carhire.service.reader.CustomerCsvReader;
import com.carhire.service.reader.CustomerReader;
import com.carhire.service.validation.CustomerValidator;
import com.carhire.service.validation.CustomerValidatorImpl;
import com.carhire.service.CustomerExperienceComparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        CustomerReader reader = new CustomerCsvReader();
        CustomerValidator validator = new CustomerValidatorImpl();
        CustomerRepository repository = new MySqlCustomerRepository();

        CustomerImportService importService = new CustomerImportServiceImpl(reader, validator, repository);
        String filePath = "customers.csv";
        importService.importValidCustomer(filePath);



        List<Customer> rawCustomers = reader.readCustomers(filePath);

        List<Customer> validCustomers = rawCustomers.stream()
                .filter(validator::isValid)
                .toList();

        System.out.println("До сортировки:");
        printCustomers(validCustomers);

        Collections.sort(validCustomers, new CustomerExperienceComparator());

        System.out.println("\nПосле сортировки (по убыванию стажа):");
        printCustomers(validCustomers);
    }

    private static void printCustomers(List<Customer> customers) {
        for (Customer c : customers) {
            System.out.println(c.getFullName() + " | Возраст: " + c.getAge() + " | Стаж: " + c.getDrivingExperienceYears());
        }
    }
}