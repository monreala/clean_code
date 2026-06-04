package com.carhire.repository;

import com.carhire.model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlCustomerRepository implements CustomerRepository {

    @Override
    public void save(Customer customer) {
        String sql = "INSERT INTO customers (id, full_name, age, driving_experience) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE full_name = ?, age = ?, driving_experience = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getId());
            stmt.setString(2, customer.getFullName());
            stmt.setInt(3, customer.getAge());
            stmt.setInt(4, customer.getDrivingExperienceYears());

            stmt.setString(5, customer.getFullName());
            stmt.setInt(6, customer.getAge());
            stmt.setInt(7, customer.getDrivingExperienceYears());

            stmt.executeUpdate();
            System.out.println("Успешно сохранено в БД: " + customer.getFullName());

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении клиента в базу данных: " + e.getMessage(), e);
        }
    }
}