package com.example.interviewcoach.controller;

import com.example.interviewcoach.service.AuthService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class LoginView {

    private static final Logger log = LoggerFactory.getLogger(LoginView.class);

    public void show(Stage stage) {
        show(stage, null);
    }

    public void show(Stage stage, AuthService authService) {
        this.stage = stage;
        loadView(authService);
    }

    private Stage stage;

    private void loadView(AuthService authService) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource("/fxml/LoginView.fxml")));
            VBox root = loader.load();

            LoginController controller = loader.getController();
            controller.setAuthService(authService);
            controller.setStage(stage);

            Scene scene = new Scene(root, 460, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/css/app.css").toExternalForm());

            stage.setScene(scene);
            stage.show();
            log.info("Login screen shown");
        } catch (IOException e) {
            log.error("Failed to load login screen", e);
            throw new RuntimeException("Failed to load login screen", e);
        }
    }
}
