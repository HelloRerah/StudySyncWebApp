<%-- 
    Document   : subjects
    Created on : May 11, 2026, 12:08:22 PM
    Author     : rerah
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="studysync.model.Student"%>
<%@page import="studysync.model.Subject"%>
<%@page import="studysync.model.Enrollment"%>
<%@page import="studysync.util.HashUtil"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Subjects · StudySync</title>
    <link rel="stylesheet" href="css/studysync.css"/>
</head>
<body>
<%
    Student student          = (Student) session.getAttribute("student");
    List<Enrollment> enrollments = (List<Enrollment>) request.getAttribute("enrollments");
    List<Subject> allSubjects    = (List<Subject>) request.getAttribute("allSubjects");
%>
<div class="hero-backdrop"></div>
<nav class="navbar">
    <div class="container nav-inner">
        <div class="nav-brand"><span>&#127979;</span> StudySync</div>
        <div class="nav-links">
            <a href="DashboardServlet"   class="nav-link">Dashboard</a>
            <a href="SubjectsServlet"    class="nav-link active">Subjects</a>
            <a href="AssessmentsServlet" class="nav-link">Assessments</a>
            <a href="PredicateServlet"   class="nav-link">Predicate</a>
            <a href="StreakServlet"      class="nav-link">Streak</a>
            <a href="SignOutServlet"     class="nav-link" style="color:var(--muted-fg);">Sign out</a>
        </div>
    </div>
</nav>

<section class="section">
    <div class="container">
        <div class="section-header">
            <span class="section-kicker">Course overview</span>
            <h2 class="section-title">Subjects this semester</h2>
        </div>

        <%
            if (enrollments == null || enrollments.isEmpty()) {
        %>
            <div class="subject-card">
                <div class="subject-top">
                    <div>
                        <div class="next-node">
                            You are not enrolled in any subjects yet.
                            Use the form below to enroll.
                        </div>
                    </div>
                </div>
            </div>
        <%
            } else {
                for (Enrollment e : enrollments) {
        %>
            <div class="subject-card">
                <div class="subject-top">
                    <div>
                        <div class="flex items-center gap-2">
                            <span class="font-semibold"><%= HashUtil.escapeHtml(e.getSubjectName()) %></span>
                            <span class="badge"><%= HashUtil.escapeHtml(e.getSubjectCode()) %></span>
                        </div>
                        <div class="subject-meta"><%= HashUtil.escapeHtml(e.getUniversity()) %></div>
                    </div>
                    <div class="subject-stats">
                        <div class="row">
                            <span class="text-muted text-sm">Progress</span>
                            <span><%= e.getProgressPercent() %>%</span>
                        </div>
                        <div class="row">
                            <span class="text-muted text-sm">Nodes</span>
                            <span><%= e.getNodesCompleted() %>/<%= e.getTotalMaterials() %></span>
                        </div>
                    </div>
                </div>
                <div class="progress-bar-wrap">
                    <div class="progress-track">
                        <div class="progress-fill" style="width:<%= e.getProgressPercent() %>%;"></div>
                    </div>
                </div>
                <div style="margin-top: 0.75rem;">
                    <a href="NodeServlet?enrollmentId=<%= e.getEnrollmentId() %>"
                       class="btn btn-primary"
                       style="font-size:0.8rem; padding: 0.4rem 0.9rem;">
                        &#128218; View nodes
                    </a>
                </div>
            </div>
        <%
                }
            }
        %>

        <div class="section-header mt-4">
            <span class="section-kicker">Available subjects</span>
            <h2 class="section-title">Enroll in a subject</h2>
        </div>
        <div class="card">
            <div class="card-body">
                <form action="SubjectsServlet" method="POST">
                    <div class="form-row">
                        <label class="form-label">Select subject</label>
                        <select class="form-select" name="subjectId">
                            <option value="">-- Choose a subject --</option>
                            <%
                                if (allSubjects != null) {
                                    for (Subject s : allSubjects) {
                            %>
                                <option value="<%= s.getSubjectId() %>">
                                    <%= HashUtil.escapeHtml(s.getSubjectCode()) %> — <%= HashUtil.escapeHtml(s.getSubjectName()) %>
                                </option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>
                    <button class="btn btn-primary mt-3" type="submit">
                        &#43; Enroll
                    </button>
                </form>
            </div>
        </div>
    </div>
</section>
</body>
</html><%-- 
    Document   : subjects
    Created on : May 11, 2026, 12:08:22 PM
    Author     : rerah
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="studysync.model.Student"%>
<%@page import="studysync.model.Subject"%>
<%@page import="studysync.model.Enrollment"%>
<%@page import="studysync.util.HashUtil"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Subjects · StudySync</title>
    <link rel="stylesheet" href="css/studysync.css"/>
</head>
<body>
<%
    Student student          = (Student) session.getAttribute("student");
    List<Enrollment> enrollments = (List<Enrollment>) request.getAttribute("enrollments");
    List<Subject> allSubjects    = (List<Subject>) request.getAttribute("allSubjects");
%>
<div class="hero-backdrop"></div>
<nav class="navbar">
    <div class="container nav-inner">
        <div class="nav-brand"><span>&#127979;</span> StudySync</div>
        <div class="nav-links">
            <a href="DashboardServlet"   class="nav-link">Dashboard</a>
            <a href="SubjectsServlet"    class="nav-link active">Subjects</a>
            <a href="AssessmentsServlet" class="nav-link">Assessments</a>
            <a href="PredicateServlet"   class="nav-link">Predicate</a>
            <a href="StreakServlet"      class="nav-link">Streak</a>
            <a href="SignOutServlet"     class="nav-link" style="color:var(--muted-fg);">Sign out</a>
        </div>
    </div>
</nav>

<section class="section">
    <div class="container">
        <div class="section-header">
            <span class="section-kicker">Course overview</span>
            <h2 class="section-title">Subjects this semester</h2>
        </div>

        <%
            if (enrollments == null || enrollments.isEmpty()) {
        %>
            <div class="subject-card">
                <div class="subject-top">
                    <div>
                        <div class="next-node">
                            You are not enrolled in any subjects yet.
                            Use the form below to enroll.
                        </div>
                    </div>
                </div>
            </div>
        <%
            } else {
                for (Enrollment e : enrollments) {
        %>
            <div class="subject-card">
                <div class="subject-top">
                    <div>
                        <div class="flex items-center gap-2">
                            <span class="font-semibold"><%= HashUtil.escapeHtml(e.getSubjectName()) %></span>
                            <span class="badge"><%= HashUtil.escapeHtml(e.getSubjectCode()) %></span>
                        </div>
                        <div class="subject-meta"><%= HashUtil.escapeHtml(e.getUniversity()) %></div>
                    </div>
                    <div class="subject-stats">
                        <div class="row">
                            <span class="text-muted text-sm">Progress</span>
                            <span><%= e.getProgressPercent() %>%</span>
                        </div>
                        <div class="row">
                            <span class="text-muted text-sm">Nodes</span>
                            <span><%= e.getNodesCompleted() %>/<%= e.getTotalMaterials() %></span>
                        </div>
                    </div>
                </div>
                <div class="progress-bar-wrap">
                    <div class="progress-track">
                        <div class="progress-fill" style="width:<%= e.getProgressPercent() %>%;"></div>
                    </div>
                </div>
                <div style="margin-top: 0.75rem;">
                    <a href="NodeServlet?enrollmentId=<%= e.getEnrollmentId() %>"
                       class="btn btn-primary"
                       style="font-size:0.8rem; padding: 0.4rem 0.9rem;">
                        &#128218; View nodes
                    </a>
                </div>
            </div>
        <%
                }
            }
        %>

        <div class="section-header mt-4">
            <span class="section-kicker">Available subjects</span>
            <h2 class="section-title">Enroll in a subject</h2>
        </div>
        <div class="card">
            <div class="card-body">
                <form action="SubjectsServlet" method="POST">
                    <div class="form-row">
                        <label class="form-label">Select subject</label>
                        <select class="form-select" name="subjectId">
                            <option value="">-- Choose a subject --</option>
                            <%
                                if (allSubjects != null) {
                                    for (Subject s : allSubjects) {
                            %>
                                <option value="<%= s.getSubjectId() %>">
                                    <%= HashUtil.escapeHtml(s.getSubjectCode()) %> — <%= HashUtil.escapeHtml(s.getSubjectName()) %>
                                </option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>
                    <button class="btn btn-primary mt-3" type="submit">
                        &#43; Enroll
                    </button>
                </form>
            </div>
        </div>
    </div>
</section>
</body>
</html>