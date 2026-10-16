package com.example.interviewcoach.controller;

import com.example.interviewcoach.config.AppConfig;
import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.service.AuthService;
import com.example.interviewcoach.util.SessionService;
import com.example.interviewcoach.util.Launcher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label errorLabel;
    @FXML private Label statusLabel;

    private AuthService authService;
    private Stage stage;

    public LoginController() {
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // @FXML
    // public void initialize() {
    //     if (logoutButton != null) {
    //         logoutButton.setDisable(false);
    //     }
    // }

    @FXML
    private void handleLogin() {
        if (stage == null) {
            return;
        }

        String email = emailField.getText();
        String password = passwordField.getText();
        errorLabel.setText("");

        if (!isDatabaseAvailable()) {
            showDatabaseError();
            return;
        }

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            errorLabel.setText("Email and password are required");
            return;
        }

        setControlsEnabled(false);
        try {
            var result = authService.login(email, password);
            if (result.isEmpty()) {
                errorLabel.setText("Invalid email or password");
                return;
            }

            AuthenticatedUser user = result.get();
            Launcher.navigateToDashboard(user, stage);
        } catch (Exception e) {
            log.error("Login failed", e);
            errorLabel.setText("Sign in failed. Try again.");
        } finally {
            setControlsEnabled(true);
        }
    }

    @FXML
    private void handleRegister() {
        if (stage == null) {
            return;
        }
        try {
            var registerView = new RegisterView();
            registerView.show(stage, authService);
        } catch (Exception e) {
            log.error("Failed to open registration", e);
            showUnexpectedError(e);
        }
    }

    private boolean isDatabaseAvailable() {
        if (authService == null) {
            return false;
        }
        try {
            AppConfig config = AppConfig.getInstance();
            return config.isDatabaseAvailable();
        } catch (Exception e) {
            log.warn("Database availability check failed", e);
            return false;
        }
    }

    private void setControlsEnabled(boolean enabled) {
        emailField.setEditable(enabled);
        passwordField.setEditable(enabled);
        loginButton.setDisable(!enabled);
        registerButton.setDisable(!enabled);
    }

    private void showDatabaseError() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Database unavailable");
        alert.setHeaderText("Cannot reach the database");
        alert.setContentText(
                "Interview Coach could not connect to MySQL. Check that MySQL is running "
                        + "and that the local environment file is configured, then restart the app.");
        alert.initOwner(stage);
        alert.showAndWait();
    }

    private void showUnexpectedError(Exception e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Unexpected error");
        alert.setHeaderText("The application encountered an unexpected error");
        alert.setContentText("See the application logs for details.");
        alert.initOwner(stage);
        alert.showAndWait();
        log.error("Unexpected error in login screen", e);
    }
}
