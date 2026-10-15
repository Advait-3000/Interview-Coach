package com.example.interviewcoach.controller;

import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.service.PerformanceService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class StudentDashboardView {

    private static final Logger log = LoggerFactory.getLogger(StudentDashboardView.class);

    private HomeController homeController;

    public void show(Stage stage, AuthenticatedUser user,
                      PerformanceService performanceService,
                      HomeController homeController) {
        this.homeController = homeController;
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource(
                            "/fxml/StudentDashboardView.fxml")));
            VBox root = loader.load();

            StudentDashboardController controller = loader.getController();
            controller.setUser(user);
            controller.setPerformanceService(performanceService);
            controller.setStage(stage);
            controller.setHomeController(homeController);

            Scene scene = new Scene(root, 920, 640);
            scene.getStylesheets().add(
                    getClass().getResource("/css/app.css").toExternalForm());

            homeController.showContent(root);
            stage.sizeToScene();
            controller.render();
            log.info("Student dashboard shown for user id={}", user.id());
        } catch (IOException e) {
            log.error("Failed to load student dashboard", e);
            throw new RuntimeException("Failed to load student dashboard", e);
        }
    }
}
