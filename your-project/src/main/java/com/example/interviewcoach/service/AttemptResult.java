package com.example.interviewcoach.service;

import com.example.interviewcoach.model.Attempt;
import com.example.interviewcoach.model.AttemptAnswer;
import com.example.interviewcoach.model.AttemptQuestion;

import java.util.List;
import java.util.Map;

public record AttemptResult(
        Attempt attempt,
        List<AnswerOutcome> outcomes,
        Map<Long, AttemptAnswer> answerRecords,
        int correctCount,
        int totalCount,
        int unansweredCount,
        double scorePercent
) {
    public int incorrectCount() {
        return totalCount - correctCount - unansweredCount;
    }
}
