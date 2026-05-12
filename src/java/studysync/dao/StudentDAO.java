package studysync.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import studysync.model.Student;
import studysync.util.DBUtil;

public class StudentDAO {

    public Student findByCredentials(String studentNumber, String passwordHash)
            throws SQLException {
        String sql = "SELECT * FROM student WHERE student_number = ? AND password_hash = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, studentNumber);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    public boolean studentNumberExists(String studentNumber) throws SQLException {
        String sql = "SELECT 1 FROM student WHERE student_number = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, studentNumber);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM student WHERE email = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void insert(Student student, String passwordHash) throws SQLException {
        String sql = "INSERT INTO student (student_number, full_name, email, password_hash) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, student.getStudentNumber());
            ps.setString(2, student.getFullName());
            ps.setString(3, student.getEmail());
            ps.setString(4, passwordHash);
            ps.executeUpdate();
        }
    }

    public void updateLastLogin(int studentId) throws SQLException {
        String sql = "UPDATE student SET last_login = CURRENT_TIMESTAMP WHERE student_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.executeUpdate();
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getInt("student_id"));
        s.setStudentNumber(rs.getString("student_number"));
        s.setFullName(rs.getString("full_name"));
        s.setEmail(rs.getString("email"));
        s.setTargetPredicate(rs.getBigDecimal("target_predicate"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        s.setLastLogin(rs.getTimestamp("last_login"));
        return s;
    }
}