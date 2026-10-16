
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

        Label title = new Label("Start an Interview");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label subtitle = new Label(
                "Choose your category, difficulty, and question count."
        );
        subtitle.setWrapText(true);

        Button startButton = new Button("Start Interview");
        startButton.setMaxWidth(Double.MAX_VALUE);
        startButton.setPrefHeight(44);

        Button backButton = new Button("Back to Dashboard");
        backButton.setMaxWidth(Double.MAX_VALUE);

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

        VBox root = new VBox(
                16,
                title,
                subtitle,
                new Label("Category"),
                categoryBox,
                new Label("Difficulty"),
                difficultyBox,
                new Label("Number of questions"),
                questionCount,
                startButton,
                backButton
        );

        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(36));
        root.setMaxWidth(560);
        root.setStyle("-fx-background-color: #F5F7FB;");

        VBox container = new VBox(root);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(24));

        stage.setTitle("Interview Coach - Interview Setup");
        stage.setScene(new Scene(container, 1000, 700));
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
