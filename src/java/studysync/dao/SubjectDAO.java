package studysync.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import studysync.model.Subject;
import studysync.util.DBUtil;

public class SubjectDAO {

    public List<Subject> findAll() throws SQLException {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM subject ORDER BY subject_name";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Subject> findByStudent(int studentId, int semester, int studyYear)
            throws SQLException {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT s.* FROM subject s "
                   + "JOIN subject_enrollment e ON s.subject_id = e.subject_id "
                   + "WHERE e.student_id = ? AND e.semester = ? AND e.study_year = ?";
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

    public Subject findById(int subjectId) throws SQLException {
        String sql = "SELECT * FROM subject WHERE subject_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    private Subject mapRow(ResultSet rs) throws SQLException {
        Subject s = new Subject();
        s.setSubjectId(rs.getInt("subject_id"));
        s.setSubjectCode(rs.getString("subject_code"));
        s.setSubjectName(rs.getString("subject_name"));
        s.setUniversity(rs.getString("university"));
        return s;
    }
}