package com.carhire.model;

public class Customer {
    private final String id;
    private final String fullName;
    private final int age;
    private final int drivingExperienceYears;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}