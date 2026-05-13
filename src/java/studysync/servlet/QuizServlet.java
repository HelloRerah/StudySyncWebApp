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
import studysync.dao.QuestionDAO;
import studysync.dao.QuizAttemptDAO;
import studysync.dao.StreakDAO;
import studysync.model.Node;
import studysync.model.Question;
import studysync.model.QuizAttempt;
import studysync.model.Student;
import studysync.service.QuizGeneratorService;

@WebServlet("/QuizServlet")
public class QuizServlet extends HttpServlet {

    private final QuestionDAO        questionDAO        = new QuestionDAO();
    private final QuizAttemptDAO     quizAttemptDAO     = new QuizAttemptDAO();
    private final NodeDAO            nodeDAO            = new NodeDAO();
    private final EnrollmentDAO      enrollmentDAO      = new EnrollmentDAO();
    private final StreakDAO          streakDAO          = new StreakDAO();
    private final QuizGeneratorService quizGenerator   = new QuizGeneratorService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        String nodeIdParam = request.getParameter("nodeId");
        if (nodeIdParam == null || nodeIdParam.isBlank()) {
            response.sendRedirect("SubjectsServlet");
            return;
        }

        int nodeId = Integer.parseInt(nodeIdParam);

        try {
            Node node = nodeDAO.findById(nodeId);

            // Check if already passed
            if (quizAttemptDAO.hasPassed(nodeId, student.getStudentId())) {
                request.setAttribute("node",    node);
                request.setAttribute("already", true);
                request.getRequestDispatcher("quiz-result.jsp").forward(request, response);
                return;
            }

            // Load existing questions or show generate form
            List<Question> questions = questionDAO.findByNode(nodeId);

            request.setAttribute("node",      node);
            request.setAttribute("questions", questions);
            request.setAttribute("attempts",
                quizAttemptDAO.getAttemptCount(nodeId, student.getStudentId()));
            request.setAttribute("bestScore",
                quizAttemptDAO.getBestScore(nodeId, student.getStudentId()));

            request.getRequestDispatcher("quiz.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error loading quiz", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        String action      = request.getParameter("action");
        String nodeIdParam = request.getParameter("nodeId");
        int nodeId         = Integer.parseInt(nodeIdParam);

        try {
            if ("generate".equals(action)) {
                String studyContent = request.getParameter("studyContent");

                if (studyContent == null || studyContent.isBlank()) {
                    response.sendRedirect("QuizServlet?nodeId=" + nodeId + "&error=1");
                    return;
                }

                // Delete old questions and generate new ones
                questionDAO.deleteByNode(nodeId);
                List<Question> questions = quizGenerator.generateQuestions(nodeId, studyContent);

                if (questions.isEmpty()) {
                    response.sendRedirect("QuizServlet?nodeId=" + nodeId + "&error=2");
                    return;
                }

                questionDAO.insertAll(questions);
                response.sendRedirect("QuizServlet?nodeId=" + nodeId);

            } else if ("submit".equals(action)) {
                List<Question> questions = questionDAO.findByNode(nodeId);

                int correct = 0;
                for (Question q : questions) {
                    String answer = request.getParameter("q" + q.getQuestionId());
                    if (q.getCorrectAnswer().equalsIgnoreCase(answer)) {
                        correct++;
                    }
                }

                int total      = questions.size();
                int score      = total == 0 ? 0 : (int) Math.round((correct * 100.0) / total);
                boolean passed = score >= 60;

                // Save attempt
                QuizAttempt attempt = new QuizAttempt();
                attempt.setNodeId(nodeId);
                attempt.setStudentId(student.getStudentId());
                attempt.setScore(score);
                attempt.setPassed(passed);
                quizAttemptDAO.insert(attempt);

                if (passed) {
                    // Mark node complete and log streak
                    Node node = nodeDAO.findById(nodeId);
                    nodeDAO.markComplete(nodeId);
                    streakDAO.logToday(student.getStudentId());

                    int enrollmentId = node.getEnrollmentId();
                    int completed    = nodeDAO.countCompleted(enrollmentId);
                    int total2       = nodeDAO.countTotal(enrollmentId);
                    enrollmentDAO.updateProgress(enrollmentId, completed, total2);
                }

                // Pass result to JSP
                request.setAttribute("score",   score);
                request.setAttribute("passed",  passed);
                request.setAttribute("correct", correct);
                request.setAttribute("total",   total);
                request.setAttribute("node",    nodeDAO.findById(nodeId));
                request.getRequestDispatcher("quiz-result.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            throw new ServletException("Database error in quiz", e);
        }
    }
}