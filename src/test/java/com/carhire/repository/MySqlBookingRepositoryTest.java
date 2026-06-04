package com.carhire.repository;

import com.carhire.model.Booking;
import com.carhire.model.Customer;
import com.carhire.model.Vehicle;
import com.carhire.model.VehicleCategory;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MySqlBookingRepositoryTest {

    private final MySqlBookingRepository repository = new MySqlBookingRepository();

    private Booking sampleBooking() {
        Customer customer = new Customer("C1", "Alice", 30, 5);
        Vehicle vehicle = new Vehicle("V1", "Ford", "Focus", VehicleCategory.STANDARD, 100.0, 0);
        return new Booking("B1", customer, vehicle, 7);
    }

    @Test
    void save_BindsParametersAndExecutesUpdate() throws SQLException {
        Booking booking = sampleBooking();
        Connection connection = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(stmt);

        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(connection);

            repository.save(booking);
        }

        verify(stmt).setString(1, "B1");
        verify(stmt).setString(2, "C1");
        verify(stmt).setString(3, "V1");
        verify(stmt).setInt(4, 7);
        verify(stmt).setString(5, "PENDING");
        verify(stmt).setString(6, "C1");
        verify(stmt).setString(7, "V1");
        verify(stmt).setInt(8, 7);
        verify(stmt).setString(9, "PENDING");
        verify(stmt).executeUpdate();
        verify(stmt).close();
        verify(connection).close();
    }

    @Test
    void save_SqlException_WrappedInRuntimeException() throws SQLException {
        Booking booking = sampleBooking();
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("boom"));

        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(connection);

            RuntimeException ex = assertThrows(RuntimeException.class, () -> repository.save(booking));
            assertEquals(SQLException.class, ex.getCause().getClass());
        }
    }
}