package com.example.interviewcoach.model;

import java.time.LocalDateTime;

public class AttemptQuestion {

    private final long id;
    private final long attemptId;
    private final long questionId;
    private final int questionOrder;
    private final String promptSnapshot;
    private final String optionASnapshot;
    private final String optionBSnapshot;
    private final String optionCSnapshot;
    private final String optionDSnapshot;
    private final char correctOptionSnapshot;
    private final String explanationSnapshot;
    private final String categorySnapshot;
    private final Difficulty difficultySnapshot;

    public AttemptQuestion(long id, long attemptId, long questionId, int questionOrder,
                           String promptSnapshot, String optionASnapshot,
                           String optionBSnapshot, String optionCSnapshot,
                           String optionDSnapshot, char correctOptionSnapshot,
                           String explanationSnapshot, String categorySnapshot,
                           Difficulty difficultySnapshot) {
        this.id = id;
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.questionOrder = questionOrder;
        this.promptSnapshot = promptSnapshot;
        this.optionASnapshot = optionASnapshot;
        this.optionBSnapshot = optionBSnapshot;
        this.optionCSnapshot = optionCSnapshot;
        this.optionDSnapshot = optionDSnapshot;
        this.correctOptionSnapshot = correctOptionSnapshot;
        this.explanationSnapshot = explanationSnapshot;
        this.categorySnapshot = categorySnapshot;
        this.difficultySnapshot = difficultySnapshot;
    }

    public long getId() {
        return id;
    }

    public long getAttemptId() {
        return attemptId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public int getQuestionOrder() {
        return questionOrder;
    }

    public String getPromptSnapshot() {
        return promptSnapshot;
    }

    public String getOptionASnapshot() {
        return optionASnapshot;
    }

    public String getOptionBSnapshot() {
        return optionBSnapshot;
    }

    public String getOptionCSnapshot() {
        return optionCSnapshot;
    }

    public String getOptionDSnapshot() {
        return optionDSnapshot;
    }

    public char getCorrectOptionSnapshot() {
        return correctOptionSnapshot;
    }

    public String getExplanationSnapshot() {
        return explanationSnapshot;
    }

    public String getCategorySnapshot() {
        return categorySnapshot;
    }

    public Difficulty getDifficultySnapshot() {
        return difficultySnapshot;
    }

    public String getOptionSnapshot(char option) {
        return switch (option) {
            case 'A' -> optionASnapshot;
            case 'B' -> optionBSnapshot;
            case 'C' -> optionCSnapshot;
            case 'D' -> optionDSnapshot;
            default -> null;
        };
    }
}
