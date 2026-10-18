package com.example.interviewcoach.controller;

import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.service.AuthService;
import com.example.interviewcoach.util.Launcher;
import com.example.interviewcoach.config.AppConfig;
import com.example.interviewcoach.repository.ConnectionProvider;
import com.example.interviewcoach.repository.AttemptRepositoryAnswers;
import com.example.interviewcoach.repository.AttemptRepository;
import com.example.interviewcoach.service.PerformanceService;
import com.example.interviewcoach.repository.QuestionRepository;
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
        VBox dashboard = new VBox(24);
        dashboard.setMaxWidth(1020);
        dashboard.setAlignment(Pos.TOP_LEFT);

        // Header Welcome Banner
        String name = currentUser == null
                ? "Student"
                : currentUser.fullName();

        Label heading = new Label("Student Dashboard");
        heading.getStyleClass().add("label-title");

        Label welcome = new Label("Welcome back, " + name + "! Ready to prepare for your next interview?");
        welcome.getStyleClass().add("label-subtitle");

        VBox introduction = new VBox(6, heading, welcome);
        introduction.getStyleClass().add("surface");
        introduction.setPadding(new Insets(24, 28, 24, 28));

        // Retrieve real statistics if database is available
        String completedCount = "0";
        String questionsCount = "0";
        String avgScore = "—";
        String practiceCount = "0";

        ConnectionProvider provider = null;
        try {
            provider = AppConfig.getInstance().getConnectionProvider();
        } catch (Exception ignored) {
        }
        if (currentUser != null && provider != null) {
            try {
                var attemptRepository = new AttemptRepository(provider);
                long totalCompleted = attemptRepository.countCompletedByUser(currentUser.id());
                completedCount = String.valueOf(totalCompleted);

                var recentAttempts = attemptRepository.findCompletedByUser(currentUser.id(), 0, 100);
                int totalQuestions = 0;
                double totalScore = 0;
                int scoredAttempts = 0;
                int practiceSessions = 0;

                for (var a : recentAttempts) {
                    totalQuestions += a.getQuestionCount();
                    if (a.getScorePercent() != null) {
                        totalScore += a.getScorePercent();
                        scoredAttempts++;
                    }
                    if (a.getAttemptType() != null && "PRACTICE".equalsIgnoreCase(a.getAttemptType().name())) {
                        practiceSessions++;
                    }
                }

                if (totalQuestions > 0) {
                    questionsCount = String.valueOf(totalQuestions);
                }
                if (scoredAttempts > 0) {
                    avgScore = String.format("%.0f%%", totalScore / scoredAttempts);
                }
                if (practiceSessions > 0) {
                    practiceCount = String.valueOf(practiceSessions);
                }
            } catch (Exception e) {
                // Keep default placeholder values on query failure
            }
        }

        // Statistics Grid
        GridPane statistics = new GridPane();
        statistics.setHgap(18);
        statistics.setVgap(18);
        statistics.setMaxWidth(Double.MAX_VALUE);

        statistics.add(createStatCard("Interviews Completed", completedCount, "Completed interview sessions", "📊"), 0, 0);
        statistics.add(createStatCard("Questions Answered", questionsCount, "Total questions across attempts", "📝"), 1, 0);
        statistics.add(createStatCard("Average Score", avgScore, "Overall performance accuracy", "🎯"), 0, 1);
        statistics.add(createStatCard("Practice Sessions", practiceCount, "Self-paced practice sessions", "⚡"), 1, 1);

        // Make columns expand equally
        javafx.scene.layout.ColumnConstraints col1 = new javafx.scene.layout.ColumnConstraints();
        col1.setPercentWidth(50);
        javafx.scene.layout.ColumnConstraints col2 = new javafx.scene.layout.ColumnConstraints();
        col2.setPercentWidth(50);
        statistics.getColumnConstraints().addAll(col1, col2);

        // Quick Actions Section
        VBox actionsContainer = new VBox(16);
        actionsContainer.getStyleClass().add("surface");
        actionsContainer.setPadding(new Insets(24, 28, 24, 28));

        Label actionsHeading = new Label("Quick Actions");
        actionsHeading.getStyleClass().add("label-section");

        Label actionsSub = new Label("Start an interview session or review your previous attempts.");
        actionsSub.getStyleClass().add("label-helper");

        Button practiceButton = new Button("Start an Interview");
        practiceButton.getStyleClass().add("primary-button");
        practiceButton.setPrefHeight(46);
        practiceButton.setPrefWidth(220);

        practiceButton.setOnAction(event -> {
            var dbProvider = AppConfig.getInstance().getConnectionProvider();

            if (dbProvider == null) {
                showMessage("Database Error", "Database connection is unavailable.");
                return;
            }

            try {
                var questionRepository = new QuestionRepository(dbProvider);
                var attemptRepository = new AttemptRepository(dbProvider);

                new InterviewSetupView(
                        stage,
                        questionRepository,
                        attemptRepository
                ).show();

            } catch (Exception ex) {
                showMessage(
                        "Interview Setup Error",
                        "Could not open interview setup: " + ex.getMessage()
                );
            }
        });

        Button historyButton = new Button("View Interview History");
        historyButton.getStyleClass().add("secondary-button");
        historyButton.setPrefHeight(46);
        historyButton.setPrefWidth(220);

        historyButton.setOnAction(event -> {
            var dbProvider = AppConfig.getInstance().getConnectionProvider();

            if (dbProvider == null) {
                showMessage(
                        "Database Error",
                        "Database connection is unavailable."
                );
                return;
            }

            try {
                var attemptRepository = new AttemptRepository(dbProvider);
                var answerRepository = new AttemptRepositoryAnswers(dbProvider);
                var questionRepository = new QuestionRepository(dbProvider);

                var performanceService = new PerformanceService(
                        attemptRepository,
                        answerRepository,
                        questionRepository
                );

                new HistoryView().show(
                        stage,
                        currentUser,
                        performanceService,
                        this
                );

            } catch (Exception ex) {
                showMessage(
                        "History Error",
                        "Could not open interview history: " + ex.getMessage()
                );
            }
        });

        HBox actions = new HBox(16, practiceButton, historyButton);
        actions.setAlignment(Pos.CENTER_LEFT);

        actionsContainer.getChildren().addAll(actionsHeading, actionsSub, actions);

        dashboard.getChildren().addAll(
                introduction,
                statistics,
                actionsContainer
        );

        showContent(dashboard);
    }

    private VBox createStatCard(String title, String value, String helper, String icon) {
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #64748B; -fx-font-size: 13px; -fx-font-weight: bold;");

        HBox topRow = new HBox(8, iconLabel, titleLabel);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        valueLabel.setStyle("-fx-text-fill: #142D52;");

        Label helperLabel = new Label(helper);
        helperLabel.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 12px;");

        VBox card = new VBox(10, topRow, valueLabel, helperLabel);
        card.getStyleClass().add("stat-card");
        card.setMaxWidth(Double.MAX_VALUE);

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
