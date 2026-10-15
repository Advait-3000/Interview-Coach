package com.example.interviewcoach.service;

import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.model.User;
import com.example.interviewcoach.model.UserRole;
import com.example.interviewcoach.repository.UserRepository;
import com.example.interviewcoach.util.SessionService;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final int MIN_PASSWORD_LENGTH = 10;

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<AuthenticatedUser> login(String emailOrNull, String password) {
        if (emailOrNull == null || password == null) {
            return Optional.empty();
        }

        String normalizedEmail = normalizeEmail(emailOrNull);
        Optional<User> maybeUser = userRepository.findByEmailNormalized(normalizedEmail);

        if (maybeUser.isEmpty()) {
            log.info("Login attempt for unknown email");
            return Optional.empty();
        }

        User user = maybeUser.get();
        if (!user.isActive()) {
            log.info("Login attempt for inactive user id={}", user.getId());
            return Optional.empty();
        }

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            log.info("Login attempt with invalid password for user id={}", user.getId());
            return Optional.empty();
        }

        log.info("User id={} logged in", user.getId());
        return Optional.of(new AuthenticatedUser(user.getId(), user.getFullName(),
                user.getEmail(), user.getRole()));
    }

    public Optional<User> register(String fullName, String email, String password,
                                   String confirmPassword) {
        validateRegistrationInput(fullName, email, password, confirmPassword);

        String normalizedEmail = normalizeEmail(email);
        if (userRepository.emailExists(normalizedEmail)) {
            return Optional.empty();
        }

        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(
                0,
                fullName,
                normalizedEmail,
                hash,
                UserRole.STUDENT,
                true,
                null,
                null
        );

        User created = userRepository.insert(user);
        log.info("Registered user id={}", created.getId());
        return Optional.of(created);
    }

    public void logout() {
        SessionService.clear();
        log.info("User logged out");
    }

    public AuthenticatedUser getCurrentUser() {
        return SessionService.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return SessionService.isLoggedIn();
    }

    public void requireAdmin() {
        AuthenticatedUser user = getCurrentUser();
        if (user == null || user.role() != UserRole.ADMIN) {
            throw new AuthorizationException("Admin access is required");
        }
    }

    public void requireStudent() {
        AuthenticatedUser user = getCurrentUser();
        if (user == null || user.role() != UserRole.STUDENT) {
            throw new AuthorizationException("Student access is required");
        }
    }

    public boolean isCurrentUserAdmin() {
        AuthenticatedUser user = getCurrentUser();
        return user != null && user.role() == UserRole.ADMIN;
    }

    public boolean isCurrentUserStudent() {
        AuthenticatedUser user = getCurrentUser();
        return user != null && user.role() == UserRole.STUDENT;
    }

    private void validateRegistrationInput(String fullName, String email,
                                           String password, String confirmPassword) {
        if (fullName == null || fullName.isBlank()) {
            throw new ValidationException("Full name is required");
        }
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required");
        }
        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$")) {
            throw new ValidationException("Enter a valid email address");
        }
        if (password == null || password.isBlank()) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new ValidationException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
        if (confirmPassword == null || !password.equals(confirmPassword)) {
            throw new ValidationException("Passwords do not match");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
