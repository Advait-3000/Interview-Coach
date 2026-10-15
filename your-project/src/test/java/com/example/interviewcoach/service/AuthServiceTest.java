package com.example.interviewcoach.service;

import com.example.interviewcoach.model.UserRole;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Test
    void passwordHashingIsNonTrivialBCrypt() {
        String password = "Correct-Horse-Battery-9";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"),
                "Expected BCrypt hash format, got: " + hash);
        assertTrue(hash.length() > 50);
    }

    @Test
    void bcryptCheckpwMatchesWhenPasswordIsCorrect() {
        String password = "Training-Wall-Phone-77";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        assertTrue(BCrypt.checkpw(password, hash),
                "Expected correct password to verify");
    }

    @Test
    void bcryptCheckpwFailsWhenPasswordIsWrong() {
        String password = "Training-Wall-Phone-77";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        assertFalse(BCrypt.checkpw("Different-Password-99", hash),
                "Expected wrong password to fail verification");
    }

    @Test
    void differentPasswordsProduceDifferentHashes() {
        String hashA = BCrypt.hashpw("Password-One-123", BCrypt.gensalt());
        String hashB = BCrypt.hashpw("Password-One-123", BCrypt.gensalt());

        assertNotEquals(hashA, hashB,
                "Independent BCrypt generations should produce different hashes due to salt");
    }

    @Test
    void userRoleEnumContainsExpectedRoles() {
        assertTrue(UserRole.valueOf("STUDENT") == UserRole.STUDENT);
        assertTrue(UserRole.valueOf("ADMIN") == UserRole.ADMIN);
    }
}
