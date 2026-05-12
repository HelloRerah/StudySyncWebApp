package studysync.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import studysync.dao.StudentDAO;
import studysync.model.Student;
import studysync.util.HashUtil;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentNumber = request.getParameter("studentId");
        String password      = request.getParameter("password");

        // Basic validation
        if (studentNumber == null || studentNumber.isBlank()
                || password == null || password.isBlank()) {
            response.sendRedirect("login.jsp?error=1");
            return;
        }

        String passwordHash = HashUtil.sha256(password);

        try {
            Student student = studentDAO.findByCredentials(studentNumber, passwordHash);

            if (student != null) {
                studentDAO.updateLastLogin(student.getStudentId());

                // Session fixation protection
                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) oldSession.invalidate();

                HttpSession session = request.getSession(true);
                session.setAttribute("student", student);
                session.setMaxInactiveInterval(30 * 60);

                response.sendRedirect("DashboardServlet");
            } else {
                response.sendRedirect("login.jsp?error=1");
            }

        } catch (SQLException e) {
            throw new ServletException("Database error during login", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}