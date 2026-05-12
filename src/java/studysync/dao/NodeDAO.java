package studysync.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import studysync.model.Node;
import studysync.util.DBUtil;

public class NodeDAO {

    public List<Node> findByEnrollment(int enrollmentId) throws SQLException {
        List<Node> list = new ArrayList<>();
        String sql = "SELECT n.*, s.subject_code, s.subject_name "
                   + "FROM node n "
                   + "JOIN subject_enrollment e ON n.enrollment_id = e.enrollment_id "
                   + "JOIN subject s ON e.subject_id = s.subject_id "
                   + "WHERE n.enrollment_id = ? "
                   + "ORDER BY n.created_at ASC";
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

    public void insert(Node node) throws SQLException {
        String sql = "INSERT INTO node (enrollment_id, title, description) "
                   + "VALUES (?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, node.getEnrollmentId());
            ps.setString(2, node.getTitle());
            ps.setString(3, node.getDescription());
            ps.executeUpdate();
        }
    }

    public void markComplete(int nodeId) throws SQLException {
        String sql = "UPDATE node SET is_completed = TRUE WHERE node_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nodeId);
            ps.executeUpdate();
        }
    }

    public Node findById(int nodeId) throws SQLException {
        String sql = "SELECT n.*, s.subject_code, s.subject_name "
                   + "FROM node n "
                   + "JOIN subject_enrollment e ON n.enrollment_id = e.enrollment_id "
                   + "JOIN subject s ON e.subject_id = s.subject_id "
                   + "WHERE n.node_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nodeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public int countCompleted(int enrollmentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM node "
                   + "WHERE enrollment_id = ? AND is_completed = TRUE";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int countTotal(int enrollmentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM node WHERE enrollment_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Node mapRow(ResultSet rs) throws SQLException {
        Node n = new Node();
        n.setNodeId(rs.getInt("node_id"));
        n.setEnrollmentId(rs.getInt("enrollment_id"));
        n.setTitle(rs.getString("title"));
        n.setDescription(rs.getString("description"));
        n.setCompleted(rs.getBoolean("is_completed"));
        n.setCreatedAt(rs.getTimestamp("created_at"));
        n.setSubjectCode(rs.getString("subject_code"));
        n.setSubjectName(rs.getString("subject_name"));
        return n;
    }
}