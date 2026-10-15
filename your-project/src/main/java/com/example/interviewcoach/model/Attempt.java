package com.example.interviewcoach.model;

import java.time.LocalDateTime;
import java.util.List;

public class Attempt {

    private final long id;
    private final long userId;
    private final AttemptType attemptType;
    private final Long categoryId;
    private final Difficulty difficulty;
    private final LocalDateTime startedAt;
    private final LocalDateTime submittedAt;
    private final AttemptStatus status;
    private final int questionCount;
    private final int correctCount;
    private final Double scorePercent;

    public Attempt(long id, long userId, AttemptType attemptType, Long categoryId,
                   Difficulty difficulty, LocalDateTime startedAt,
                   LocalDateTime submittedAt, AttemptStatus status,
                   int questionCount, int correctCount, Double scorePercent) {
        this.id = id;
        this.userId = userId;
        this.attemptType = attemptType;
        this.categoryId = categoryId;
        this.difficulty = difficulty;
        this.startedAt = startedAt;
        this.submittedAt = submittedAt;
        this.status = status;
        this.questionCount = questionCount;
        this.correctCount = correctCount;
        this.scorePercent = scorePercent;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public AttemptType getAttemptType() {
        return attemptType;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public AttemptStatus getStatus() {
        return status;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public Double getScorePercent() {
        return scorePercent;
    }

    public boolean isCompleted() {
        return AttemptStatus.COMPLETED == status;
    }

    public List<AttemptQuestion> getQuestionSnapshot() {
        return List.of();
    }
}
