package com.example.interviewcoach.service;

import com.example.interviewcoach.model.Attempt;
import com.example.interviewcoach.model.AttemptAnswer;
import com.example.interviewcoach.model.AttemptQuestion;
import com.example.interviewcoach.model.AttemptStatus;
import com.example.interviewcoach.model.AttemptType;
import com.example.interviewcoach.model.Category;
import com.example.interviewcoach.model.Difficulty;
import com.example.interviewcoach.model.Question;
import com.example.interviewcoach.repository.AttemptRepository;
import com.example.interviewcoach.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AssessmentService {

    private static final Logger log = LoggerFactory.getLogger(AssessmentService.class);

    private final AttemptRepository attemptRepository;
    private final QuestionRepository questionRepository;

    public AssessmentService(AttemptRepository attemptRepository,
                             QuestionRepository questionRepository) {
        this.attemptRepository = attemptRepository;
        this.questionRepository = questionRepository;
    }

    public AttemptSession startAttempt(long userId, AttemptType type, Long categoryId,
                                        Difficulty difficulty, int questionCount) {
        if (questionCount <= 0) {
            throw new IllegalArgumentException("At least one question is required");
        }

        List<Question> questions = questionRepository.findActiveQuestions(
                categoryId, difficulty, questionCount);
        if (questions.isEmpty()) {
            throw new NoQuestionsAvailableException(
                    "No active questions match the selected filters");
        }

        Attempt attempt = attemptRepository.startAttempt(userId, type, categoryId,
                difficulty, questions.size());
        List<AttemptQuestion> snapshot = questionRepository.snapshotAttemptQuestions(
                attempt.getId(), questions);

        Map<Long, Character> answers = new LinkedHashMap<>();
        for (AttemptQuestion sq : snapshot) {
            answers.put(sq.getQuestionId(), null);
        }

        log.info("Started attempt id={} userId={} type={} questions={}",
                attempt.getId(), userId, type, questions.size());
        return new AttemptSession(attempt, snapshot, answers, type);
    }

    public void saveAnswer(long attemptId, long questionId, Character selectedOption) {
        attemptRepository.saveAnswer(attemptId, questionId, selectedOption);
        log.debug("Saved answer attemptId={} questionId={} option={}",
                attemptId, questionId, selectedOption);
    }

    public AttemptResult submitAttempt(long attemptId, long userId,
                                        Map<Long, Character> answers) {
        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt not found"));

        if (attempt.getUserId() != userId) {
            throw new AuthorizationException("Not authorized for this attempt");
        }

        if (attempt.getStatus() == AttemptStatus.COMPLETED) {
            throw new IllegalStateException("Attempt is already completed");
        }

        List<AttemptQuestion> snapshot = attemptRepository.listAttemptQuestions(attemptId);
        if (snapshot.isEmpty()) {
            throw new IllegalStateException("Attempt has no question snapshot");
        }

        Map<Long, Character> bestAnswers = new LinkedHashMap<>(answers);
        for (AttemptQuestion sq : snapshot) {
            bestAnswers.putIfAbsent(sq.getQuestionId(), null);
        }

        AttemptResult result = computeResult(attempt, snapshot, bestAnswers);

        attemptRepository.finalizeAttempt(attemptId, bestAnswers, result);

        log.info("Completed attempt id={} userId={} score={}",
                attemptId, userId, result.scorePercent());
        return result;
    }

    private AttemptResult computeResult(Attempt attempt,
                                         List<AttemptQuestion> snapshot,
                                         Map<Long, Character> answers) {
        int total = snapshot.size();
        int correct = 0;
        int unanswered = 0;
        Map<Long, AttemptAnswer> answerRecords = new LinkedHashMap<>();
        List<AnswerOutcome> outcomes = new java.util.ArrayList<>(total);

        for (AttemptQuestion sq : snapshot) {
            Character selected = answers.get(sq.getQuestionId());
            boolean answered = selected != null;
            boolean isCorrect = answered && selected == sq.getCorrectOptionSnapshot();
            if (!answered) {
                unanswered++;
            } else if (isCorrect) {
                correct++;
            }

            AttemptAnswer record = new AttemptAnswer(
                    0, attempt.getId(), sq.getQuestionId(),
                    selected, isCorrect, java.time.LocalDateTime.now());
            answerRecords.put(sq.getQuestionId(), record);
            outcomes.add(new AnswerOutcome(sq.getQuestionId(), sq.getQuestionOrder(),
                    sq.getPromptSnapshot(), selected,
                    sq.getCorrectOptionSnapshot(), isCorrect,
                    sq.getExplanationSnapshot(), sq.getCategorySnapshot(),
                    sq.getDifficultySnapshot()));
        }

        double percent = total == 0 ? 0.0 : (correct * 100.0) / total;

        Attempt updated = new Attempt(
                attempt.getId(), attempt.getUserId(), attempt.getAttemptType(),
                attempt.getCategoryId(), attempt.getDifficulty(),
                attempt.getStartedAt(), java.time.LocalDateTime.now(),
                AttemptStatus.COMPLETED, total, correct, percent);

        return new AttemptResult(updated, outcomes, answerRecords, correct, total,
                unanswered, percent);
    }

    /**
     * Computes the expected score without persisting anything. Useful for
     * preview and test verification.
     */
    public AttemptResult computeExpectedResult(List<AttemptQuestion> snapshot,
                                                Map<Long, Character> answers) {
        Attempt fake = new Attempt(0, 0, AttemptType.MOCK_TEST, null, null,
                java.time.LocalDateTime.now(), null, AttemptStatus.COMPLETED,
                snapshot.size(), 0, 0.0);
        return computeResult(fake, snapshot, answers);
    }
}
