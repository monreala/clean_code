package com.carhire.repository;

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

class MySqlVehicleRepositoryTest {

    private final MySqlVehicleRepository repository = new MySqlVehicleRepository();

    @Test
    void save_BindsParametersAndExecutesUpdate() throws SQLException {
        Vehicle vehicle = new Vehicle("ABC123", "Ford", "Focus", VehicleCategory.STANDARD, 99.5, 1000);
        Connection connection = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(stmt);

        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(connection);

            repository.save(vehicle);
        }

        verify(stmt).setString(1, "ABC123");
        verify(stmt).setString(2, "Ford");
        verify(stmt).setString(3, "Focus");
        verify(stmt).setString(4, "STANDARD");
        verify(stmt).setDouble(5, 99.5);
        verify(stmt).setInt(6, 1000);
        verify(stmt).setString(7, "Ford");
        verify(stmt).setString(8, "Focus");
        verify(stmt).setString(9, "STANDARD");
        verify(stmt).setDouble(10, 99.5);
        verify(stmt).setInt(11, 1000);
        verify(stmt).executeUpdate();
        verify(stmt).close();
        verify(connection).close();
    }

    @Test
    void save_SqlException_WrappedInRuntimeException() throws SQLException {
        Vehicle vehicle = new Vehicle("ABC123", "Ford", "Focus", VehicleCategory.ECONOMY, 50.0, 0);
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("boom"));

        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(connection);

            RuntimeException ex = assertThrows(RuntimeException.class, () -> repository.save(vehicle));
            assertEquals(SQLException.class, ex.getCause().getClass());
        }
    }
}