package com.example.interviewcoach.controller;

import com.example.interviewcoach.service.AuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterController {

    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private Button createButton;
    @FXML private Button backButton;
    @FXML private Label nameValidation;
    @FXML private Label emailValidation;
    @FXML private Label passwordValidation;
    @FXML private Label errorLabel;
    @FXML private Label successLabel;

    private AuthService authService;
    private Stage stage;

    public RegisterController() {
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        clearMessages();
    }

    @FXML
    private void handleCreateAccount() {
        log.info("Create Account clicked");
        log.info("Stage initialized: {}", stage != null);
        log.info("AuthService initialized: {}", authService != null);

        if (stage == null || authService == null) {
            log.error("Registration stopped: dependencies are not initialized");
            errorLabel.setText("Registration is not initialized. Please try again.");
            return;
        }

        clearMessages();

        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirm = confirmField.getText();

        boolean nameOk = validateName(name, nameValidation);
        boolean emailOk = validateEmail(email, emailValidation);
        boolean passwordOk = validatePassword(password, confirm, passwordValidation);

        if (!nameOk || !emailOk || !passwordOk) {
            return;
        }

        setControlsEnabled(false);
        successLabel.setText("");
        errorLabel.setText("");

        try {
            var result = authService.register(name.trim(), email.trim(), password, confirm);
            if (result.isEmpty()) {
                errorLabel.setText("An account with this email already exists.");
                return;
            }

            successLabel.setText("Account created. You can now sign in.");
            nameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            confirmField.setText("");
            log.info("Registration success for email={}", email.trim());
        } catch (Exception e) {
            log.error("Registration failed", e);
            errorLabel.setText("Could not create account. Try again.");
        } finally {
            setControlsEnabled(true);
        }
    }

    @FXML
    private void handleBack() {
        if (stage == null) {
            stage.close();
        }
        // stage.close();
    }

    private boolean validateName(String name, Label label) {
        if (name == null || name.isBlank()) {
            label.setText("Full name is required");
            return false;
        }
        if (name.trim().length() < 2) {
            label.setText("Enter your full name");
            return false;
        }
        label.setText("");
        return true;
    }

    private boolean validateEmail(String email, Label label) {
        if (email == null || email.isBlank()) {
            label.setText("Email is required");
            return false;
        }
        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$")) {
            label.setText("Enter a valid email address");
            return false;
        }
        label.setText("");
        return true;
    }

    private boolean validatePassword(String password, String confirm, Label label) {
        if (password == null || password.isBlank()) {
            label.setText("Password is required");
            return false;
        }
        if (password.length() < 10) {
            label.setText("Password must be at least 10 characters");
            return false;
        }
        if (confirm == null || !password.equals(confirm)) {
            label.setText("Passwords do not match");
            return false;
        }
        label.setText("");
        return true;
    }

    private void clearMessages() {
        nameValidation.setText("");
        emailValidation.setText("");
        passwordValidation.setText("");
        errorLabel.setText("");
        successLabel.setText("");
    }

    private void setControlsEnabled(boolean enabled) {
        nameField.setEditable(enabled);
        emailField.setEditable(enabled);
        passwordField.setEditable(enabled);
        confirmField.setEditable(enabled);
        createButton.setDisable(!enabled);
        backButton.setDisable(!enabled);
    }
}
