package studysync.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import studysync.model.Enrollment;
import studysync.util.DBUtil;

public class EnrollmentDAO {

    public List<Enrollment> findByStudent(int studentId, int semester, int studyYear)
            throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.*, s.subject_code, s.subject_name, s.university "
                   + "FROM subject_enrollment e "
                   + "JOIN subject s ON e.subject_id = s.subject_id "
                   + "WHERE e.student_id = ? AND e.semester = ? AND e.study_year = ? "
                   + "ORDER BY s.subject_name";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, semester);
            ps.setInt(3, studyYear);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public boolean isEnrolled(int studentId, int subjectId, int semester, int studyYear)
            throws SQLException {
        String sql = "SELECT 1 FROM subject_enrollment "
                   + "WHERE student_id = ? AND subject_id = ? "
                   + "AND semester = ? AND study_year = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, subjectId);
            ps.setInt(3, semester);
            ps.setInt(4, studyYear);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void enroll(int studentId, int subjectId, int semester, int studyYear)
            throws SQLException {
        String sql = "INSERT INTO subject_enrollment "
                   + "(student_id, subject_id, semester, study_year) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, subjectId);
            ps.setInt(3, semester);
            ps.setInt(4, studyYear);
            ps.executeUpdate();
        }
    }

    public Enrollment findById(int enrollmentId) throws SQLException {
        String sql = "SELECT e.*, s.subject_code, s.subject_name, s.university "
                   + "FROM subject_enrollment e "
                   + "JOIN subject s ON e.subject_id = s.subject_id "
                   + "WHERE e.enrollment_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    private Enrollment mapRow(ResultSet rs) throws SQLException {
        Enrollment e = new Enrollment();
        e.setEnrollmentId(rs.getInt("enrollment_id"));
        e.setStudentId(rs.getInt("student_id"));
        e.setSubjectId(rs.getInt("subject_id"));
        e.setSemester(rs.getInt("semester"));
        e.setStudyYear(rs.getInt("study_year"));
        e.setTotalMaterials(rs.getInt("total_materials"));
        e.setNodesCompleted(rs.getInt("nodes_completed"));
        e.setSubjectCode(rs.getString("subject_code"));
        e.setSubjectName(rs.getString("subject_name"));
        e.setUniversity(rs.getString("university"));
        return e;
    }
    
    public void updateProgress(int enrollmentId, int nodesCompleted, int totalMaterials)
        throws SQLException {
    String sql = "UPDATE subject_enrollment "
               + "SET nodes_completed = ?, total_materials = ? "
               + "WHERE enrollment_id = ?";
    try (Connection c = DBUtil.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, nodesCompleted);
        ps.setInt(2, totalMaterials);
        ps.setInt(3, enrollmentId);
        ps.executeUpdate();
    }
}
    
}