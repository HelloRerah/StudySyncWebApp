package studysync.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import studysync.model.QuizAttempt;
import studysync.util.DBUtil;

public class QuizAttemptDAO {

    public void insert(QuizAttempt attempt) throws SQLException {
        String sql = "INSERT INTO quiz_attempt (node_id, student_id, score, passed) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, attempt.getNodeId());
            ps.setInt(2, attempt.getStudentId());
            ps.setInt(3, attempt.getScore());
            ps.setBoolean(4, attempt.isPassed());
            ps.executeUpdate();
        }
    }

    public boolean hasPassed(int nodeId, int studentId) throws SQLException {
        String sql = "SELECT 1 FROM quiz_attempt "
                   + "WHERE node_id = ? AND student_id = ? AND passed = TRUE";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nodeId);
            ps.setInt(2, studentId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public int getBestScore(int nodeId, int studentId) throws SQLException {
        String sql = "SELECT MAX(score) FROM quiz_attempt "
                   + "WHERE node_id = ? AND student_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nodeId);
            ps.setInt(2, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int score = rs.getInt(1);
                return rs.wasNull() ? 0 : score;
            }
        }
        return 0;
    }

    public int getAttemptCount(int nodeId, int studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM quiz_attempt "
                   + "WHERE node_id = ? AND student_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nodeId);
            ps.setInt(2, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}