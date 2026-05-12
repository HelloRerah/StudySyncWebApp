package studysync.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import studysync.model.Assessment;
import studysync.util.DBUtil;

public class AssessmentDAO {

    public List<Assessment> findByStudent(int studentId) throws SQLException {
        List<Assessment> list = new ArrayList<>();
        String sql = "SELECT a.*, s.subject_code, s.subject_name "
                   + "FROM assessment a "
                   + "JOIN subject_enrollment e ON a.enrollment_id = e.enrollment_id "
                   + "JOIN subject s ON e.subject_id = s.subject_id "
                   + "WHERE e.student_id = ? "
                   + "ORDER BY a.due_date ASC";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Assessment> findUpcoming(int studentId) throws SQLException {
        List<Assessment> list = new ArrayList<>();
        String sql = "SELECT a.*, s.subject_code, s.subject_name "
                   + "FROM assessment a "
                   + "JOIN subject_enrollment e ON a.enrollment_id = e.enrollment_id "
                   + "JOIN subject s ON e.subject_id = s.subject_id "
                   + "WHERE e.student_id = ? "
                   + "AND a.due_date >= CURRENT_TIMESTAMP "
                   + "ORDER BY a.due_date ASC";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Assessment> findMarked(int enrollmentId) throws SQLException {
        List<Assessment> list = new ArrayList<>();
        String sql = "SELECT a.*, s.subject_code, s.subject_name "
                   + "FROM assessment a "
                   + "JOIN subject_enrollment e ON a.enrollment_id = e.enrollment_id "
                   + "JOIN subject s ON e.subject_id = s.subject_id "
                   + "WHERE a.enrollment_id = ? AND a.score IS NOT NULL";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public void insert(Assessment assessment) throws SQLException {
        String sql = "INSERT INTO assessment "
                   + "(enrollment_id, title, assessment_type, weight, score, due_date, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, assessment.getEnrollmentId());
            ps.setString(2, assessment.getTitle());
            ps.setString(3, assessment.getAssessmentType());
            ps.setBigDecimal(4, assessment.getWeight());
            ps.setBigDecimal(5, assessment.getScore());
            ps.setTimestamp(6, assessment.getDueDate());
            ps.setString(7, assessment.getStatus());
            ps.executeUpdate();
        }
    }

    public int countDueThisWeek(int studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM assessment a "
                   + "JOIN subject_enrollment e ON a.enrollment_id = e.enrollment_id "
                   + "WHERE e.student_id = ? "
                   + "AND a.due_date >= CURRENT_TIMESTAMP "
                   + "AND a.due_date <= {fn TIMESTAMPADD(SQL_TSI_DAY, 7, CURRENT_TIMESTAMP)}";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Assessment mapRow(ResultSet rs) throws SQLException {
        Assessment a = new Assessment();
        a.setAssessmentId(rs.getInt("assessment_id"));
        a.setEnrollmentId(rs.getInt("enrollment_id"));
        a.setTitle(rs.getString("title"));
        a.setAssessmentType(rs.getString("assessment_type"));
        a.setWeight(rs.getBigDecimal("weight"));
        a.setScore(rs.getBigDecimal("score"));
        a.setDueDate(rs.getTimestamp("due_date"));
        a.setStatus(rs.getString("status"));
        a.setCreatedAt(rs.getTimestamp("created_at"));
        a.setSubjectCode(rs.getString("subject_code"));
        a.setSubjectName(rs.getString("subject_name"));
        return a;
    }
}