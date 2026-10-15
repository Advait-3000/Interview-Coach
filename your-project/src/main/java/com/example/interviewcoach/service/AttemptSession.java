package com.example.interviewcoach.service;

import com.example.interviewcoach.model.Attempt;
import com.example.interviewcoach.model.AttemptQuestion;
import com.example.interviewcoach.model.AttemptType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AttemptSession {

    private final Attempt attempt;
    private final List<AttemptQuestion> snapshot;
    private final Map<Long, Character> answers;
    private final AttemptType type;

    public AttemptSession(Attempt attempt, List<AttemptQuestion> snapshot,
                           Map<Long, Character> answers, AttemptType type) {
        this.attempt = attempt;
        this.snapshot = snapshot;
        this.answers = new LinkedHashMap<>(answers);
        this.type = type;
    }

    public Attempt getAttempt() {
        return attempt;
    }

    public List<AttemptQuestion> getSnapshot() {
        return snapshot;
    }

    public Map<Long, Character> getAnswers() {
        return new LinkedHashMap<>(answers);
    }

    public AttemptType getType() {
        return type;
    }

    public void setAnswer(long questionId, Character option) {
        answers.put(questionId, option);
    }

    public Character getAnswer(long questionId) {
        return answers.get(questionId);
    }

    public long getCurrentQuestionCount() {
        return snapshot.size();
    }

    public int answeredCount() {
        return (int) answers.values().stream()
                .filter(opt -> opt != null)
                .count();
    }
}
