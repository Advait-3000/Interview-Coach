package com.example.interviewcoach.controller;

import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.service.AuthService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class HomeView {

    private static final Logger log = LoggerFactory.getLogger(HomeView.class);

    private Stage stage;
    private HomeController controller;
    private AuthService authService;
    private AuthenticatedUser currentUser;

    public void show(Stage stage, AuthenticatedUser user, AuthService authService) {
        this.stage = stage;
        this.currentUser = user;
        this.authService = authService;
        load();
    }

    private void load() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource("/fxml/HomeView.fxml")));
            BorderPane root = loader.load();

            controller = loader.getController();
            controller.setAuthService(authService);
            controller.setStage(stage);
            controller.setCurrentUser(currentUser);
            controller.enableLogout();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/css/app.css").toExternalForm());

            stage.setScene(scene);
            stage.show();
            log.info("Home shell shown for user id={}", currentUser != null ? currentUser.id() : null);
        } catch (IOException e) {
            log.error("Failed to load home shell", e);
            throw new RuntimeException("Failed to load home shell", e);
        }
    }

    public HomeController controller() {
        return controller;
    }

    public static void navigateToAdminDashboard(Stage stage, AuthenticatedUser user) {
        // TODO: implement admin dashboard
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Admin Dashboard");
        alert.setHeaderText("Admin dashboard");
        alert.setContentText("Admin dashboard is under development.");
        alert.initOwner(stage);
        alert.showAndWait();
    }

    public static void navigateToStudentDashboard(
            Stage stage,
            AuthenticatedUser user
    ) {
        HomeView homeView = new HomeView();
        homeView.show(stage, user, null);
    }
}
