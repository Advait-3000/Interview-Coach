package com.example.interviewcoach.controller;

import com.example.interviewcoach.model.Attempt;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class AttemptCard extends VBox {

    public AttemptCard(Attempt attempt) {
        super(6);
        getStyleClass().add("card");
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        setPrefWidth(780);
        setMaxWidth(780);

        HBox header = new HBox(14);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label typeLabel = new Label(attempt.getAttemptType().name().charAt(0) + " "
                + attempt.getAttemptType().name().toLowerCase().replace('_', ' '));
        typeLabel.getStyleClass().add("badge");

        Label scoreLabel = new Label(scoreText(attempt));
        scoreLabel.getStyleClass().add("label-body");
        scoreLabel.setStyle("-fx-font-weight: bold;");

        header.getChildren().addAll(typeLabel, scoreLabel);

        Label dateLabel = new Label(dateText(attempt));
        dateLabel.getStyleClass().add("label-helper");

        Label detailLabel = new Label(
                attempt.getQuestionCount() + " questions · "
                        + correctText(attempt));
        detailLabel.getStyleClass().add("label-helper");

        getChildren().addAll(header, dateLabel, detailLabel);
    }

    private String scoreText(Attempt attempt) {
        Double percent = attempt.getScorePercent();
        if (percent == null) {
            return "Not scored";
        }
        return Math.round(percent) + "%";
    }

    private String correctText(Attempt attempt) {
        int correct = attempt.getCorrectCount();
        int total = attempt.getQuestionCount();
        if (total == 0) {
            return "0/0 correct";
        }
        return correct + "/" + total + " correct";
    }

    private String dateText(Attempt attempt) {
        java.time.LocalDateTime submitted = attempt.getSubmittedAt();
        if (submitted == null) {
            return "";
        }
        return submitted.format(java.time.format.DateTimeFormatter.ofPattern(
                "MMM dd, yyyy · HH:mm"));
    }
}
