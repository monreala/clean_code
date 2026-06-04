package com.carhire.repository;

import com.carhire.model.Customer;
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

class MySqlCustomerRepositoryTest {

    private final MySqlCustomerRepository repository = new MySqlCustomerRepository();

    @Test
    void save_BindsParametersAndExecutesUpdate() throws SQLException {
        Customer customer = new Customer("42", "Alice", 30, 5);
        Connection connection = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(stmt);

        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(connection);

            repository.save(customer);
        }

        verify(stmt).setString(1, "42");
        verify(stmt).setString(2, "Alice");
        verify(stmt).setInt(3, 30);
        verify(stmt).setInt(4, 5);
        verify(stmt).setString(5, "Alice");
        verify(stmt).setInt(6, 30);
        verify(stmt).setInt(7, 5);
        verify(stmt).executeUpdate();
        verify(stmt).close();
        verify(connection).close();
    }

    @Test
    void save_SqlException_WrappedInRuntimeException() throws SQLException {
        Customer customer = new Customer("42", "Alice", 30, 5);
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("boom"));

        try (var mocked = mockStatic(DatabaseConnection.class)) {
            mocked.when(DatabaseConnection::getConnection).thenReturn(connection);

            RuntimeException ex = assertThrows(RuntimeException.class, () -> repository.save(customer));
            assertEquals(SQLException.class, ex.getCause().getClass());
        }
    }
}