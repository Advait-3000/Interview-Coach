package com.example.interviewcoach.service;

import com.example.interviewcoach.model.Difficulty;

public record AnswerOutcome(
        long questionId,
        int questionOrder,
        String prompt,
        Character selectedOption,
        char correctOption,
        boolean isCorrect,
        String explanation,
        String category,
        Difficulty difficulty
) {
    public String selectedOptionText() {
        if (selectedOption == null) {
            return "Not answered";
        }
        return String.valueOf(selectedOption);
    }

    public String correctOptionText() {
        return String.valueOf(correctOption);
    }
}
