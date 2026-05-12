package studysync.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import studysync.dao.StreakDAO;
import studysync.model.Student;

@WebServlet("/StreakServlet")
public class StreakServlet extends HttpServlet {

    private final StreakDAO streakDAO = new StreakDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        try {
            int currentStreak = streakDAO.getCurrentStreak(student.getStudentId());
            int bestStreak    = streakDAO.getBestStreak(student.getStudentId());
            int totalNodes    = streakDAO.getTotalNodes(student.getStudentId());
            boolean loggedToday = streakDAO.loggedToday(student.getStudentId());
            List<LocalDate> thisWeek = streakDAO.getThisWeek(student.getStudentId());

            request.setAttribute("currentStreak", currentStreak);
            request.setAttribute("bestStreak",    bestStreak);
            request.setAttribute("totalNodes",    totalNodes);
            request.setAttribute("loggedToday",   loggedToday);
            request.setAttribute("thisWeek",      thisWeek);

            request.getRequestDispatcher("streak.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error loading streak", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        try {
            streakDAO.logToday(student.getStudentId());
            response.sendRedirect("StreakServlet");
        } catch (SQLException e) {
            throw new ServletException("Database error logging streak", e);
        }
    }
}