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
import java.sql.Timestamp;
import java.util.List;
import studysync.dao.AssessmentDAO;
import studysync.dao.EnrollmentDAO;
import studysync.model.Assessment;
import studysync.model.Enrollment;
import studysync.model.Student;

@WebServlet("/AssessmentsServlet")
public class AssessmentsServlet extends HttpServlet {

    private final AssessmentDAO assessmentDAO = new AssessmentDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        try {
            List<Assessment> upcoming = assessmentDAO.findUpcoming(student.getStudentId());
            List<Enrollment> enrollments = enrollmentDAO.findByStudent(
                    student.getStudentId(), 1, 2026);

            request.setAttribute("upcoming",    upcoming);
            request.setAttribute("enrollments", enrollments);

            request.getRequestDispatcher("assessments.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error loading assessments", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        String enrollmentIdParam = request.getParameter("enrollmentId");
        String title             = request.getParameter("title");
        String type              = request.getParameter("type");
        String weightParam       = request.getParameter("weight");
        String scoreParam        = request.getParameter("score");
        String dueDateParam      = request.getParameter("dueDate");
        String status            = request.getParameter("status");

        if (enrollmentIdParam == null || title == null || title.isBlank()
                || weightParam == null || weightParam.isBlank()) {
            response.sendRedirect("AssessmentsServlet?error=1");
            return;
        }

        try {
            Assessment a = new Assessment();
            a.setEnrollmentId(Integer.parseInt(enrollmentIdParam));
            a.setTitle(title.trim());
            a.setAssessmentType(type);
            a.setWeight(new BigDecimal(weightParam));
            a.setScore(scoreParam != null && !scoreParam.isBlank()
                    ? new BigDecimal(scoreParam) : null);
            a.setDueDate(dueDateParam != null && !dueDateParam.isBlank()
        ? Timestamp.valueOf(dueDateParam.replace("T", " ") + ":00") : null);
            a.setStatus(status != null ? status : "Pending");

            assessmentDAO.insert(a);
            response.sendRedirect("AssessmentsServlet?success=1");

        } catch (SQLException e) {
            throw new ServletException("Database error adding assessment", e);
        }
    }
}