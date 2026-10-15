package com.example.interviewcoach.service;

import com.example.interviewcoach.model.Difficulty;

public record QuestionCommand(
        long categoryId,
        String prompt,
        Difficulty difficulty,
        String optionA,
        String optionB,
        String optionC,
        String optionD,
        char correctOption,
        String explanation
) {
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
