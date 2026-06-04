package com.carhire.importing;

import com.carhire.model.Customer;
import com.carhire.repository.CustomerRepository;
import com.carhire.service.reader.CustomerReader;
import com.carhire.service.validation.CustomerValidator;

import java.util.ArrayList;
import java.util.List;

public class CustomerImportServiceImpl implements CustomerImportService {

    private final CustomerReader customerReader;
    private final CustomerValidator customerValidator;
    private final CustomerRepository customerRepository;

    public CustomerImportServiceImpl(CustomerReader customerReader,
                                     CustomerValidator customerValidator,
                                     CustomerRepository customerRepository) {
        this.customerReader = customerReader;
        this.customerValidator = customerValidator;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> importValidCustomer(String filePath) {
        List<Customer> rawCustomers = customerReader.readCustomers(filePath);
        List<Customer> imported = new ArrayList<>();
        for (Customer customer : rawCustomers) {
            if (customerValidator.isValid(customer)) {
                customerRepository.save(customer);
                imported.add(customer);
            } else {
                System.out.println("Клиент отклонен валидатором: " + customer.getFullName());
            }
        }
        System.out.println("импортировано клиентов: " + imported.size());
        return imported;
    }
}