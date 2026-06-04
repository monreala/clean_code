package com.carhire.repository;

import com.carhire.model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlBookingRepository implements BookingRepository {

    @Override
    public void save(Booking booking) {
        String sql = "INSERT INTO bookings (booking_id, customer_id, vehicle_reg_number, rent_days, status) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE customer_id = ?, vehicle_reg_number = ?, rent_days = ?, status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, booking.getBookingId());
            stmt.setString(2, booking.getCustomer().getId());
            stmt.setString(3, booking.getVehicle().getRegNumber());
            stmt.setInt(4, booking.getRentDays());
            stmt.setString(5, booking.getStatus().name());

            stmt.setString(6, booking.getCustomer().getId());
            stmt.setString(7, booking.getVehicle().getRegNumber());
            stmt.setInt(8, booking.getRentDays());
            stmt.setString(9, booking.getStatus().name());

            stmt.executeUpdate();
            System.out.println("Успешно сохранено в БД: " + booking.getBookingId());

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении бронирования в базу данных: " + e.getMessage(), e);
        }
    }
}