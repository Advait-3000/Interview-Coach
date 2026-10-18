package com.example.interviewcoach.controller;

import com.example.interviewcoach.model.Attempt;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class AttemptCard extends VBox {

    public AttemptCard(Attempt attempt) {
        super(10);
        getStyleClass().add("card");
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        setMaxWidth(Double.MAX_VALUE);
        setPadding(new javafx.geometry.Insets(16, 20, 16, 20));

        HBox header = new HBox(12);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label typeLabel = new Label(attempt.getAttemptType().name().charAt(0) + " "
                + attempt.getAttemptType().name().toLowerCase().replace('_', ' '));
        typeLabel.getStyleClass().add("badge-primary");

        if (attempt.getDifficulty() != null) {
            Label diffLabel = new Label(attempt.getDifficulty().name());
            if ("EASY".equalsIgnoreCase(attempt.getDifficulty().name())) {
                diffLabel.getStyleClass().add("badge-success");
            } else if ("MEDIUM".equalsIgnoreCase(attempt.getDifficulty().name())) {
                diffLabel.getStyleClass().add("badge-warning");
            } else {
                diffLabel.getStyleClass().add("badge-error");
            }
            header.getChildren().add(diffLabel);
        }

        header.getChildren().add(typeLabel);

        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        header.getChildren().add(spacer);

        Label scoreLabel = new Label(scoreText(attempt));
        if (attempt.getScorePercent() != null) {
            double p = attempt.getScorePercent();
            if (p >= 70.0) {
                scoreLabel.getStyleClass().add("badge-success");
            } else if (p >= 50.0) {
                scoreLabel.getStyleClass().add("badge-warning");
            } else {
                scoreLabel.getStyleClass().add("badge-error");
            }
        } else {
            scoreLabel.getStyleClass().add("badge-neutral");
        }
        header.getChildren().add(scoreLabel);

        HBox metaRow = new HBox(16);
        metaRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label dateLabel = new Label("📅 " + dateText(attempt));
        dateLabel.getStyleClass().add("label-helper-small");

        Label detailLabel = new Label("📝 " + attempt.getQuestionCount() + " questions · " + correctText(attempt));
        detailLabel.getStyleClass().add("label-helper-small");

        metaRow.getChildren().addAll(dateLabel, detailLabel);

        getChildren().addAll(header, metaRow);
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
