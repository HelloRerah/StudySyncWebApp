package studysync.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import studysync.dao.StudentDAO;
import studysync.model.Student;
import studysync.util.HashUtil;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentNumber = request.getParameter("studentId");
        String fullName      = request.getParameter("name");
        String email         = request.getParameter("email");
        String password      = request.getParameter("password");

        // Basic validation
        if (studentNumber == null || studentNumber.isBlank()
                || fullName == null || fullName.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            response.sendRedirect("register.jsp?error=1");
            return;
        }

        try {
            // Check for duplicates
            if (studentDAO.studentNumberExists(studentNumber)) {
                response.sendRedirect("register.jsp?error=2");
                return;
            }

            if (studentDAO.emailExists(email)) {
                response.sendRedirect("register.jsp?error=3");
                return;
            }

            // Build student object
            Student student = new Student();
            student.setStudentNumber(studentNumber);
            student.setFullName(fullName);
            student.setEmail(email);

            // Hash password and insert
            String passwordHash = HashUtil.sha256(password);
            studentDAO.insert(student, passwordHash);

            // Redirect to login with success message
            response.sendRedirect("login.jsp?registered=1");

        } catch (SQLException e) {
            throw new ServletException("Database error during registration", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("register.jsp");
    }
}