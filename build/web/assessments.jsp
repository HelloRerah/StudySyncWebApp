<%-- 
    Document   : assessments
    Created on : May 11, 2026, 12:09:08 PM
    Author     : rerah
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="studysync.model.Student"%>
<%@page import="studysync.model.Assessment"%>
<%@page import="studysync.model.Enrollment"%>
<%@page import="studysync.util.HashUtil"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Assessments · StudySync</title>
    <link rel="stylesheet" href="css/studysync.css"/>
</head>
<body>
<%
    Student student              = (Student) session.getAttribute("student");
    List<Assessment> upcoming    = (List<Assessment>) request.getAttribute("upcoming");
    List<Enrollment> enrollments = (List<Enrollment>) request.getAttribute("enrollments");
    String error   = request.getParameter("error");
    String success = request.getParameter("success");
%>
<div class="hero-backdrop"></div>
<nav class="navbar">
    <div class="container nav-inner">
        <div class="nav-brand"><span>&#127979;</span> StudySync</div>
        <div class="nav-links">
            <a href="DashboardServlet"   class="nav-link">Dashboard</a>
            <a href="SubjectsServlet"    class="nav-link">Subjects</a>
            <a href="AssessmentsServlet" class="nav-link active">Assessments</a>
            <a href="PredicateServlet"   class="nav-link">Predicate</a>
            <a href="StreakServlet"      class="nav-link">Streak</a>
            <a href="SignOutServlet"     class="nav-link" style="color:var(--muted-fg);">Sign out</a>
        </div>
    </div>
</nav>
<section class="section">
    <div class="container">
        <% if ("1".equals(error)) { %>
            <div class="alert alert-error">Please fill in all required fields.</div>
        <% } else if ("2".equals(error)) { %>
            <div class="alert alert-error">Weight must be a number between 0 and 100.</div>
        <% } else if ("3".equals(error)) { %>
            <div class="alert alert-error">Score must be a number between 0 and 100.</div>
        <% } else if ("4".equals(error)) { %>
            <div class="alert alert-error">Invalid due date format. Please use the date picker.</div>
        <% } else if ("1".equals(success)) { %>
            <div class="alert alert-success">Assessment added successfully.</div>
        <% } %>
        <div class="dash-grid">
            <!-- LEFT: Upcoming assessments -->
            <div>
                <div class="section-header">
                    <span class="section-kicker">Deadlines</span>
                    <h2 class="section-title">Upcoming assessments</h2>
                </div>
                <%
                    if (upcoming == null || upcoming.isEmpty()) {
                %>
                    <div class="assessment-item">
                        <div class="a-top">
                            <div>
                                <div class="a-title">No upcoming assessments</div>
                                <div class="a-meta">Add one using the form</div>
                            </div>
                        </div>
                    </div>
                <%
                    } else {
                        for (Assessment a : upcoming) {
                %>
                    <div class="assessment-item">
                        <div class="a-top">
                            <div>
                                <div class="a-title"><%= HashUtil.escapeHtml(a.getTitle()) %></div>
                                <div class="a-meta">
                                    <%= HashUtil.escapeHtml(a.getSubjectCode()) %> &middot;
                                    <%= HashUtil.escapeHtml(a.getAssessmentType()) %>
                                </div>
                            </div>
                            <span class="badge"><%= a.getWeight() %>%</span>
                        </div>
                        <div class="a-bottom">
                            <span>
                                <%= a.getDueDate() != null ? a.getDueDate().toString() : "No due date" %>
                            </span>
                            <span><%= HashUtil.escapeHtml(a.getStatus()) %></span>
                        </div>
                    </div>
                <%
                        }
                    }
                %>
            </div>
            <!-- RIGHT: Add assessment form -->
            <div>
                <div class="card">
                    <div class="card-header">
                        <div class="kicker">New</div>
                        <h2>Add assessment</h2>
                    </div>
                    <div class="card-body">
                        <form action="AssessmentsServlet" method="POST">
                            <div class="form-row">
                                <label class="form-label">Subject</label>
                                <select class="form-select" name="enrollmentId">
                                    <option value="">-- Select subject --</option>
                                    <%
                                        if (enrollments != null) {
                                            for (Enrollment e : enrollments) {
                                    %>
                                        <option value="<%= e.getEnrollmentId() %>">
                                            <%= HashUtil.escapeHtml(e.getSubjectCode()) %> — <%= HashUtil.escapeHtml(e.getSubjectName()) %>
                                        </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            <div class="form-row">
                                <label class="form-label">Title</label>
                                <input class="form-input" type="text"
                                       name="title" placeholder="e.g. Linked List Quiz" required/>
                            </div>
                            <div class="form-row">
                                <label class="form-label">Type</label>
                                <select class="form-select" name="type">
                                    <option>Quiz</option>
                                    <option>Test</option>
                                    <option>Assignment</option>
                                    <option>Exam</option>
                                    <option>Project</option>
                                </select>
                            </div>
                            <div class="form-row">
                                <label class="form-label">Weight (%)</label>
                                <input class="form-input" type="number"
                                       name="weight" min="0" max="100"
                                       placeholder="e.g. 20" required/>
                            </div>
                            <div class="form-row">
                                <label class="form-label">Score (%) — optional</label>
                                <input class="form-input" type="number"
                                       name="score" min="0" max="100"
                                       placeholder="Leave blank if not yet marked"/>
                            </div>
                            <div class="form-row">
                                <label class="form-label">Due date — optional</label>
                                <input class="form-input" type="datetime-local" name="dueDate"/>
                            </div>
                            <div class="form-row">
                                <label class="form-label">Status</label>
                                <select class="form-select" name="status">
                                    <option>Pending</option>
                                    <option>Draft started</option>
                                    <option>Needs revision</option>
                                    <option>Ready to review</option>
                                    <option>Submitted</option>
                                </select>
                            </div>
                            <button class="btn btn-primary w-full mt-3" type="submit">
                                &#43; Add assessment
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
</html>