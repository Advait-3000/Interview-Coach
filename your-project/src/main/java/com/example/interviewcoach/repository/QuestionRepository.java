package com.example.interviewcoach.repository;

import com.example.interviewcoach.model.AttemptQuestion;
import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.model.Category;
import com.example.interviewcoach.model.Difficulty;
import com.example.interviewcoach.model.Question;
import com.example.interviewcoach.service.NotFoundException;
import com.example.interviewcoach.service.QuestionCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionRepository {

    private static final Logger log = LoggerFactory.getLogger(QuestionRepository.class);

    private final ConnectionProvider provider;

    public QuestionRepository(ConnectionProvider provider) {
        this.provider = provider;
    }

    public List<Category> listActiveCategories() {
        String sql = """
                SELECT id, name, description, is_active, created_at, updated_at
                FROM categories
                WHERE is_active = 1
                ORDER BY name
                """;

        List<Category> categories = new ArrayList<>();
        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(mapCategory(rs));
            }
        } catch (SQLException e) {
            log.error("Failed to list active categories", e);
            throw new RepositoryException("Unable to load categories", e);
        }
        return categories;
    }

    public Optional<Category> findCategoryById(long categoryId) {
        String sql = """
                SELECT id, name, description, is_active, created_at, updated_at
                FROM categories
                WHERE id = ?
                """;

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCategory(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find category id={}", categoryId, e);
            throw new RepositoryException("Unable to query category", e);
        }
        return Optional.empty();
    }

    public List<Question> findActiveQuestions(long categoryId, Difficulty difficulty,
                                               int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }

        String sql = """
                SELECT q.id, q.category_id, q.prompt, q.difficulty,
                       q.option_a, q.option_b, q.option_c, q.option_d,
                       q.correct_option, q.explanation, q.is_active,
                       q.created_by, q.created_at, q.updated_at,
                       c.name AS category_name
                FROM questions q
                JOIN categories c ON c.id = q.category_id
                WHERE q.category_id = ?
                  AND q.difficulty = ?
                  AND q.is_active = 1
                ORDER BY q.id
                LIMIT ?
                """;

        List<Question> questions = new ArrayList<>();
        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, categoryId);
            stmt.setString(2, difficulty.name());
            stmt.setInt(3, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    questions.add(mapQuestion(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find active questions", e);
            throw new RepositoryException("Unable to query questions", e);
        }
        return questions;
    }

    public long countActiveQuestionsByCategoryAndDifficulty(long categoryId,
                                                             Difficulty difficulty) {
        String sql = """
                SELECT COUNT(*)
                FROM questions q
                WHERE q.category_id = ?
                  AND q.difficulty = ?
                  AND q.is_active = 1
                """;

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, categoryId);
            stmt.setString(2, difficulty.name());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to count active questions", e);
            throw new RepositoryException("Unable to count questions", e);
        }
        return 0;
    }

    public Optional<Question> findQuestionForAdmin(long questionId) {
        String sql = """
                SELECT q.id, q.category_id, q.prompt, q.difficulty,
                       q.option_a, q.option_b, q.option_c, q.option_d,
                       q.correct_option, q.explanation, q.is_active,
                       q.created_by, q.created_at, q.updated_at,
                       c.name AS category_name
                FROM questions q
                JOIN categories c ON c.id = q.category_id
                WHERE q.id = ?
                """;

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, questionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapQuestion(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find question id={}", questionId, e);
            throw new RepositoryException("Unable to query question", e);
        }
        return Optional.empty();
    }

    public Question createQuestion(QuestionCommand command, AuthenticatedUser actor) {
        String sql = """
                INSERT INTO questions (category_id, prompt, difficulty,
                                       option_a, option_b, option_c, option_d,
                                       correct_option, explanation, is_active,
                                       created_by, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1, ?, NOW(), NOW())
                """;

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id"})) {
            stmt.setLong(1, command.categoryId());
            stmt.setString(2, command.prompt());
            stmt.setString(3, command.difficulty().name());
            stmt.setString(4, command.optionA());
            stmt.setString(5, command.optionB());
            stmt.setString(6, command.optionC());
            stmt.setString(7, command.optionD());
            stmt.setString(8, String.valueOf(command.correctOption()));
            stmt.setString(9, command.explanation());
            if (actor != null) {
                stmt.setLong(10, actor.id());
            } else {
                stmt.setNull(10, java.sql.Types.BIGINT);
            }
            stmt.executeUpdate();

            long id;
            try (ResultSet generated = stmt.getGeneratedKeys()) {
                if (generated.next()) {
                    id = generated.getLong(1);
                } else {
                    throw new RepositoryException("Could not retrieve generated question id");
                }
            }

            log.info("Created question id={}", id);
            return findQuestionForAdmin(id).orElseThrow();
        } catch (SQLException e) {
            log.error("Failed to create question", e);
            throw new RepositoryException("Unable to create question", e);
        }
    }

    public Question updateQuestion(long questionId, QuestionCommand command,
                                   AuthenticatedUser actor) {
        String sql = """
                UPDATE questions
                SET category_id = ?, prompt = ?, difficulty = ?,
                    option_a = ?, option_b = ?, option_c = ?, option_d = ?,
                    correct_option = ?, explanation = ?, updated_at = NOW()
                WHERE id = ?
                """;

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, command.categoryId());
            stmt.setString(2, command.prompt());
            stmt.setString(3, command.difficulty().name());
            stmt.setString(4, command.optionA());
            stmt.setString(5, command.optionB());
            stmt.setString(6, command.optionC());
            stmt.setString(7, command.optionD());
            stmt.setString(8, String.valueOf(command.correctOption()));
            stmt.setString(9, command.explanation());
            stmt.setLong(10, questionId);
            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new NotFoundException("Question not found");
            }
        } catch (SQLException e) {
            log.error("Failed to update question id={}", questionId, e);
            throw new RepositoryException("Unable to update question", e);
        }
        return findQuestionForAdmin(questionId).orElseThrow();
    }

    public void deactivateQuestion(long questionId, AuthenticatedUser actor) {
        String sql = """
                UPDATE questions
                SET is_active = 0, updated_at = NOW()
                WHERE id = ?
                """;

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, questionId);
            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new NotFoundException("Question not found");
            }
        } catch (SQLException e) {
            log.error("Failed to deactivate question id={}", questionId, e);
            throw new RepositoryException("Unable to deactivate question", e);
        }
    }

    public void deleteQuestion(long questionId, AuthenticatedUser actor) {
        String sql = "DELETE FROM questions WHERE id = ?";
        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, questionId);
            int deleted = stmt.executeUpdate();
            if (deleted == 0) {
                throw new NotFoundException("Question not found");
            }
        } catch (SQLException e) {
            log.error("Failed to delete question id={}", questionId, e);
            throw new RepositoryException("Unable to delete question", e);
        }
    }

    public List<AttemptQuestion> snapshotAttemptQuestions(long attemptId,
                                                           List<Question> questions) {
        if (questions.isEmpty()) {
            return List.of();
        }

        String sql = """
                INSERT INTO attempt_questions (attempt_id, question_id, question_order,
                                               prompt_snapshot, option_a_snapshot,
                                               option_b_snapshot, option_c_snapshot,
                                               option_d_snapshot, correct_option_snapshot,
                                               explanation_snapshot, category_snapshot,
                                               difficulty_snapshot)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        List<AttemptQuestion> snapshot = new ArrayList<>();
        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql,
                     new String[]{"id"})) {
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                stmt.setLong(1, attemptId);
                stmt.setLong(2, q.getId());
                stmt.setInt(3, i + 1);
                stmt.setString(4, q.getPrompt());
                stmt.setString(5, q.getOptionA());
                stmt.setString(6, q.getOptionB());
                stmt.setString(7, q.getOptionC());
                stmt.setString(8, q.getOptionD());
                stmt.setString(9, String.valueOf(q.getCorrectOption()));
                stmt.setString(10, q.getExplanation());
                stmt.setString(11, q.getCategoryName());
                stmt.setString(12, q.getDifficulty().name());
                stmt.addBatch();
            }
            stmt.executeBatch();

            try (ResultSet generated = stmt.getGeneratedKeys()) {
                while (generated.next()) {
                    long snapshotId = generated.getLong(1);
                    Question q = questions.get((int) (snapshotId % questions.size()));
                    snapshot.add(new AttemptQuestion(
                            snapshotId, attemptId, q.getId(),
                            (int) (snapshotId % questions.size()) + 1,
                            q.getPrompt(), q.getOptionA(), q.getOptionB(),
                            q.getOptionC(), q.getOptionD(),
                            q.getCorrectOption(), q.getExplanation(),
                            q.getCategoryName(), q.getDifficulty()
                    ));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to snapshot attempt questions for attemptId={}", attemptId, e);
            throw new RepositoryException("Unable to snapshot attempt questions", e);
        }
        return snapshot;
    }

    public List<AttemptQuestion> listAttemptQuestions(long attemptId) {
        String sql = """
                SELECT id, attempt_id, question_id, question_order,
                       prompt_snapshot, option_a_snapshot, option_b_snapshot,
                       option_c_snapshot, option_d_snapshot, correct_option_snapshot,
                       explanation_snapshot, category_snapshot, difficulty_snapshot
                FROM attempt_questions
                WHERE attempt_id = ?
                ORDER BY question_order
                """;

        List<AttemptQuestion> questions = new ArrayList<>();
        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, attemptId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    questions.add(mapAttemptQuestion(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to list attempt questions for attemptId={}", attemptId, e);
            throw new RepositoryException("Unable to query attempt questions", e);
        }
        return questions;
    }

    private Category mapCategory(ResultSet rs) throws SQLException {
        return new Category(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("is_active"),
                rs.getTimestamp("created_at") != null
                        ? rs.getTimestamp("created_at").toLocalDateTime()
                        : null,
                rs.getTimestamp("updated_at") != null
                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                        : null
        );
    }

    private Question mapQuestion(ResultSet rs) throws SQLException {
        return new Question(
                rs.getLong("id"),
                rs.getLong("category_id"),
                rs.getString("prompt"),
                Difficulty.valueOf(rs.getString("difficulty")),
                rs.getString("option_a"),
                rs.getString("option_b"),
                rs.getString("option_c"),
                rs.getString("option_d"),
                rs.getString("correct_option").charAt(0),
                rs.getString("explanation"),
                rs.getBoolean("is_active"),
                rs.getObject("created_by") != null
                        ? rs.getLong("created_by")
                        : null,
                rs.getTimestamp("created_at") != null
                        ? rs.getTimestamp("created_at").toLocalDateTime()
                        : null,
                rs.getTimestamp("updated_at") != null
                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                        : null,
                rs.getString("category_name")
        );
    }

    private AttemptQuestion mapAttemptQuestion(ResultSet rs) throws SQLException {
        return new AttemptQuestion(
                rs.getLong("id"),
                rs.getLong("attempt_id"),
                rs.getLong("question_id"),
                rs.getInt("question_order"),
                rs.getString("prompt_snapshot"),
                rs.getString("option_a_snapshot"),
                rs.getString("option_b_snapshot"),
                rs.getString("option_c_snapshot"),
                rs.getString("option_d_snapshot"),
                rs.getString("correct_option_snapshot").charAt(0),
                rs.getString("explanation_snapshot"),
                rs.getString("category_snapshot"),
                Difficulty.valueOf(rs.getString("difficulty_snapshot"))
        );
    }
}
