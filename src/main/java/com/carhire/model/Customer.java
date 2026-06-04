package com.carhire.model;

public class Customer {
    private String id;
    private String fullName;
    private int age;
    private int drivingExperienceYears;

    public Customer(String id, String fullName, int age, int drivingExperienceYears) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.drivingExperienceYears = drivingExperienceYears;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public int getDrivingExperienceYears() {
        return drivingExperienceYears;
    }


}
