<%-- 
    Document   : predicate
    Created on : May 11, 2026, 12:05:17 PM
    Author     : rerah
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="studysync.model.Student"%>
<%@page import="studysync.model.Assessment"%>
<%@page import="studysync.model.Enrollment"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.math.BigDecimal"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Predicate · StudySync</title>
    <link rel="stylesheet" href="css/studysync.css"/>
</head>
<body>
<%
    Student student             = (Student) session.getAttribute("student");
    BigDecimal overallPredicate = (BigDecimal) request.getAttribute("overallPredicate");
    BigDecimal targetPredicate  = (BigDecimal) request.getAttribute("targetPredicate");
    List<Enrollment> enrollments = (List<Enrollment>) request.getAttribute("enrollments");
    Map<Integer, BigDecimal> predicateByEnrollment =
        (Map<Integer, BigDecimal>) request.getAttribute("predicateByEnrollment");
    Map<Integer, List<Assessment>> assessmentsByEnrollment =
        (Map<Integer, List<Assessment>>) request.getAttribute("assessmentsByEnrollment");
    if (overallPredicate == null) overallPredicate = BigDecimal.ZERO;
    if (targetPredicate  == null) targetPredicate  = new BigDecimal("75.00");
%>
<div class="hero-backdrop"></div>
<nav class="navbar">
    <div class="container nav-inner">
        <div class="nav-brand"><span>&#127979;</span> StudySync</div>
        <div class="nav-links">
            <a href="DashboardServlet" class="nav-link">Dashboard</a>
            <a href="SubjectsServlet"    class="nav-link">Subjects</a>
            <a href="AssessmentsServlet" class="nav-link">Assessments</a>
            <a href="PredicateServlet"   class="nav-link active">Predicate</a>
            <a href="StreakServlet"      class="nav-link">Streak</a>
            <a href="SignOutServlet"     class="nav-link" style="color:var(--muted-fg);">Sign out</a>
        </div>
    </div>
</nav>

<section class="section">
    <div class="container">
        <div class="section-header">
            <span class="section-kicker">Academic standing</span>
            <h2 class="section-title">Predicate Calculator</h2>
        </div>

        <div class="dash-grid">

            <!-- LEFT: Per-module breakdown -->
            <div>
                <div class="stat-card mb-4">
                    <div class="stat-top">
                        <div>
                            <div class="stat-label">Overall predicate</div>
                            <div class="stat-value"><%= overallPredicate %>%</div>
                        </div>
                        <div class="icon-well">&#127919;</div>
                    </div>
                    <div class="stat-helper">
                        Weighted average across all marked assessments.
                        Your target is <strong><%= targetPredicate %>%</strong>.
                    </div>
                </div>

                <%
                    if (enrollments != null && assessmentsByEnrollment != null) {
                        for (Enrollment e : enrollments) {
                            List<Assessment> marked = assessmentsByEnrollment.get(e.getEnrollmentId());
                            BigDecimal subjectPredicate = predicateByEnrollment != null
                                ? predicateByEnrollment.get(e.getEnrollmentId())
                                : BigDecimal.ZERO;
                            if (subjectPredicate == null) subjectPredicate = BigDecimal.ZERO;
                %>
                    <div class="card mb-4">
                        <div class="card-header">
                            <div class="kicker">Module predicate</div>
                            <h2><%= e.getSubjectName() %> — <%= e.getSubjectCode() %></h2>
                        </div>
                        <div class="card-body">
                            <%
                                if (marked == null || marked.isEmpty()) {
                            %>
                                <p class="text-sm text-muted">
                                    No marked assessments yet for this module.
                                </p>
                            <%
                                } else {
                            %>
                                <table class="pred-table">
                                    <thead>
                                        <tr>
                                            <th>Assessment</th>
                                            <th>Weight</th>
                                            <th>Score</th>
                                            <th>Weighted</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            for (Assessment a : marked) {
                                                BigDecimal weighted = a.getScore()
                                                    .multiply(a.getWeight())
                                                    .divide(new BigDecimal("100"));
                                        %>
                                        <tr>
                                            <td><%= a.getTitle() %></td>
                                            <td><%= a.getWeight() %>%</td>
                                            <td><%= a.getScore() %>%</td>
                                            <td><%= weighted.setScale(1, java.math.RoundingMode.HALF_UP) %>%</td>
                                        </tr>
                                        <%
                                            }
                                        %>
                                    </tbody>
                                </table>
                                <div class="progress-bar-wrap mt-3">
                                    <div class="progress-label">
                                        <span class="text-sm text-muted">Module predicate</span>
                                        <span class="text-sm font-semibold"><%= subjectPredicate %>%</span>
                                    </div>
                                    <div class="progress-track">
                                        <div class="progress-fill"
                                             style="width: <%= subjectPredicate %>%;"></div>
                                    </div>
                                </div>
                            <%
                                }
                            %>
                        </div>
                    </div>
                <%
                        }
                    }
                %>
            </div>

            <!-- RIGHT: Add new mark form -->
            <div>
                <div class="card">
                    <div class="card-header">
                        <div class="kicker">Calculate</div>
                        <h2>Add a mark</h2>
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
                                            <%= e.getSubjectCode() %> — <%= e.getSubjectName() %>
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
                                       name="title" placeholder="e.g. Test 1"/>
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
                                <label class="form-label">Score (%)</label>
                                <input class="form-input" type="number"
                                       name="score" min="0" max="100"
                                       placeholder="e.g. 74"/>
                            </div>
                            <div class="form-row">
                                <label class="form-label">Weight (%)</label>
                                <input class="form-input" type="number"
                                       name="weight" min="0" max="100"
                                       placeholder="e.g. 20"/>
                            </div>
                            <button class="btn btn-primary w-full mt-3" type="submit">
                                &#43; Add mark
                            </button>
                        </form>
                    </div>
                </div>

                <div class="highlight-block mt-3">
                    <div class="text-sm font-semibold">&#127919; Target: <%= targetPredicate %>%</div>
                    <div class="text-sm text-muted mt-1">
                        <%
                            if (overallPredicate.compareTo(targetPredicate) >= 0) {
                        %>
                            You have reached your target predicate. Keep it up!
                        <%
                            } else {
                        %>
                            Keep adding marks to track your progress towards your target.
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>

        </div>
    </div>
</section>
</body>
</html>