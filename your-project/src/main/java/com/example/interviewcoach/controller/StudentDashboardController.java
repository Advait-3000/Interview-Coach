package com.example.interviewcoach.controller;

import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.model.Attempt;
import com.example.interviewcoach.service.PerformanceService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StudentDashboardController {

    private static final Logger log = LoggerFactory.getLogger(StudentDashboardController.class);

    @FXML private Label greetingLabel;
    @FXML private Label subtitleLabel;
    @FXML private FlowPane actionCards;
    @FXML private Label recentLabel;
    @FXML private Button historyButton;
    @FXML private VBox recentSummary;
    @FXML private VBox emptyState;
    @FXML private Label emptyTitle;
    @FXML private Label emptyHint;
    @FXML private ScrollPane recentScroll;

    private AuthenticatedUser user;
    private PerformanceService performanceService;
    private Stage stage;
    private HomeController homeController;

    public StudentDashboardController() {
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }

    public void setPerformanceService(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    @FXML
    public void initialize() {
        actionCards.setPrefWrapLength(780);
    }

    public void render() {
        if (user == null) {
            return;
        }

        greetingLabel.setText("Welcome back, " + user.fullName() + ".");
        subtitleLabel.setText("Practice questions, take mock tests, and review your progress.");

        rebuildActionCards();
        rebuildRecentSection();
    }

    private void rebuildActionCards() {
        actionCards.getChildren().clear();

        actionCards.getChildren().addAll(
                makeActionCard("Practice", "Practice questions by topic and difficulty",
                        "Practice.png", () -> handlePractice()),
                makeActionCard("Mock test", "Timed-style mock assessment",
                        "MockTest.png", () -> handleMockTest()),
                makeActionCard("History", "Review past attempts and answers",
                        "History.png", () -> handleHistory()),
                makeActionCard("Performance", "See topic-wise strengths and weak areas",
                        "Performance.png", () -> handlePerformance())
        );
    }

    private Button makeActionCard(String title, String description,
                                    String iconName, Runnable action) {
        VBox card = new VBox(8);
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setPrefSize(170, 150);
        card.getStyleClass().add("card");

        Label textTitle = new Label(title);
        textTitle.getStyleClass().add("label-section");

        Label textDesc = new Label(description);
        textDesc.getStyleClass().add("label-helper");
        textDesc.setWrapText(true);
        textDesc.setMaxWidth(150);

        Button actionButton = new Button("Open");
        actionButton.getStyleClass().add("primary-button");
        actionButton.setPrefWidth(96);
        actionButton.setOnAction(ev -> action.run());

        card.getChildren().addAll(textTitle, textDesc, actionButton);
        return actionButton;
    }

    private void rebuildRecentSection() {
        recentSummary.getChildren().clear();
        emptyState.setVisible(false);
        emptyState.setManaged(false);
        recentLabel.setVisible(true);

        if (performanceService == null) {
            recentLabel.setText("Recent attempts");
            return;
            }

        List<Attempt> recent = performanceService.getHistory(user.id(), 0, 3);
        if (recent.isEmpty()) {
            recentLabel.setVisible(false);
            emptyState.setVisible(true);
            emptyState.setManaged(true);
            emptyTitle.setText("No attempts yet");
            emptyHint.setText("Start a practice session or mock test to see your results here.");
            return;
        }

        recentLabel.setText("Recent attempts");
        for (Attempt a : recent) {
            AttemptCard card = new AttemptCard(a);
            recentSummary.getChildren().add(card);
        }
    }

    @FXML
    private void handlePractice() {
        try {
            PracticeView view = new PracticeView();
            view.show(stage, user, performanceService, homeController);
        } catch (Exception e) {
            log.error("Failed to open practice", e);
            showError("Could not open practice.");
        }
    }

    @FXML
    private void handleMockTest() {
        try {
            MockTestView view = new MockTestView();
            view.show(stage, user, performanceService, homeController);
        } catch (Exception e) {
            log.error("Failed to open mock test", e);
            showError("Could not open mock test.");
        }
    }

    @FXML
    private void handleHistory() {
        try {
            HistoryView view = new HistoryView();
            view.show(stage, user, performanceService, homeController);
        } catch (Exception e) {
            log.error("Failed to open history", e);
            showError("Could not open history.");
        }
    }

    @FXML
    private void handlePerformance() {
        try {
            PerformanceView view = new PerformanceView();
            view.show(stage, user, performanceService, homeController);
        } catch (Exception e) {
            log.error("Failed to open performance", e);
            showError("Could not open performance.");
        }
    }

    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(stage);
        alert.showAndWait();
    }
}
