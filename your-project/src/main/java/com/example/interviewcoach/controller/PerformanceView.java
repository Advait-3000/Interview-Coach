package com.example.interviewcoach.controller;

import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.service.PerformanceService;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class PerformanceView {

    public void show(Stage stage, AuthenticatedUser user,
                      PerformanceService performanceService,
                      HomeController homeController) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Performance");
        alert.setHeaderText("Performance analysis is under development");
        alert.setContentText("This feature is being implemented.");
        alert.initOwner(stage);
        alert.showAndWait();
    }
}
