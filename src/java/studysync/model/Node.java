package studysync.model;

import java.sql.Timestamp;

public class Node {

    private int nodeId;
    private int enrollmentId;
    private String title;
    private String description;
    private boolean completed;
    private Timestamp createdAt;

    // Joined fields
    private String subjectCode;
    private String subjectName;

    public int getNodeId() { return nodeId; }
    public void setNodeId(int nodeId) { this.nodeId = nodeId; }

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
}