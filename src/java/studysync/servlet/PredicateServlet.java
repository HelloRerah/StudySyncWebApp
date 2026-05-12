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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import studysync.dao.AssessmentDAO;
import studysync.dao.EnrollmentDAO;
import studysync.model.Assessment;
import studysync.model.Enrollment;
import studysync.model.Student;
import studysync.service.PredicateService;

@WebServlet("/PredicateServlet")
public class PredicateServlet extends HttpServlet {

    private final EnrollmentDAO    enrollmentDAO    = new EnrollmentDAO();
    private final AssessmentDAO    assessmentDAO    = new AssessmentDAO();
    private final PredicateService predicateService = new PredicateService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        try {
            List<Enrollment> enrollments = enrollmentDAO.findByStudent(
                    student.getStudentId(), 1, 2026);

            Map<Integer, BigDecimal>       predicateByEnrollment   = new LinkedHashMap<>();
            Map<Integer, List<Assessment>> assessmentsByEnrollment = new LinkedHashMap<>();

            BigDecimal overallWeightedSum = BigDecimal.ZERO;
            BigDecimal overallTotalWeight = BigDecimal.ZERO;

            for (Enrollment e : enrollments) {
                List<Assessment> marked = assessmentDAO.findMarked(e.getEnrollmentId());
                assessmentsByEnrollment.put(e.getEnrollmentId(), marked);

                BigDecimal subjectWeightedSum = BigDecimal.ZERO;
                BigDecimal subjectTotalWeight = BigDecimal.ZERO;

                for (Assessment a : marked) {
                    subjectWeightedSum = subjectWeightedSum.add(
                        a.getScore().multiply(a.getWeight())
                         .divide(new BigDecimal("100"))
                    );
                    subjectTotalWeight = subjectTotalWeight.add(a.getWeight());
                    overallWeightedSum = overallWeightedSum.add(
                        a.getScore().multiply(a.getWeight())
                         .divide(new BigDecimal("100"))
                    );
                    overallTotalWeight = overallTotalWeight.add(a.getWeight());
                }

                BigDecimal subjectPredicate = BigDecimal.ZERO;
                if (subjectTotalWeight.compareTo(BigDecimal.ZERO) > 0) {
                    subjectPredicate = subjectWeightedSum
                            .divide(subjectTotalWeight, 4, java.math.RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"))
                            .setScale(1, java.math.RoundingMode.HALF_UP);
                }
                predicateByEnrollment.put(e.getEnrollmentId(), subjectPredicate);
            }

            BigDecimal overallPredicate = BigDecimal.ZERO;
            if (overallTotalWeight.compareTo(BigDecimal.ZERO) > 0) {
                overallPredicate = overallWeightedSum
                        .divide(overallTotalWeight, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .setScale(1, java.math.RoundingMode.HALF_UP);
            }

            request.setAttribute("enrollments",             enrollments);
            request.setAttribute("predicateByEnrollment",   predicateByEnrollment);
            request.setAttribute("assessmentsByEnrollment", assessmentsByEnrollment);
            request.setAttribute("overallPredicate",        overallPredicate);
            request.setAttribute("targetPredicate",         student.getTargetPredicate());

            request.getRequestDispatcher("predicate.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error loading predicate", e);
        }
    }
}