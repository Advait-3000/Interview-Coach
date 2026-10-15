package com.example.interviewcoach.service;

import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.model.Category;
import com.example.interviewcoach.model.Difficulty;
import com.example.interviewcoach.model.Question;
import com.example.interviewcoach.model.UserRole;
import com.example.interviewcoach.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Category> listActiveCategories() {
        return questionRepository.listActiveCategories();
    }

    public Optional<Category> findCategoryById(long categoryId) {
        return questionRepository.findCategoryById(categoryId);
    }

    public List<Question> findActiveQuestions(long categoryId, Difficulty difficulty,
                                               int limit) {
        return questionRepository.findActiveQuestions(categoryId, difficulty, limit);
    }

    public long countActiveQuestionsByCategoryAndDifficulty(long categoryId,
                                                             Difficulty difficulty) {
        return questionRepository.countActiveQuestionsByCategoryAndDifficulty(categoryId,
                                                                              difficulty);
    }

    public Optional<Question> findQuestionForAdmin(long questionId) {
        return questionRepository.findQuestionForAdmin(questionId);
    }

    public Question createQuestion(QuestionCommand command, AuthenticatedUser actor) {
        requireAdmin(actor);
        validateQuestionCommand(command);

        Question created = questionRepository.createQuestion(command, actor);
        log.info("Admin id={} created question id={}", actor.id(), created.getId());
        return created;
    }

    public Question updateQuestion(long questionId, QuestionCommand command,
                                   AuthenticatedUser actor) {
        requireAdmin(actor);
        validateQuestionCommand(command);

        Question existing = requireExistingQuestion(questionId);
        Question updated = questionRepository.updateQuestion(questionId, command, actor);
        log.info("Admin id={} updated question id={}", actor.id(), updated.getId());
        return updated;
    }

    public void deactivateQuestion(long questionId, AuthenticatedUser actor) {
        requireAdmin(actor);
        questionRepository.findQuestionForAdmin(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found"));
        questionRepository.deactivateQuestion(questionId, actor);
        log.info("Admin id={} deactivated question id={}", actor.id(), questionId);
    }

    public void deleteQuestion(long questionId, AuthenticatedUser actor) {
        requireAdmin(actor);
        Question existing = requireExistingQuestion(questionId);
        questionRepository.deleteQuestion(questionId, actor);
        log.info("Admin id={} deleted question id={}", actor.id(), questionId);
    }

    private Question requireExistingQuestion(long questionId) {
        return questionRepository.findQuestionForAdmin(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found"));
    }

    private void requireAdmin(AuthenticatedUser actor) {
        if (actor == null || actor.role() != UserRole.ADMIN) {
            throw new AuthorizationException("Admin access is required");
        }
    }

    private void validateQuestionCommand(QuestionCommand command) {
        if (command.categoryId() <= 0) {
            throw new ValidationException("Category is required");
        }
        if (command.prompt() == null || command.prompt().isBlank()) {
            throw new ValidationException("Question prompt is required");
        }
        if (command.difficulty() == null) {
            throw new ValidationException("Difficulty is required");
        }
        if (command.getOption('A') == null || command.getOption('A').isBlank()) {
            throw new ValidationException("Option A is required");
        }
        if (command.getOption('B') == null || command.getOption('B').isBlank()) {
            throw new ValidationException("Option B is required");
        }
        if (command.getOption('C') == null || command.getOption('C').isBlank()) {
            throw new ValidationException("Option C is required");
        }
        if (command.getOption('D') == null || command.getOption('D').isBlank()) {
            throw new ValidationException("Option D is required");
        }
        if (!List.of('A', 'B', 'C', 'D').contains(command.correctOption())) {
            throw new ValidationException("Correct option must be A, B, C, or D");
        }
    }
}
