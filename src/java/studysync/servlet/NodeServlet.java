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
import studysync.dao.NodeDAO;
import studysync.dao.StreakDAO;
import studysync.model.Enrollment;
import studysync.model.Node;
import studysync.model.Student;

@WebServlet("/NodeServlet")
public class NodeServlet extends HttpServlet {

    private final NodeDAO       nodeDAO       = new NodeDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final StreakDAO     streakDAO     = new StreakDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        String enrollmentIdParam = request.getParameter("enrollmentId");

        if (enrollmentIdParam == null || enrollmentIdParam.isBlank()) {
            response.sendRedirect("SubjectsServlet");
            return;
        }

        int enrollmentId = Integer.parseInt(enrollmentIdParam);

        try {
            Enrollment enrollment = enrollmentDAO.findById(enrollmentId);
            List<Node> nodes      = nodeDAO.findByEnrollment(enrollmentId);
            int completed         = nodeDAO.countCompleted(enrollmentId);
            int total             = nodeDAO.countTotal(enrollmentId);

            request.setAttribute("enrollment", enrollment);
            request.setAttribute("nodes",      nodes);
            request.setAttribute("completed",  completed);
            request.setAttribute("total",      total);

            request.getRequestDispatcher("nodes.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error loading nodes", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        String action            = request.getParameter("action");
        String enrollmentIdParam = request.getParameter("enrollmentId");
        int enrollmentId         = Integer.parseInt(enrollmentIdParam);

        try {
            if ("add".equals(action)) {
                String title       = request.getParameter("title");
                String description = request.getParameter("description");

                if (title == null || title.isBlank()) {
                    response.sendRedirect("NodeServlet?enrollmentId=" + enrollmentId + "&error=1");
                    return;
                }

                Node node = new Node();
                node.setEnrollmentId(enrollmentId);
                node.setTitle(title.trim());
                node.setDescription(description != null ? description.trim() : "");
                nodeDAO.insert(node);

                // Update total_materials counter
                int completed = nodeDAO.countCompleted(enrollmentId);
                int total     = nodeDAO.countTotal(enrollmentId);
                enrollmentDAO.updateProgress(enrollmentId, completed, total);

            } else if ("complete".equals(action)) {
                String nodeIdParam = request.getParameter("nodeId");
                int nodeId = Integer.parseInt(nodeIdParam);

                nodeDAO.markComplete(nodeId);
                streakDAO.logToday(student.getStudentId());

                // Update nodes_completed counter
                int completed = nodeDAO.countCompleted(enrollmentId);
                int total     = nodeDAO.countTotal(enrollmentId);
                enrollmentDAO.updateProgress(enrollmentId, completed, total);
            }

            response.sendRedirect("NodeServlet?enrollmentId=" + enrollmentId);

        } catch (SQLException e) {
            throw new ServletException("Database error in node action", e);
        }
    }
}