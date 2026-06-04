package com.carhire.importing;

import com.carhire.model.Customer;

import java.util.List;

public interface CustomerImportService {
    List<Customer> importValidCustomer(String filePath);
}