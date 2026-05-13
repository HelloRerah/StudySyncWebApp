package studysync.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import studysync.model.Question;
import studysync.util.DBUtil;

public class QuestionDAO {

    public void insertAll(List<Question> questions) throws SQLException {
        String sql = "INSERT INTO question "
                   + "(node_id, question_text, option_a, option_b, option_c, option_d, correct_answer) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (Question q : questions) {
                ps.setInt(1, q.getNodeId());
                ps.setString(2, q.getQuestionText());
                ps.setString(3, q.getOptionA());
                ps.setString(4, q.getOptionB());
                ps.setString(5, q.getOptionC());
                ps.setString(6, q.getOptionD());
                ps.setString(7, q.getCorrectAnswer());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public List<Question> findByNode(int nodeId) throws SQLException {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM question WHERE node_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nodeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public void deleteByNode(int nodeId) throws SQLException {
        String sql = "DELETE FROM question WHERE node_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nodeId);
            ps.executeUpdate();
        }
    }

    private Question mapRow(ResultSet rs) throws SQLException {
        Question q = new Question();
        q.setQuestionId(rs.getInt("question_id"));
        q.setNodeId(rs.getInt("node_id"));
        q.setQuestionText(rs.getString("question_text"));
        q.setOptionA(rs.getString("option_a"));
        q.setOptionB(rs.getString("option_b"));
        q.setOptionC(rs.getString("option_c"));
        q.setOptionD(rs.getString("option_d"));
        q.setCorrectAnswer(rs.getString("correct_answer"));
        return q;
    }
}