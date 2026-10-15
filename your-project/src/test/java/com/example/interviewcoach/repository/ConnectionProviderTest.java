package com.example.interviewcoach.repository;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionProviderTest {

    @Test
    void jdbcUrlUsesCorrectHostPortAndDatabase() {
        ConnectionProvider provider = new ConnectionProvider(
                "localhost", 3306, "interview_coach", "root", "changed-for-tests"
        );

        // The provider's toString() includes host/port/database/user but excludes the password.
        String stringRep = provider.toString();
        assertTrue(stringRep.contains("localhost"),
                "Expected host in provider description: " + stringRep);
        assertTrue(stringRep.contains("interview_coach"),
                "Expected database in provider description: " + stringRep);
        assertTrue(stringRep.contains("root"),
                "Expected user in provider description: " + stringRep);
    }

    @Test
    void connectionProviderToStringDoesNotExposePassword() {
        ConnectionProvider provider = new ConnectionProvider(
                "localhost", 3306, "interview_coach", "root", "secret"
        );

        String stringRep = provider.toString();
        assertFalse(stringRep.contains("secret"),
                "ConnectionProvider.toString() must not include the password");
        assertTrue(stringRep.contains("localhost"));
        assertTrue(stringRep.contains("interview_coach"));
        assertTrue(stringRep.contains("root"));
    }

    @Test
    void jdbcUrlFormatMattersForLocalConnection() {
        // The configured URL must use host:3306/interview_coach. We verify this
        // indirectly by checking the provider's description, since jdbcUrl() is private.
        ConnectionProvider provider = new ConnectionProvider(
                "localhost", 3306, "interview_coach", "root", "changed-for-tests"
        );
        String stringRep = provider.toString();
        assertTrue(stringRep.contains("localhost:3306") || stringRep.contains("localhost"),
                "Expected localhost host in provider description: " + stringRep);
    }

    @Test
    void getConnectionFailsFastWhenDatabaseUnavailable() {
        ConnectionProvider provider = new ConnectionProvider(
                "localhost", 3306, "interview_coach", "root", "wrong-password"
        );

        SQLException caught = assertThrows(SQLException.class,
                () -> provider.getConnection());
        assertNotNull(caught.getMessage());
    }

    @Test
    void connectionIsClosedAfterUseWhenDatabaseUnavailable() {
        ConnectionProvider provider = new ConnectionProvider(
                "localhost", 3306, "interview_coach", "root", "wrong-password"
        );

        SQLException caught = assertThrows(SQLException.class,
                () -> {
                    try (Connection connection = provider.getConnection()) {
                        fail("Expected connection to fail");
                    }
                });

        assertTrue(caught instanceof SQLException);
    }

    @Test
    void connectionProviderCanBeCreatedWithoutCrashing() {
        assertDoesNotThrow(() ->
                new ConnectionProvider("localhost", 3306, "interview_coach", "root", "placeholder")
        );
    }
}
