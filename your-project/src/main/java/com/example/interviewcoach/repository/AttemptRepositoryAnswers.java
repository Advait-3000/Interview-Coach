package com.example.interviewcoach.repository;

import com.example.interviewcoach.model.AttemptAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AttemptRepositoryAnswers {

    private static final Logger log = LoggerFactory.getLogger(AttemptRepositoryAnswers.class);

    private final ConnectionProvider provider;

    public AttemptRepositoryAnswers(ConnectionProvider provider) {
        this.provider = provider;
    }

    public List<AttemptAnswer> listAttemptAnswers(long attemptId) {
        String sql = """
                SELECT id, attempt_id, question_id, selected_option, is_correct, answered_at
                FROM attempt_answers
                WHERE attempt_id = ?
                ORDER BY question_id
                """;

        List<AttemptAnswer> answers = new ArrayList<>();
        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, attemptId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    answers.add(mapAnswer(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to list answers for attemptId={}", attemptId, e);
            throw new RepositoryException("Unable to query answers", e);
        }
        return answers;
    }

    private AttemptAnswer mapAnswer(ResultSet rs) throws SQLException {
        return new AttemptAnswer(
                rs.getLong("id"),
                rs.getLong("attempt_id"),
                rs.getLong("question_id"),
                rs.getString("selected_option") != null
                        ? rs.getString("selected_option").charAt(0) : null,
                rs.getBoolean("is_correct"),
                rs.getTimestamp("answered_at") != null
                        ? rs.getTimestamp("answered_at").toLocalDateTime() : null
        );
    }
}
