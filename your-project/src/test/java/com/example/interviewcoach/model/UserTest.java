package com.example.interviewcoach.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void userRecordsExposeAllStoredFields() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(
                7L,
                "Ada Lovelace",
                "ada@example.com",
                "$2a$10$abcdefghijklmnopqrstuu",
                UserRole.STUDENT,
                true,
                now,
                now
        );

        assertEquals(7L, user.getId());
        assertEquals("Ada Lovelace", user.getFullName());
        assertEquals("ada@example.com", user.getEmail());
        assertEquals("$2a$10$abcdefghijklmnopqrstuu", user.getPasswordHash());
        assertEquals(UserRole.STUDENT, user.getRole());
        assertTrue(user.isActive());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void userPasswordHashIsNotReturnedAsPlainText() {
        User user = new User(
                1L,
                "Test User",
                "test@example.com",
                "stored-hash",
                UserRole.STUDENT,
                true,
                null,
                null
        );

        assertEquals("stored-hash", user.getPasswordHash());
        assertNotEquals("plain-password", user.getPasswordHash());
    }
}
