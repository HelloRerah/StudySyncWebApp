package studysync.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Student {

    private int studentId;
    private String studentNumber;
    private String fullName;
    private String email;
    private BigDecimal targetPredicate;
    private Timestamp createdAt;
    private Timestamp lastLogin;

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public BigDecimal getTargetPredicate() { return targetPredicate; }
    public void setTargetPredicate(BigDecimal targetPredicate) { this.targetPredicate = targetPredicate; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getLastLogin() { return lastLogin; }
    public void setLastLogin(Timestamp lastLogin) { this.lastLogin = lastLogin; }
}