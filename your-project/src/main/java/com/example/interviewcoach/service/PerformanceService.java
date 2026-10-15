package com.example.interviewcoach.service;

import com.example.interviewcoach.model.Attempt;
import com.example.interviewcoach.model.AttemptAnswer;
import com.example.interviewcoach.model.AttemptQuestion;
import com.example.interviewcoach.repository.AttemptRepository;
import com.example.interviewcoach.repository.AttemptRepositoryAnswers;
import com.example.interviewcoach.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PerformanceService {

    private static final Logger log = LoggerFactory.getLogger(PerformanceService.class);

    private static final int WEAK_AREA_MIN_SAMPLE = 5;
    private static final double WEAK_AREA_THRESHOLD = 50.0;

    private final AttemptRepository attemptRepository;
    private final AttemptRepositoryAnswers answerRepository;
    private final QuestionRepository questionRepository;

    public PerformanceService(AttemptRepository attemptRepository,
                               AttemptRepositoryAnswers answerRepository,
                               QuestionRepository questionRepository) {
        this.attemptRepository = attemptRepository;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    public List<Attempt> getHistory(long userId, int page, int pageSize) {
        int offset = page * pageSize;
        return attemptRepository.findCompletedByUser(userId, offset, pageSize);
    }

    public long getHistoryCount(long userId) {
        return attemptRepository.countCompletedByUser(userId);
    }

    public Optional<AttemptDetails> getAttemptDetails(long userId, long attemptId) {
        Attempt attempt = attemptRepository.findById(attemptId).orElse(null);
        if (attempt == null || attempt.getUserId() != userId) {
            return Optional.empty();
        }

        List<AttemptQuestion> snapshot = questionRepository.listAttemptQuestions(attemptId);
        List<AttemptAnswer> answers = answerRepository.listAttemptAnswers(attemptId);

        List<AnswerOutcome> outcomes = new ArrayList<>(snapshot.size());
        Map<Long, AttemptAnswer> answerByQuestion = new LinkedHashMap<>();
        for (AttemptAnswer a : answers) {
            answerByQuestion.put(a.getQuestionId(), a);
        }

        for (AttemptQuestion sq : snapshot) {
            AttemptAnswer a = answerByQuestion.get(sq.getQuestionId());
            boolean isCorrect = a != null && a.isCorrect();
            outcomes.add(new AnswerOutcome(
                    sq.getQuestionId(), sq.getQuestionOrder(),
                    sq.getPromptSnapshot(),
                    a != null ? a.getSelectedOption() : null,
                    sq.getCorrectOptionSnapshot(),
                    isCorrect,
                    sq.getExplanationSnapshot(),
                    sq.getCategorySnapshot(),
                    sq.getDifficultySnapshot()
            ));
        }

        int correct = (int) outcomes.stream().filter(AnswerOutcome::isCorrect).count();
        int total = outcomes.size();
        int unanswered = (int) outcomes.stream()
                .filter(o -> o.selectedOption() == null)
                .count();
        double percent = total == 0 ? 0.0 : (correct * 100.0) / total;

        Attempt resultAttempt = new Attempt(
                attempt.getId(), attempt.getUserId(), attempt.getAttemptType(),
                attempt.getCategoryId(), attempt.getDifficulty(),
                attempt.getStartedAt(), attempt.getSubmittedAt(),
                attempt.getStatus(), total, correct, percent);

        return Optional.of(new AttemptDetails(resultAttempt, outcomes));
    }

    public List<CategoryPerformance> getCategoryPerformance(long userId) {
        List<Attempt> history = attemptRepository.findCompletedByUser(userId, 0, 500);
        if (history.isEmpty()) {
            return List.of();
        }

        Map<String, CategoryStats> statsByCategory = new LinkedHashMap<>();

        for (Attempt attempt : history) {
            Optional<AttemptDetails> details = getAttemptDetails(userId, attempt.getId());
            if (details.isEmpty()) {
                continue;
            }
            for (AnswerOutcome outcome : details.get().outcomes()) {
                statsByCategory.computeIfAbsent(outcome.category(), k ->
                        new CategoryStats(0, 0)).record(outcome);
            }
        }

        List<CategoryPerformance> result = new ArrayList<>(statsByCategory.size());
        for (Map.Entry<String, CategoryStats> entry : statsByCategory.entrySet()) {
            CategoryStats stats = entry.getValue();
            double percent = stats.answered == 0
                    ? 0.0
                    : (stats.correct * 100.0) / stats.answered;
            result.add(new CategoryPerformance(
                    entry.getKey(),
                    stats.correct,
                    stats.answered,
                    percent,
                    isWeakArea(stats.answered, percent)
            ));
        }

        result.sort((a, b) -> Double.compare(b.accuracyPercent(), a.accuracyPercent()));
        return result;
    }

    private boolean isWeakArea(int answered, double accuracyPercent) {
        return answered >= WEAK_AREA_MIN_SAMPLE && accuracyPercent < WEAK_AREA_THRESHOLD;
    }

    public static class CategoryStats {
        private int correct;
        private int answered;

        public CategoryStats(int correct, int answered) {
            this.correct = correct;
            this.answered = answered;
        }

        public int correct() {
            return correct;
        }

        public int answered() {
            return answered;
        }

        void record(AnswerOutcome outcome) {
            if (outcome.selectedOption() != null) {
                answered++;
                if (outcome.isCorrect()) {
                    correct++;
                }
            }
        }
    }

    public record CategoryPerformance(
            String category,
            int correct,
            int answered,
            double accuracyPercent,
            boolean weakArea
    ) {
    }

    public record AttemptDetails(Attempt attempt, List<AnswerOutcome> outcomes) {
    }
}
