package studysync.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import studysync.dao.AssessmentDAO;
import studysync.model.Assessment;

public class PredicateService {

    private final AssessmentDAO assessmentDAO = new AssessmentDAO();

    public BigDecimal calculatePredicate(int enrollmentId) throws SQLException {
        List<Assessment> marked = assessmentDAO.findMarked(enrollmentId);
        if (marked.isEmpty()) return BigDecimal.ZERO;

        BigDecimal weightedSum  = BigDecimal.ZERO;
        BigDecimal totalWeight  = BigDecimal.ZERO;

        for (Assessment a : marked) {
            weightedSum = weightedSum.add(
                a.getScore().multiply(a.getWeight()).divide(new BigDecimal("100"))
            );
            totalWeight = totalWeight.add(a.getWeight());
        }

        if (totalWeight.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return weightedSum.divide(totalWeight, 2, RoundingMode.HALF_UP)
                          .multiply(new BigDecimal("100"))
                          .setScale(1, RoundingMode.HALF_UP);
    }

    public BigDecimal requiredMark(BigDecimal currentPredicate,
                                   BigDecimal targetPredicate,
                                   BigDecimal nextWeight,
                                   BigDecimal totalWeight) {
        if (nextWeight.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        BigDecimal numerator = targetPredicate.multiply(totalWeight)
                .subtract(currentPredicate.multiply(
                        totalWeight.subtract(nextWeight)));

        BigDecimal required = numerator.divide(nextWeight, 2, RoundingMode.HALF_UP);

        if (required.compareTo(new BigDecimal("100")) > 0)
            return new BigDecimal("100");
        if (required.compareTo(BigDecimal.ZERO) < 0)
            return BigDecimal.ZERO;

        return required.setScale(1, RoundingMode.HALF_UP);
    }
}