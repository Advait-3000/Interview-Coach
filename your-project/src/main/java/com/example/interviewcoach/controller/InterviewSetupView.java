
package com.example.interviewcoach.controller;

import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.model.AttemptType;
import com.example.interviewcoach.model.Category;
import com.example.interviewcoach.model.Difficulty;
import com.example.interviewcoach.model.Question;
import com.example.interviewcoach.repository.AttemptRepository;
import com.example.interviewcoach.repository.QuestionRepository;
import com.example.interviewcoach.util.SessionService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class InterviewSetupView {

    private final Stage stage;
    private final QuestionRepository questionRepository;
    private final AttemptRepository attemptRepository;

    public InterviewSetupView(
            Stage stage,
            QuestionRepository questionRepository,
            AttemptRepository attemptRepository
    ) {
        this.stage = stage;
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
    }

    public void show() {
        
        ComboBox<Category> categoryBox = new ComboBox<>(
                FXCollections.observableArrayList(
                        questionRepository.listActiveCategories()
                )
        );

        categoryBox.setPromptText("Select category");
        categoryBox.setMaxWidth(Double.MAX_VALUE);

        categoryBox.setCellFactory(listView -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                setText(empty || category == null ? null : category.getName());
            }
        });

        categoryBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                setText(empty || category == null ? null : category.getName());
            }
        });

        categoryBox.setPromptText("Select category");
        categoryBox.setMaxWidth(Double.MAX_VALUE);

        ComboBox<Difficulty> difficultyBox = new ComboBox<>(
                FXCollections.observableArrayList(Difficulty.values())
        );
        difficultyBox.setValue(Difficulty.EASY);
        difficultyBox.setMaxWidth(Double.MAX_VALUE);

        Spinner<Integer> questionCount = new Spinner<>(1, 50, 10);
        questionCount.setEditable(true);
        questionCount.setMaxWidth(Double.MAX_VALUE);

        Label tag = new Label("SESSION SETUP");
        tag.getStyleClass().add("brand-tag");

        Label title = new Label("Configure Your Interview");
        title.getStyleClass().add("label-title");

        Label subtitle = new Label(
                "Choose your preferred category, difficulty level, and question count."
        );
        subtitle.getStyleClass().add("label-subtitle");
        subtitle.setWrapText(true);

        VBox headerBox = new VBox(8, tag, title, subtitle);

        // Category field group
        Label categoryLabel = new Label("Question Category");
        categoryLabel.getStyleClass().add("label-field");
        Label categoryHint = new Label("Select the subject area you want to practice.");
        categoryHint.getStyleClass().add("label-helper-small");
        categoryBox.getStyleClass().add("combo-box");
        categoryBox.setPrefHeight(42);
        VBox categoryGroup = new VBox(6, categoryLabel, categoryHint, categoryBox);

        // Difficulty field group
        Label difficultyLabel = new Label("Difficulty Level");
        difficultyLabel.getStyleClass().add("label-field");
        Label difficultyHint = new Label("Select the target challenge level.");
        difficultyHint.getStyleClass().add("label-helper-small");
        difficultyBox.getStyleClass().add("combo-box");
        difficultyBox.setPrefHeight(42);
        VBox difficultyGroup = new VBox(6, difficultyLabel, difficultyHint, difficultyBox);

        // Question count field group
        Label countLabel = new Label("Number of Questions");
        countLabel.getStyleClass().add("label-field");
        Label countHint = new Label("Choose between 1 and 50 questions.");
        countHint.getStyleClass().add("label-helper-small");
        questionCount.getStyleClass().add("spinner");
        questionCount.setPrefHeight(42);
        VBox countGroup = new VBox(6, countLabel, countHint, questionCount);

        Button startButton = new Button("Start Interview Session");
        startButton.getStyleClass().add("primary-button");
        startButton.setMaxWidth(Double.MAX_VALUE);
        startButton.setPrefHeight(46);

        Button backButton = new Button("Back to Dashboard");
        backButton.getStyleClass().add("secondary-button");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setPrefHeight(40);

        startButton.setOnAction(event -> {
            Category category = categoryBox.getValue();
            Difficulty difficulty = difficultyBox.getValue();
            AuthenticatedUser user = SessionService.getCurrentUser();

            if (user == null) {
                showError("Your session has expired. Please sign in again.");
                return;
            }

            if (category == null) {
                showError("Please select a category.");
                return;
            }

            int count = questionCount.getValue();

            try {
                List<Question> questions =
                        questionRepository.findActiveQuestions(
                                category.getId(), difficulty, count
                        );

                if (questions.isEmpty()) {
                    showError(
                            "No active questions are available for this category and difficulty."
                    );
                    return;
                }

                if (questions.size() < count) {
                    showError(
                            "Only " + questions.size()
                            + " questions are available. Choose a smaller question count."
                    );
                    return;
                }

                var attempt = attemptRepository.startAttempt(
                        user.id(),
                        AttemptType.PRACTICE,
                        category.getId(),
                        difficulty,
                        questions.size()
                );

                var snapshots = questionRepository.snapshotAttemptQuestions(
                        attempt.getId(), questions
                );

                new InterviewView(
                        stage,
                        attemptRepository,
                        questionRepository,
                        attempt,
                        snapshots
                ).show();

            } catch (Exception ex) {
                showError("Could not start interview: " + ex.getMessage());
            }
        });

        backButton.setOnAction(event ->
                com.example.interviewcoach.util.Launcher
                        .navigateToDashboard(
                                SessionService.getCurrentUser(), stage
                        )
        );

        javafx.scene.control.Separator separator = new javafx.scene.control.Separator();

        VBox buttonBox = new VBox(10, startButton, backButton);
        buttonBox.setPadding(new Insets(12, 0, 0, 0));

        VBox formCard = new VBox(
                18,
                headerBox,
                separator,
                categoryGroup,
                difficultyGroup,
                countGroup,
                buttonBox
        );

        formCard.setAlignment(Pos.TOP_LEFT);
        formCard.setPadding(new Insets(36, 40, 36, 40));
        formCard.setMaxWidth(560);
        formCard.getStyleClass().add("surface");

        VBox outerWrapper = new VBox(formCard);
        outerWrapper.setAlignment(Pos.CENTER);
        outerWrapper.setPadding(new Insets(36));
        outerWrapper.setStyle("-fx-background-color: #F5F7FB;");

        javafx.scene.control.ScrollPane scroll = new javafx.scene.control.ScrollPane(outerWrapper);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.getStyleClass().add("scroll-pane");

        Scene scene = new Scene(scroll, 1060, 740);
        scene.getStylesheets().add(
                java.util.Objects.requireNonNull(
                        getClass().getResource("/css/app.css")
                ).toExternalForm()
        );

        stage.setTitle("Interview Coach - Configure Interview");
        stage.setScene(scene);
        stage.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(stage);
        alert.setTitle("Interview Setup");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
