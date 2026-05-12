package studysync.model;

public class Subject {

    private int subjectId;
    private String subjectCode;
    private String subjectName;
    private String university;

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
}