package com.carhire.repository;

import com.carhire.model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlVehicleRepository implements VehicleRepository {

    @Override
    public void save(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (reg_number, brand, model, category, base_daily_rate, mileage_since_last_service) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE brand = ?, model = ?, category = ?, base_daily_rate = ?, mileage_since_last_service = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicle.getRegNumber());
            stmt.setString(2, vehicle.getBrand());
            stmt.setString(3, vehicle.getModel());
            stmt.setString(4, vehicle.getCategory().name());
            stmt.setDouble(5, vehicle.getBaseDailyRate());
            stmt.setInt(6, vehicle.getMileageSinceLastService());

            stmt.setString(7, vehicle.getBrand());
            stmt.setString(8, vehicle.getModel());
            stmt.setString(9, vehicle.getCategory().name());
            stmt.setDouble(10, vehicle.getBaseDailyRate());
            stmt.setInt(11, vehicle.getMileageSinceLastService());

            stmt.executeUpdate();
            System.out.println("Успешно сохранено в БД: " + vehicle.getRegNumber());

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении автомобиля в базу данных: " + e.getMessage(), e);
        }
    }
}