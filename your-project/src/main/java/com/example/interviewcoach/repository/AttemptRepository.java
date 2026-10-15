package com.example.interviewcoach.repository;

import com.example.interviewcoach.model.Attempt;
import com.example.interviewcoach.model.AttemptAnswer;
import com.example.interviewcoach.model.AttemptQuestion;
import com.example.interviewcoach.model.AttemptStatus;
import com.example.interviewcoach.model.AttemptType;
import com.example.interviewcoach.model.Difficulty;
import com.example.interviewcoach.service.AttemptResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AttemptRepository {

    private static final Logger log = LoggerFactory.getLogger(AttemptRepository.class);

    private final ConnectionProvider provider;

    public AttemptRepository(ConnectionProvider provider) {
        this.provider = provider;
    }

    public Optional<Attempt> findById(long attemptId) {
        String sql = """
                SELECT id, user_id, attempt_type, category_id, difficulty,
                       started_at, submitted_at, status, question_count,
                       correct_count, score_percent
                FROM attempts
                WHERE id = ?
                """;

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, attemptId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapAttempt(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find attempt id={}", attemptId, e);
            throw new RepositoryException("Unable to query attempt", e);
        }
        return Optional.empty();
    }

    public List<Attempt> findCompletedByUser(long userId, int offset, int limit) {
        if (limit <= 0 || offset < 0) {
            throw new IllegalArgumentException("Invalid pagination");
        }

        String sql = """
                SELECT id, user_id, attempt_type, category_id, difficulty,
                       started_at, submitted_at, status, question_count,
                       correct_count, score_percent
                FROM attempts
                WHERE user_id = ?
                  AND status = 'COMPLETED'
                ORDER BY submitted_at DESC
                LIMIT ? OFFSET ?
                """;

        List<Attempt> attempts = new ArrayList<>();
        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    attempts.add(mapAttempt(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to list attempts for user id={}", userId, e);
            throw new RepositoryException("Unable to query attempts", e);
        }
        return attempts;
    }

    public long countCompletedByUser(long userId) {
        String sql = """
                SELECT COUNT(*)
                FROM attempts
                WHERE user_id = ? AND status = 'COMPLETED'
                """;

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to count attempts for user id={}", userId, e);
            throw new RepositoryException("Unable to count attempts", e);
        }
        return 0;
    }

    public Attempt startAttempt(long userId, AttemptType type, Long categoryId,
                                 Difficulty difficulty, int questionCount) {
        String sql = """
                INSERT INTO attempts (user_id, attempt_type, category_id, difficulty,
                                      question_count, correct_count, status, started_at)
                VALUES (?, ?, ?, ?, ?, 0, 'IN_PROGRESS', NOW())
                """;

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql,
                     new String[]{"id"})) {
            stmt.setLong(1, userId);
            stmt.setString(2, type.name());
            if (categoryId != null) {
                stmt.setLong(3, categoryId);
            } else {
                stmt.setNull(3, java.sql.Types.BIGINT);
            }
            if (difficulty != null) {
                stmt.setString(4, difficulty.name());
            } else {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            }
            stmt.setInt(5, questionCount);
            stmt.executeUpdate();

            long id;
            try (ResultSet generated = stmt.getGeneratedKeys()) {
                if (generated.next()) {
                    id = generated.getLong(1);
                } else {
                    throw new RepositoryException("Could not retrieve generated attempt id");
                }
            }

            log.info("Started attempt id={} userId={}", id, userId);
            return new Attempt(id, userId, type, categoryId, difficulty,
                    LocalDateTime.now(), null, AttemptStatus.IN_PROGRESS,
                    questionCount, 0, null);
        } catch (SQLException e) {
            log.error("Failed to start attempt for user id={}", userId, e);
            throw new RepositoryException("Unable to start attempt", e);
        }
    }

    public void saveAnswer(long attemptId, long questionId, Character selectedOption) {
        String sql = """
                INSERT INTO attempt_answers (attempt_id, question_id, selected_option, answered_at)
                VALUES (?, ?, ?, NOW())
                ON DUPLICATE KEY UPDATE selected_option = VALUES(selected_option),
                                      answered_at = NOW()
                """;

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, attemptId);
            stmt.setLong(2, questionId);
            if (selectedOption != null) {
                stmt.setString(3, String.valueOf(selectedOption));
            } else {
                stmt.setNull(3, java.sql.Types.CHAR);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to save answer attemptId={} questionId={}", attemptId, questionId, e);
            throw new RepositoryException("Unable to save answer", e);
        }
    }

    public void finalizeAttempt(long attemptId, Map<Long, Character> answers,
                                 AttemptResult result) {
        // Single transaction for attempt update and answer insert.
        String updateSql = """
                UPDATE attempts
                SET status = 'COMPLETED',
                    submitted_at = NOW(),
                    correct_count = ?,
                    score_percent = ?
                WHERE id = ?
                """;

        String answerSql = """
                INSERT INTO attempt_answers (attempt_id, question_id, selected_option, is_correct, answered_at)
                VALUES (?, ?, ?, ?, NOW())
                ON DUPLICATE KEY UPDATE selected_option = VALUES(selected_option),
                                      is_correct = VALUES(is_correct),
                                      answered_at = VALUES(answered_at)
                """;

        try (Connection conn = provider.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int correctCount = result.correctCount();
                double scorePercent = result.scorePercent();

                try (PreparedStatement upd = conn.prepareStatement(updateSql)) {
                    upd.setInt(1, correctCount);
                    upd.setDouble(2, scorePercent);
                    upd.setLong(3, attemptId);
                    int updated = upd.executeUpdate();
                    if (updated == 0) {
                        throw new IllegalStateException("Attempt not found during finalization");
                    }
                }

                try (PreparedStatement ins = conn.prepareStatement(answerSql)) {
                    for (Map.Entry<Long, Character> entry : answers.entrySet()) {
                        Long questionId = entry.getKey();
                        Character selected = entry.getValue();
                        boolean isCorrect = result.answerRecords().get(questionId).isCorrect();
                        ins.setLong(1, attemptId);
                        ins.setLong(2, questionId);
                        if (selected != null) {
                            ins.setString(3, String.valueOf(selected));
                        } else {
                            ins.setNull(3, java.sql.Types.CHAR);
                        }
                        ins.setBoolean(4, isCorrect);
                        ins.addBatch();
                    }
                    ins.executeBatch();
                }

                conn.commit();
                log.info("Finalized attempt id={} correct={} score={}%",
                        attemptId, correctCount, Math.round(scorePercent));
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackIgnored) {
                    log.warn("Rollback failed for attempt id={}", attemptId, rollbackIgnored);
                }
                log.error("Failed to finalize attempt id={}", attemptId, e);
                throw new RepositoryException("Unable to finalize attempt", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            log.error("Failed to finalize attempt id={}", attemptId, e);
            throw new RepositoryException("Unable to finalize attempt", e);
        }
    }

    public List<AttemptQuestion> listAttemptQuestions(long attemptId) {
        // Implemented in QuestionRepository for shared mapping; delegated here for unity.
        throw new UnsupportedOperationException("Use QuestionRepository.snapshotAttemptQuestions / listAttemptQuestions");
    }

    private Attempt mapAttempt(ResultSet rs) throws SQLException {
        Long categoryId = rs.getObject("category_id") != null
                ? rs.getLong("category_id") : null;
        Difficulty difficulty = rs.getObject("difficulty") != null
                ? Difficulty.valueOf(rs.getString("difficulty")) : null;
        return new Attempt(
                rs.getLong("id"),
                rs.getLong("user_id"),
                AttemptType.valueOf(rs.getString("attempt_type")),
                categoryId,
                difficulty,
                rs.getTimestamp("started_at") != null
                        ? rs.getTimestamp("started_at").toLocalDateTime()
                        : null,
                rs.getTimestamp("submitted_at") != null
                        ? rs.getTimestamp("submitted_at").toLocalDateTime()
                        : null,
                AttemptStatus.valueOf(rs.getString("status")),
                rs.getInt("question_count"),
                rs.getInt("correct_count"),
                rs.getObject("score_percent") != null
                        ? rs.getDouble("score_percent") : null
        );
    }
}
