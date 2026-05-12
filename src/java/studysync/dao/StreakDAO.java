package studysync.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import studysync.util.DBUtil;

public class StreakDAO {

    public void logToday(int studentId) throws SQLException {
        String sql = "INSERT INTO streak_log (student_id, log_date) VALUES (?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            // Ignore duplicate — unique constraint means already logged today
            if (!e.getSQLState().equals("23505")) throw e;
        }
    }

    public int getCurrentStreak(int studentId) throws SQLException {
        int streak = 0;
        LocalDate checkDate = LocalDate.now();
        String sql = "SELECT log_date FROM streak_log WHERE student_id = ? AND log_date = ?";
        try (Connection c = DBUtil.getConnection()) {
            while (true) {
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, studentId);
                    ps.setDate(2, Date.valueOf(checkDate));
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        streak++;
                        checkDate = checkDate.minusDays(1);
                    } else {
                        break;
                    }
                }
            }
        }
        return streak;
    }

    public int getBestStreak(int studentId) throws SQLException {
        String sql = "SELECT log_date FROM streak_log "
                   + "WHERE student_id = ? ORDER BY log_date ASC";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            int best    = 0;
            int current = 0;
            LocalDate prev = null;

            while (rs.next()) {
                LocalDate date = rs.getDate("log_date").toLocalDate();
                if (prev == null || date.equals(prev.plusDays(1))) {
                    current++;
                } else {
                    current = 1;
                }
                if (current > best) best = current;
                prev = date;
            }
            return best;
        }
    }

    public int getTotalNodes(int studentId) throws SQLException {
        String sql = "SELECT SUM(nodes_done) FROM streak_log WHERE student_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int total = rs.getInt(1);
                return rs.wasNull() ? 0 : total;
            }
        }
        return 0;
    }

    public boolean loggedToday(int studentId) throws SQLException {
        String sql = "SELECT 1 FROM streak_log WHERE student_id = ? AND log_date = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public java.util.List<LocalDate> getThisWeek(int studentId) throws SQLException {
        java.util.List<LocalDate> dates = new java.util.ArrayList<>();
        LocalDate monday = LocalDate.now()
                .with(java.time.DayOfWeek.MONDAY);
        String sql = "SELECT log_date FROM streak_log "
                   + "WHERE student_id = ? AND log_date >= ? AND log_date <= ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setDate(2, Date.valueOf(monday));
            ps.setDate(3, Date.valueOf(monday.plusDays(6)));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dates.add(rs.getDate("log_date").toLocalDate());
            }
        }
        return dates;
    }
}