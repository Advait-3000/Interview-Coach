package com.example.interviewcoach.controller;

import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.service.AuthService;
import com.example.interviewcoach.util.Launcher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @FXML private Label appTitle;
    @FXML private Label userLabel;
    @FXML private Button logoutButton;
    @FXML private Pane contentPane;

    private AuthService authService;
    private Stage stage;
    private AuthenticatedUser currentUser;

    public HomeController() {
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentUser(AuthenticatedUser user) {
        this.currentUser = user;
        if (user != null) {
            userLabel.setText("Signed in as " + user.fullName());
        } else {
            userLabel.setText("Not signed in");
        }
    }

    @FXML
    public void initialize() {
        logoutButton.setDisable(true);
    }

    public void enableLogout() {
        logoutButton.setDisable(false);
    }

    @FXML
    private void handleLogout() {
        if (authService == null) {
            Launcher.logout(stage);
            return;
        }
        authService.logout();
        Launcher.logout(stage);
    }

    protected void showContent(Pane content) {
        contentPane.getChildren().setAll(content);
    }
}
