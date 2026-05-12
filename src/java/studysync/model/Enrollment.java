package studysync.model;

public class Enrollment {

    private int enrollmentId;
    private int studentId;
    private int subjectId;
    private int semester;
    private int studyYear;
    private int totalMaterials;
    private int nodesCompleted;

    // Joined fields — populated when queried with subject data
    private String subjectCode;
    private String subjectName;
    private String university;

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public int getStudyYear() { return studyYear; }
    public void setStudyYear(int studyYear) { this.studyYear = studyYear; }

    public int getTotalMaterials() { return totalMaterials; }
    public void setTotalMaterials(int totalMaterials) { this.totalMaterials = totalMaterials; }

    public int getNodesCompleted() { return nodesCompleted; }
    public void setNodesCompleted(int nodesCompleted) { this.nodesCompleted = nodesCompleted; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    public int getProgressPercent() {
        if (totalMaterials == 0) return 0;
        return (int) Math.round((nodesCompleted * 100.0) / totalMaterials);
    }
}