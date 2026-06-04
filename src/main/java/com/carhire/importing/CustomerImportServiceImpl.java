package com.carhire.importing;

import com.carhire.model.Customer;
import com.carhire.repository.CustomerRepository;
import com.carhire.service.reader.CustomerReader;
import com.carhire.service.validation.CustomerValidator;
import java.util.List;

public class CustomerImportServiceImpl implements CustomerImportService {

    private final CustomerReader customerReader;
    private final CustomerValidator customerValidator;
    private final CustomerRepository customerRepository;

    public CustomerImportServiceImpl(CustomerReader customerReader,CustomerValidator customerValidator, CustomerRepository customerRepository) {
        this.customerReader = customerReader;
        this.customerValidator = customerValidator;
        this.customerRepository = customerRepository;
    }



    @Override
    public void importValidCustomer(String filePath){
        List<Customer> rawCustomers = customerReader.readCustomers(filePath);
        int importedCount = 0;
        for(Customer customer : rawCustomers){
            if(customerValidator.isValid(customer)){
                customerRepository.save(customer);
                importedCount++;
            }else {
                System.out.println("Клиент отклонен валидатором: "+ customer.getFullName());
            }
        }
        System.out.println("импортировано клиентов: " + importedCount);
    }

}
