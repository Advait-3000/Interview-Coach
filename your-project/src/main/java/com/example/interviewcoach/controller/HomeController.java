package com.example.interviewcoach.controller;

import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.service.AuthService;
import com.example.interviewcoach.util.Launcher;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HomeController {

    @FXML private Label appTitle;
    @FXML private Label userLabel;
    @FXML private Button logoutButton;
    @FXML private Pane contentPane;

    private AuthService authService;
    private Stage stage;
    private AuthenticatedUser currentUser;

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentUser(AuthenticatedUser user) {
        this.currentUser = user;

        if (user != null && userLabel != null) {
            userLabel.setText("Signed in as " + user.fullName());
        } else if (userLabel != null) {
            userLabel.setText("Not signed in");
        }

        showDashboard();
    }

    @FXML
    public void initialize() {
        if (logoutButton != null) {
            logoutButton.setDisable(false);
        }

        showDashboard();
    }

    @FXML
    private void handleLogout() {
        if (authService != null) {
            authService.logout();
        }

        Launcher.logout(stage);
    }

    private void showDashboard() {
        VBox dashboard = new VBox(22);
        dashboard.setPadding(new Insets(32));
        dashboard.setStyle("-fx-background-color: #F5F7FB;");

        Label heading = new Label("Student Dashboard");
        heading.setFont(Font.font("System", FontWeight.BOLD, 28));

        String name = currentUser == null
                ? "Welcome back!"
                : "Welcome back, " + currentUser.fullName() + "!";

        Label welcome = new Label(name);
        welcome.setStyle("-fx-text-fill: #64748B; -fx-font-size: 15px;");

        VBox introduction = new VBox(8, heading, welcome);

        GridPane statistics = new GridPane();
        statistics.setHgap(16);
        statistics.setVgap(16);

        statistics.add(createStatCard("Interviews Completed", "0"),
                0, 0);
        statistics.add(createStatCard("Questions Answered", "0"),
                1, 0);
        statistics.add(createStatCard("Average Score", "—"),
                0, 1);
        statistics.add(createStatCard("Practice Sessions", "0"),
                1, 1);

        Label actionsHeading = new Label("Quick Actions");
        actionsHeading.setFont(Font.font("System", FontWeight.BOLD, 20));

        Button practiceButton = new Button("Start an Interview");
        practiceButton.setMaxWidth(Double.MAX_VALUE);
        practiceButton.setPrefHeight(48);
        practiceButton.setStyle(
                "-fx-background-color: #2458C5;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;"
        );

        practiceButton.setOnAction(event ->
                showMessage(
                        "Interview Practice",
                        "Interview practice is the next feature to implement."
                )
        );

        Button historyButton = new Button("View Interview History");
        historyButton.setMaxWidth(Double.MAX_VALUE);
        historyButton.setPrefHeight(48);

        historyButton.setOnAction(event ->
                showMessage(
                        "Interview History",
                        "Interview history will be available here."
                )
        );

        HBox actions = new HBox(14, practiceButton, historyButton);
        actions.setAlignment(Pos.CENTER_LEFT);

        dashboard.getChildren().addAll(
                introduction,
                statistics,
                actionsHeading,
                actions
        );

        showContent(dashboard);
    }

    private VBox createStatCard(String title, String value) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-text-fill: #64748B; -fx-font-size: 13px;"
        );

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 26));
        valueLabel.setStyle("-fx-text-fill: #142D52;");

        VBox card = new VBox(12, titleLabel, valueLabel);
        card.setPadding(new Insets(22));
        card.setPrefSize(300, 115);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E2E8F0;" +
                "-fx-border-radius: 12;"
        );

        return card;
    }

    private void showMessage(String title, String message) {
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);

        if (stage != null) {
            alert.initOwner(stage);
        }

        alert.showAndWait();
    }

    public void enableLogout() {
        if (logoutButton != null) {
            logoutButton.setDisable(false);
        }
    }

    protected void showContent(Pane content) {
        contentPane.getChildren().setAll(content);
    }
}
