package com.carhire.service;
import com.carhire.model.Customer;
import java.util.Comparator;


public class CustomerExperienceComparator implements Comparator<Customer> {

    @Override
    public int compare(Customer c1, Customer c2) {
        // Чтобы сортировать по убыванию, вычитаем стаж первого из стажа второго
        // Если нужно по возрастанию: return c1.getDrivingExperienceYears() - c2.getDrivingExperienceYears();
        return c2.getDrivingExperienceYears() - c1.getDrivingExperienceYears();
    }
}