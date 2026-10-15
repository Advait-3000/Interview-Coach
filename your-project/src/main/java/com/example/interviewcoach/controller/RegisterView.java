package com.example.interviewcoach.controller;

import com.example.interviewcoach.service.AuthService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class RegisterView {

    private static final Logger log =
            LoggerFactory.getLogger(RegisterView.class);

    public void show(Stage owner, AuthService authService) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(
                            getClass().getResource("/fxml/RegisterView.fxml")
                    )
            );

            VBox root = loader.load();

            Stage dialog = new Stage();
            dialog.initOwner(owner);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Create account");
            dialog.setResizable(false);

            Scene scene = new Scene(root, 460, 620);
            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            getClass().getResource("/css/app.css")
                    ).toExternalForm()
            );

            dialog.setScene(scene);

            RegisterController controller = loader.getController();
            controller.setAuthService(authService);
            controller.setStage(dialog); // Pass the registration window

            dialog.show();

            log.info("Registration dialog shown");

        } catch (IOException e) {
            log.error("Failed to load registration screen", e);
            throw new RuntimeException(
                    "Failed to load registration screen", e
            );
        }
    }
}