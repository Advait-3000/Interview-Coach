package com.example.interviewcoach.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionProvider implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(ConnectionProvider.class);

    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;
    private final boolean logConnectionStatus;

    public ConnectionProvider(String host, int port, String database,
                              String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.logConnectionStatus = true;
    }

    public ConnectionProvider(String host, String port, String database,
                              String user, String password) {
        this(host, Integer.parseInt(port), database, user, password);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                jdbcUrl(),
                user,
                password);
    }

    private String jdbcUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/" + database
                + "?useSSL=false"
                + "&allowPublicKeyRetrieval=true"
                + "&serverTimezone=UTC"
                + "&characterEncoding=UTF-8"
                + "&driverMySQLCompatibility=8.0";
    }

    public void close() {
        // ConnectionProvider does not pool connections.
        // Applications should close individual connections, not this instance.
    }

    @Override
    public String toString() {
        return "ConnectionProvider{host=%s, port=%d, database=%s, user=%s}"
                .formatted(host, port, database, user);
    }
}
