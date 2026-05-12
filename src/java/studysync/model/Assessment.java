package studysync.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Assessment {

    private int assessmentId;
    private int enrollmentId;
    private String title;
    private String assessmentType;
    private BigDecimal weight;
    private BigDecimal score;
    private Timestamp dueDate;
    private String status;
    private Timestamp createdAt;

    // Joined fields
    private String subjectCode;
    private String subjectName;

    public int getAssessmentId() { return assessmentId; }
    public void setAssessmentId(int assessmentId) { this.assessmentId = assessmentId; }

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAssessmentType() { return assessmentType; }
    public void setAssessmentType(String assessmentType) { this.assessmentType = assessmentType; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }

    public Timestamp getDueDate() { return dueDate; }
    public void setDueDate(Timestamp dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
}