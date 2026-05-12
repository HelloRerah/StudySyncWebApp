package studysync.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import studysync.dao.EnrollmentDAO;
import studysync.dao.SubjectDAO;
import studysync.model.Enrollment;
import studysync.model.Student;
import studysync.model.Subject;

@WebServlet("/SubjectsServlet")
public class SubjectsServlet extends HttpServlet {

    private final SubjectDAO subjectDAO       = new SubjectDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        int semester  = 1;
        int studyYear = 2026;

        try {
            // All available subjects for enrollment
            List<Subject> allSubjects = subjectDAO.findAll();

            // Subjects this student is enrolled in
            List<Enrollment> enrollments = enrollmentDAO.findByStudent(
                    student.getStudentId(), semester, studyYear);

            request.setAttribute("allSubjects",  allSubjects);
            request.setAttribute("enrollments",  enrollments);
            request.setAttribute("semester",     semester);
            request.setAttribute("studyYear",    studyYear);

            request.getRequestDispatcher("subjects.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error loading subjects", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        String subjectIdParam = request.getParameter("subjectId");

        if (subjectIdParam == null || subjectIdParam.isBlank()) {
            response.sendRedirect("SubjectsServlet");
            return;
        }

        int subjectId = Integer.parseInt(subjectIdParam);
        int semester  = 1;
        int studyYear = 2026;

        try {
            if (!enrollmentDAO.isEnrolled(student.getStudentId(), subjectId, semester, studyYear)) {
                enrollmentDAO.enroll(student.getStudentId(), subjectId, semester, studyYear);
            }
            response.sendRedirect("SubjectsServlet");

        } catch (SQLException e) {
            throw new ServletException("Database error during enrollment", e);
        }
    }
}