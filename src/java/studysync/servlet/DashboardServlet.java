package studysync.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import studysync.dao.AssessmentDAO;
import studysync.dao.EnrollmentDAO;
import studysync.dao.StreakDAO;
import studysync.model.Assessment;
import studysync.model.Enrollment;
import studysync.model.Student;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {

    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final AssessmentDAO assessmentDAO = new AssessmentDAO();
    private final StreakDAO     streakDAO     = new StreakDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        try {
            // Enrolled subjects
            List<Enrollment> enrollments = enrollmentDAO.findByStudent(
                    student.getStudentId(), 1, 2026);

            // Upcoming assessments
            List<Assessment> upcoming = assessmentDAO.findUpcoming(
                    student.getStudentId());

            // Due this week count
            int dueThisWeek = assessmentDAO.countDueThisWeek(
                    student.getStudentId());

            // Streak
            int currentStreak = streakDAO.getCurrentStreak(student.getStudentId());

            // Overall predicate across all modules
            BigDecimal overallPredicate = BigDecimal.ZERO;
            BigDecimal totalWeight      = BigDecimal.ZERO;

            for (Enrollment e : enrollments) {
                List<Assessment> marked = assessmentDAO.findMarked(e.getEnrollmentId());
                for (Assessment a : marked) {
                    overallPredicate = overallPredicate.add(
                        a.getScore().multiply(a.getWeight())
                         .divide(new BigDecimal("100"))
                    );
                    totalWeight = totalWeight.add(a.getWeight());
                }
            }

            if (totalWeight.compareTo(BigDecimal.ZERO) > 0) {
                overallPredicate = overallPredicate
                        .divide(totalWeight, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .setScale(1, java.math.RoundingMode.HALF_UP);
            }

            // Progress this term
            int totalNodes     = 0;
            int completedNodes = 0;
            for (Enrollment e : enrollments) {
                totalNodes     += e.getTotalMaterials();
                completedNodes += e.getNodesCompleted();
            }
            int progressPercent = totalNodes == 0 ? 0
                    : (int) Math.round((completedNodes * 100.0) / totalNodes);

            request.setAttribute("enrollments",      enrollments);
            request.setAttribute("upcoming",         upcoming);
            request.setAttribute("dueThisWeek",      dueThisWeek);
            request.setAttribute("currentStreak",    currentStreak);
            request.setAttribute("overallPredicate", overallPredicate);
            request.setAttribute("progressPercent",  progressPercent);
            request.setAttribute("completedNodes",   completedNodes);
            request.setAttribute("totalNodes",       totalNodes);

            request.getRequestDispatcher("dashboard.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error loading dashboard", e);
        }
    }
}