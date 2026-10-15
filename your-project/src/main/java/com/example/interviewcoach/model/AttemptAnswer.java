package com.example.interviewcoach.model;

import java.time.LocalDateTime;

public class AttemptAnswer {

    private final long id;
    private final long attemptId;
    private final long questionId;
    private final Character selectedOption;
    private final boolean isCorrect;
    private final LocalDateTime answeredAt;

    public AttemptAnswer(long id, long attemptId, long questionId,
                         Character selectedOption, boolean isCorrect,
                         LocalDateTime answeredAt) {
        this.id = id;
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.selectedOption = selectedOption;
        this.isCorrect = isCorrect;
        this.answeredAt = answeredAt;
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

    public Character getSelectedOption() {
        return selectedOption;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }
}
