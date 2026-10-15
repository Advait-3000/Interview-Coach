package com.example.interviewcoach.repository;

import com.example.interviewcoach.model.User;
import com.example.interviewcoach.model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    private final ConnectionProvider provider;

    public UserRepository(ConnectionProvider provider) {
        this.provider = provider;
    }

    public Optional<User> findByEmailNormalized(String normalizedEmail) {
        String sql = """
                SELECT id, full_name, email, password_hash, role, is_active,
                       created_at, updated_at
                FROM users
                WHERE email = ?
                  AND is_active = 1
                """;

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, normalizedEmail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to lookup user by email", e);
            throw new RepositoryException("Unable to query user", e);
        }
        return Optional.empty();
    }

    public User insert(User user) {
        String sql = """
                INSERT INTO users (full_name, email, password_hash, role, is_active, created_at, updated_at)
                VALUES (?, ?, ?, ?, 1, NOW(), NOW())
                """;

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getRole().name());
            stmt.executeUpdate();

            long id;
            try (ResultSet generated = stmt.getGeneratedKeys()) {
                if (generated.next()) {
                    id = generated.getLong(1);
                } else {
                    throw new RepositoryException("Could not retrieve generated user id");
                }
            }

            log.info("Created user id={} email={}", id, user.getEmail());
            return new User(
                    id,
                    user.getFullName(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getRole(),
                    true,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
        } catch (SQLException e) {
            log.error("Failed to insert user email={}", user.getEmail(), e);
            throw new RepositoryException("Unable to create user", e);
        }
    }

    public boolean emailExists(String normalizedEmail) {
        return findByEmailNormalized(normalizedEmail).isPresent();
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                UserRole.valueOf(rs.getString("role")),
                rs.getBoolean("is_active"),
                rs.getTimestamp("created_at") != null
                        ? rs.getTimestamp("created_at").toLocalDateTime()
                        : null,
                rs.getTimestamp("updated_at") != null
                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                        : null
        );
    }
}
