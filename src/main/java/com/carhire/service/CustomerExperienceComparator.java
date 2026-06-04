package com.carhire.service;

import com.carhire.model.Customer;

import java.util.Comparator;


public class CustomerExperienceComparator implements Comparator<Customer> {

    @Override
    public int compare(Customer c1, Customer c2) {
        return Integer.compare(c2.getDrivingExperienceYears(), c1.getDrivingExperienceYears());
    }
}