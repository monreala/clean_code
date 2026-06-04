package com.carhire.model;

public class Vehicle {
    private String regNumber;
    private String brand;
    private String model;
    private VehicleCategory category;
    private double baseDailyRate;
    private int mileageSinceLastService;

    public Vehicle(String regNumber, String brand, String model, VehicleCategory category, double baseDailyRate, int mileageSinceLastService) {
        this.regNumber = regNumber;
        this.brand = brand;
        this.model = model;
        this.category = category;
        this.baseDailyRate = baseDailyRate;
        this.mileageSinceLastService = mileageSinceLastService;
    }

    public String getRegNumber() {
        return regNumber;
    }
    public String getBrand() {
        return brand;
    }
    public String getModel() {
        return model;
    }
    public VehicleCategory getCategory() {
        return category;
    }
    public double getBaseDailyRate() {
        return baseDailyRate;
    }
    public int getMileageSinceLastService() {
        return mileageSinceLastService;
    }


    public void setMileageSinceLastService(int mileageSinceLastService) {
        this.mileageSinceLastService = mileageSinceLastService;
    }
}