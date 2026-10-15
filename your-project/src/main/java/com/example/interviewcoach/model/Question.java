package com.example.interviewcoach.model;

import java.time.LocalDateTime;

public class Question {

    private final long id;
    private final long categoryId;
    private final String prompt;
    private final Difficulty difficulty;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;
    private final char correctOption;
    private final String explanation;
    private final boolean isActive;
    private final Long createdBy;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String categoryName;

    public Question(long id, long categoryId, String prompt, Difficulty difficulty,
                    String optionA, String optionB, String optionC, String optionD,
                    char correctOption, String explanation, boolean isActive,
                    Long createdBy, LocalDateTime createdAt, LocalDateTime updatedAt,
                    String categoryName) {
        this.id = id;
        this.categoryId = categoryId;
        this.prompt = prompt;
        this.difficulty = difficulty;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.explanation = explanation;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryName = categoryName;
    }

    public long getId() {
        return id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getPrompt() {
        return prompt;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public char getCorrectOption() {
        return correctOption;
    }

    public String getExplanation() {
        return explanation;
    }

    public boolean isActive() {
        return isActive;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getOption(char option) {
        return switch (option) {
            case 'A' -> optionA;
            case 'B' -> optionB;
            case 'C' -> optionC;
            case 'D' -> optionD;
            default -> null;
        };
    }
}
