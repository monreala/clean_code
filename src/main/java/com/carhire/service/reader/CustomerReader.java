package com.carhire.service.reader;

import com.carhire.model.Customer;
import java.util.List;

public interface CustomerReader {

    List<Customer> readCustomers(String filePath);
}