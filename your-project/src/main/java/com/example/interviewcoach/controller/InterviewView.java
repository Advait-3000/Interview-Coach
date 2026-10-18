
package com.example.interviewcoach.controller;

import com.example.interviewcoach.model.Attempt;
import com.example.interviewcoach.model.AttemptAnswer;
import com.example.interviewcoach.model.AttemptQuestion;
import com.example.interviewcoach.model.AttemptStatus;
import com.example.interviewcoach.repository.AttemptRepository;
import com.example.interviewcoach.repository.QuestionRepository;
import com.example.interviewcoach.service.AnswerOutcome;
import com.example.interviewcoach.service.AttemptResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InterviewView {

    private final Stage stage;
    private final AttemptRepository attemptRepository;
    private final QuestionRepository questionRepository;
    private final Attempt attempt;
    private final List<AttemptQuestion> questions;

    private final Map<Long, Character> answers = new LinkedHashMap<>();

    private int currentIndex = 0;

    private Label progressLabel;
    private Label questionLabel;
    private Label answeredLabel;
    private final RadioButton[] options = new RadioButton[4];
    private ToggleGroup optionGroup;
    private Button previousButton;
    private Button nextButton;
    private Button submitButton;

    public InterviewView(
            Stage stage,
            AttemptRepository attemptRepository,
            QuestionRepository questionRepository,
            Attempt attempt,
            List<AttemptQuestion> questions
    ) {
        this.stage = stage;
        this.attemptRepository = attemptRepository;
        this.questionRepository = questionRepository;
        this.attempt = attempt;
        this.questions = new ArrayList<>(questions);
    }

    public void show() {
        if (questions.isEmpty()) {
            showMessage("No Questions", "There are no questions in this attempt.");
            return;
        }

        progressLabel = new Label();
        progressLabel.getStyleClass().add("badge-primary");

        answeredLabel = new Label();
        answeredLabel.getStyleClass().add("badge-neutral");

        questionLabel = new Label();
        questionLabel.setWrapText(true);
        questionLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        questionLabel.setStyle("-fx-text-fill: #142D52; -fx-line-spacing: 4px;");

        optionGroup = new ToggleGroup();

        VBox optionBox = new VBox(12);
        for (int i = 0; i < options.length; i++) {
            options[i] = new RadioButton();
            options[i].setToggleGroup(optionGroup);
            options[i].setWrapText(true);
            options[i].setMaxWidth(Double.MAX_VALUE);
            options[i].getStyleClass().add("quiz-option");
            optionBox.getChildren().add(options[i]);
        }

        previousButton = new Button("← Previous");
        previousButton.getStyleClass().add("secondary-button");
        previousButton.setPrefWidth(120);
        previousButton.setPrefHeight(40);

        nextButton = new Button("Next →");
        nextButton.getStyleClass().add("secondary-button");
        nextButton.setPrefWidth(120);
        nextButton.setPrefHeight(40);

        submitButton = new Button("Submit Interview");
        submitButton.getStyleClass().add("primary-button");
        submitButton.setPrefWidth(160);
        submitButton.setPrefHeight(40);

        previousButton.setOnAction(event -> {
            saveCurrentAnswer();
            if (currentIndex > 0) {
                currentIndex--;
                renderQuestion();
            }
        });

        nextButton.setOnAction(event -> {
            saveCurrentAnswer();
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                renderQuestion();
            }
        });

        submitButton.setOnAction(event -> submitInterview());

        HBox navButtons = new HBox(12, previousButton, nextButton);
        navButtons.setAlignment(Pos.CENTER_LEFT);

        javafx.scene.layout.Region navSpacer = new javafx.scene.layout.Region();
        HBox.setHgrow(navSpacer, javafx.scene.layout.Priority.ALWAYS);

        HBox navigation = new HBox(navButtons, navSpacer, submitButton);
        navigation.setAlignment(Pos.CENTER_LEFT);
        navigation.setPadding(new Insets(12, 0, 0, 0));

        // Top Status Header inside Card
        HBox topStatus = new HBox(12, progressLabel, answeredLabel);
        topStatus.setAlignment(Pos.CENTER_LEFT);

        javafx.scene.control.Separator separator = new javafx.scene.control.Separator();

        VBox card = new VBox(
                20,
                topStatus,
                separator,
                questionLabel,
                optionBox,
                new javafx.scene.control.Separator(),
                navigation
        );

        card.setPadding(new Insets(36, 40, 36, 40));
        card.setMaxWidth(860);
        card.getStyleClass().add("surface");

        VBox container = new VBox(card);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(32));
        container.setStyle("-fx-background-color: #F5F7FB;");

        javafx.scene.control.ScrollPane scroll = new javafx.scene.control.ScrollPane(container);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.getStyleClass().add("scroll-pane");

        Scene scene = new Scene(scroll, 1060, 760);
        scene.getStylesheets().add(
                java.util.Objects.requireNonNull(
                        getClass().getResource("/css/app.css")
                ).toExternalForm()
        );

        stage.setTitle("Interview Coach - Assessment");
        stage.setScene(scene);

        renderQuestion();
        stage.show();
    }

    private void renderQuestion() {
        AttemptQuestion question = questions.get(currentIndex);

        progressLabel.setText(
                "Question " + (currentIndex + 1) + " of " + questions.size()
        );
        questionLabel.setText((currentIndex + 1) + ". " + question.getPromptSnapshot());

        String[] optionTexts = {
                question.getOptionASnapshot(),
                question.getOptionBSnapshot(),
                question.getOptionCSnapshot(),
                question.getOptionDSnapshot()
        };

        char[] letters = {'A', 'B', 'C', 'D'};

        optionGroup.selectToggle(null);

        for (int i = 0; i < options.length; i++) {
            options[i].setText("   " + letters[i] + ".  " + optionTexts[i]);
            options[i].setUserData(letters[i]);
            options[i].setSelected(
                    answers.get(question.getQuestionId()) != null
                    && answers.get(question.getQuestionId()) == letters[i]
            );
        }

        previousButton.setDisable(currentIndex == 0);
        nextButton.setDisable(currentIndex == questions.size() - 1);

        long answeredCount = answers.values().stream().count();
        answeredLabel.setText(
                "Answered: " + answeredCount + " / " + questions.size()
        );
    }

    private void saveCurrentAnswer() {
        AttemptQuestion question = questions.get(currentIndex);

        if (optionGroup.getSelectedToggle() == null) {
            answers.remove(question.getQuestionId());
        } else {
            Character selected = (Character)
                    optionGroup.getSelectedToggle().getUserData();
            answers.put(question.getQuestionId(), selected);
        }
    }

    private void submitInterview() {
        saveCurrentAnswer();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.initOwner(stage);
        confirmation.setTitle("Submit Interview");
        confirmation.setHeaderText("Submit your answers?");
        confirmation.setContentText(
                "You answered " + answers.size() + " of "
                + questions.size() + " questions."
        );

        var result = confirmation.showAndWait();

        if (result.isEmpty()
                || result.get() != javafx.scene.control.ButtonType.OK) {
            return;
        }

        try {
            List<AnswerOutcome> outcomes = new ArrayList<>();
            Map<Long, AttemptAnswer> answerRecords = new LinkedHashMap<>();
            int correctCount = 0;
            int unansweredCount = 0;

            for (AttemptQuestion question : questions) {
                Character selected = answers.get(question.getQuestionId());
                boolean correct = selected != null
                        && selected == question.getCorrectOptionSnapshot();

                if (correct) {
                    correctCount++;
                }
                if (selected == null) {
                    unansweredCount++;
                }

                AttemptAnswer answer = new AttemptAnswer(
                        0,
                        attempt.getId(),
                        question.getQuestionId(),
                        selected,
                        correct,
                        null
                );

                answerRecords.put(question.getQuestionId(), answer);

                outcomes.add(new AnswerOutcome(
                        question.getQuestionId(),
                        question.getQuestionOrder(),
                        question.getPromptSnapshot(),
                        selected,
                        question.getCorrectOptionSnapshot(),
                        correct,
                        question.getExplanationSnapshot(),
                        question.getCategorySnapshot(),
                        question.getDifficultySnapshot()
                ));
            }

            int total = questions.size();
            double score = total == 0
                    ? 0.0
                    : correctCount * 100.0 / total;

            AttemptResult attemptResult = new AttemptResult(
                    attempt,
                    outcomes,
                    answerRecords,
                    correctCount,
                    total,
                    unansweredCount,
                    score
            );

            attemptRepository.finalizeAttempt(
                    attempt.getId(), answers, attemptResult
            );

            showResults(correctCount, total, unansweredCount, score);

        } catch (Exception ex) {
            showMessage(
                    "Submission Failed",
                    "Could not save your interview: " + ex.getMessage()
            );
        }
    }

    private void showResults(
            int correct,
            int total,
            int unanswered,
            double score
    ) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(stage);
        alert.setTitle("Interview Results");
        alert.setHeaderText("Interview Completed!");
        alert.setContentText(
                "Score: " + String.format("%.1f%%", score)
                + "\nCorrect answers: " + correct
                + "\nIncorrect answers: " + (total - correct - unanswered)
                + "\nUnanswered: " + unanswered
                + "\nTotal questions: " + total
        );

        alert.showAndWait();

        com.example.interviewcoach.util.Launcher.navigateToDashboard(
                com.example.interviewcoach.util.SessionService.getCurrentUser(),
                stage
        );
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
