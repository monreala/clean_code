package com.carhire.model;

public class Vehicle {
    private final String regNumber;
    private final String brand;
    private final String model;
    private final VehicleCategory category;
    private final double baseDailyRate;
    private int mileageSinceLastService;

    public Vehicle(String regNumber, String brand, String model, VehicleCategory category,
                   double baseDailyRate, int mileageSinceLastService) {
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

    public void updateMileageSinceLastService(int mileage) {
        if (mileage < 0) {
            throw new IllegalArgumentException("Mileage cannot be negative");
        }
        this.mileageSinceLastService = mileage;
    }

    public void resetMileageAfterService() {
        this.mileageSinceLastService = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle other)) return false;
        return regNumber != null && regNumber.equals(other.regNumber);
    }

    @Override
    public int hashCode() {
        return regNumber == null ? 0 : regNumber.hashCode();
    }
}