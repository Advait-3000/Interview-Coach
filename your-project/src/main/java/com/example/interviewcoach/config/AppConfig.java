package com.example.interviewcoach.config;

import com.example.interviewcoach.repository.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);
    private static final String PROP_HOST = "DB_HOST";
    private static final String PROP_PORT = "DB_PORT";
    private static final String PROP_NAME = "DB_NAME";
    private static final String PROP_USER = "DB_USER";
    private static final String PROP_PASSWORD = "DB_PASSWORD";
    private static final String PROP_LOG_LEVEL = "APP_LOG_LEVEL";
    private static final String PROP_SEED = "APP_SEED_DEMO_DATA";

    private static AppConfig instance;
    private final Map<String, String> properties = new HashMap<>();
    private ConnectionProvider connectionProvider;

    private AppConfig() {
    }

    public static void load() {
        if (instance != null) {
            return;
        }
        instance = new AppConfig();
        instance.loadFromEnvironment();
        instance.applyLogging();
        instance.initConnectionProvider();
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AppConfig has not been loaded");
        }
        return instance;
    }

    public String get(String key, String defaultMaybe) {
        String value = readProperty(key);
        if (value == null || value.isBlank()) {
            return defaultMaybe;
        }
        return value;
    }

    public String require(String key) {
        String value = readProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Required configuration is missing: " + key);
        }
        return value;
    }

    public boolean isDatabaseAvailable() {
        if (connectionProvider == null) {
            return false;
        }
        try (var c = connectionProvider.getConnection()) {
            return c != null && !c.isClosed();
        } catch (Exception ignored) {
            return false;
        }
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    public boolean isSeedDemoDataRequested() {
        String value = readProperty(PROP_SEED);
        return "true".equalsIgnoreCase(value);
    }

    public void shutdown() {
        if (connectionProvider != null) {
            try {
                connectionProvider.close();
            } catch (Exception ignored) {
            }
        }
    }

    private void loadFromEnvironment() {
        loadEnvironmentVariables();
        loadEnvFileIfPresent();
    }

    private void loadEnvironmentVariables() {
        for (String key : Map.of(
                PROP_HOST, "DB_HOST",
                PROP_PORT, "DB_PORT",
                PROP_NAME, "DB_NAME",
                PROP_USER, "DB_USER",
                PROP_PASSWORD, "DB_PASSWORD",
                PROP_LOG_LEVEL, "APP_LOG_LEVEL",
                PROP_SEED, "APP_SEED_DEMO_DATA"
        ).keySet()) {
            String value = System.getProperty(key);
            if (value == null) {
                value = System.getenv(key);
            }
            if (value != null) {
                properties.put(key, value);
            }
        }
    }

    private void loadEnvFileIfPresent() {
        Path envPath = findEnvFile();
        if (envPath == null) {
            return;
        }
        try {
            Files.lines(envPath)
                    .map(String::trim)
                    .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                    .forEach(line -> {
                        int separator = line.indexOf('=');
                        if (separator > 0) {
                            String key = line.substring(0, separator).trim();
                            String value = line.substring(separator + 1).trim();
                            properties.putIfAbsent(key, value);
                        }
                    });
        } catch (IOException e) {
            log.warn("Could not read env file", e);
        }
    }

    private Path findEnvFile() {
        String userDir = System.getProperty("user.dir");
        Path candidate = Path.of(userDir, ".env");
        if (Files.exists(candidate)) {
            return candidate;
        }
        candidate = Path.of(userDir, ".env.local");
        if (Files.exists(candidate)) {
            return candidate;
        }
        return null;
    }

    private String readProperty(String key) {
        return properties.get(key);
    }

    private void applyLogging() {
        String level = readProperty(PROP_LOG_LEVEL);
        if (level != null && !level.isBlank()) {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", level);
        }
    }

    private void initConnectionProvider() {
        String host = readProperty(PROP_HOST);
        String port = readProperty(PROP_PORT);
        String database = readProperty(PROP_NAME);
        String user = readProperty(PROP_USER);
        String password = readProperty(PROP_PASSWORD);

        if (host == null || port == null || database == null || user == null) {
            log.warn("Database configuration is incomplete; connection will be unavailable");
            connectionProvider = null;
            return;
        }

        connectionProvider = new ConnectionProvider(host, port, database, user, password);
        log.info("Database connection provider configured for db={} host={} user={}",
                database, host, user);
    }
}
