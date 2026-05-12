<%-- 
    Document   : dashboard
    Created on : May 11, 2026, 12:09:56 PM
    Author     : rerah
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="studysync.model.Student"%>
<%@page import="studysync.model.Assessment"%>
<%@page import="studysync.model.Enrollment"%>
<%@page import="studysync.util.HashUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.math.BigDecimal"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Dashboard · StudySync</title>
    <link rel="stylesheet" href="css/studysync.css"/>
</head>
<body>
<%
    Student student              = (Student) session.getAttribute("student");
    List<Enrollment> enrollments = (List<Enrollment>) request.getAttribute("enrollments");
    List<Assessment> upcoming    = (List<Assessment>) request.getAttribute("upcoming");
    Integer dueThisWeek          = (Integer) request.getAttribute("dueThisWeek");
    Integer currentStreak        = (Integer) request.getAttribute("currentStreak");
    BigDecimal overallPredicate  = (BigDecimal) request.getAttribute("overallPredicate");
    Integer progressPercent      = (Integer) request.getAttribute("progressPercent");
    Integer completedNodes       = (Integer) request.getAttribute("completedNodes");
    Integer totalNodes           = (Integer) request.getAttribute("totalNodes");

    if (dueThisWeek      == null) dueThisWeek      = 0;
    if (currentStreak    == null) currentStreak    = 0;
    if (overallPredicate == null) overallPredicate = BigDecimal.ZERO;
    if (progressPercent  == null) progressPercent  = 0;
    if (completedNodes   == null) completedNodes   = 0;
    if (totalNodes       == null) totalNodes       = 0;
%>
<div class="hero-backdrop"></div>

<!-- ── NAVBAR ── -->
<nav class="navbar">
    <div class="container nav-inner">
        <div class="nav-brand">
            <span>&#127979;</span> StudySync
        </div>
        <div class="nav-links">
            <a href="DashboardServlet"   class="nav-link active">Dashboard</a>
            <a href="SubjectsServlet"    class="nav-link">Subjects</a>
            <a href="AssessmentsServlet" class="nav-link">Assessments</a>
            <a href="PredicateServlet"   class="nav-link">Predicate</a>
            <a href="StreakServlet"      class="nav-link">Streak</a>
            <a href="SignOutServlet"     class="nav-link" style="color:var(--muted-fg);">Sign out</a>
        </div>
    </div>
</nav>

<!-- ── HERO SECTION ── -->
<section class="hero-section">
    <div class="container">
        <div class="hero-grid">

            <!-- LEFT: Heading -->
            <div>
                <div class="hero-pill">
                    <span>&#127979;</span>
                    STUDYSYNC &middot; PERSONAL ACADEMIC DASHBOARD
                </div>
                <h1 class="hero-title">Welcome back, <%= HashUtil.escapeHtml(student.getFullName()) %>.</h1>
                <p class="hero-sub">
                    Track subjects, stay ahead of deadlines, monitor your predicate,
                    and keep your daily study habit in one focused workspace.
                </p>
                <div class="hero-actions">
                    <a href="SubjectsServlet"    class="btn btn-primary">&#9654; Open dashboard flow</a>
                    <a href="AssessmentsServlet" class="btn btn-soft">&#43; Add assessment</a>
                </div>
            </div>

            <!-- RIGHT: Today's Pulse Card -->
            <div class="card">
                <div class="card-header">
                    <div class="kicker">Today's pulse</div>
                    <h2>What needs attention first</h2>
                </div>
                <div class="card-body">
                    <%
                        if (upcoming != null && !upcoming.isEmpty()) {
                            Assessment next = upcoming.get(0);
                    %>
                        <div class="pulse-item">
                            <div class="pi-top">
                                <div>
                                    <div class="pi-title"><%= HashUtil.escapeHtml(next.getTitle()) %></div>
                                    <div class="pi-desc">
                                        <%= HashUtil.escapeHtml(next.getSubjectCode()) %> &middot;
                                        <%= HashUtil.escapeHtml(next.getAssessmentType()) %> &middot;
                                        <%= next.getWeight() %>%
                                    </div>
                                </div>
                                <span class="badge"><%= HashUtil.escapeHtml(next.getStatus()) %></span>
                            </div>
                        </div>
                    <%
                        } else {
                    %>
                        <div class="pulse-item">
                            <div class="pi-top">
                                <div>
                                    <div class="pi-title">No upcoming assessments</div>
                                    <div class="pi-desc">You are all caught up for now.</div>
                                </div>
                            </div>
                        </div>
                    <%
                        }
                    %>
                    <div class="pulse-grid">
                        <div class="pulse-mini">
                            <div class="pm-label">TARGET</div>
                            <div class="pm-value"><%= student.getTargetPredicate() %>%</div>
                            <div class="pm-sub">Your current academic target.</div>
                        </div>
                        <div class="pulse-mini">
                            <div class="pm-label">STREAK</div>
                            <div class="pm-value"><%= currentStreak %> days</div>
                            <div class="pm-sub">
                                <%= currentStreak == 0 ? "Log a node today to start." : "Keep it alive today!" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</section>

<!-- ── STAT CARDS ── -->
<div class="container">
    <div class="stat-grid">

        <div class="stat-card">
            <div class="stat-top">
                <div>
                    <div class="stat-label">Study streak</div>
                    <div class="stat-value"><%= currentStreak %> days</div>
                </div>
                <div class="icon-well">&#128293;</div>
            </div>
            <div class="stat-helper">
                <%= currentStreak == 0 ? "Complete a node today to start your streak" : "Keep today alive with one completed node" %>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-top">
                <div>
                    <div class="stat-label">Current predicate</div>
                    <div class="stat-value">
                        <%= overallPredicate.compareTo(BigDecimal.ZERO) == 0 ? "N/A" : overallPredicate + "%" %>
                    </div>
                </div>
                <div class="icon-well">&#127919;</div>
            </div>
            <div class="stat-helper">
                <%= overallPredicate.compareTo(BigDecimal.ZERO) == 0
                    ? "Add marked assessments to calculate your predicate"
                    : "Across weighted assessments this semester" %>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-top">
                <div>
                    <div class="stat-label">Due this week</div>
                    <div class="stat-value"><%= dueThisWeek %> items</div>
                </div>
                <div class="icon-well">&#128197;</div>
            </div>
            <div class="stat-helper">
                <%= dueThisWeek == 0 ? "Nothing due this week" : "Stay on top of your deadlines" %>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-top">
                <div>
                    <div class="stat-label">Progress this term</div>
                    <div class="stat-value"><%= progressPercent %>%</div>
                </div>
                <div class="icon-well">&#9203;</div>
            </div>
            <div class="stat-helper">
                <%= totalNodes == 0 ? "Enroll in subjects to track progress"
                    : completedNodes + " of " + totalNodes + " learning nodes completed" %>
            </div>
        </div>

    </div>
</div>

<!-- ── MAIN DASHBOARD GRID ── -->
<section class="section">
    <div class="container">
        <div class="dash-grid">

            <!-- LEFT: Subjects this semester -->
            <div>
                <div class="section-header">
                    <span class="section-kicker">University-aware subjects</span>
                    <h2 class="section-title">This semester</h2>
                </div>

                <%
                    if (enrollments == null || enrollments.isEmpty()) {
                %>
                    <div class="subject-card">
                        <div class="subject-top">
                            <div>
                                <div class="next-node">
                                    No subjects enrolled yet. Go to
                                    <a href="SubjectsServlet">Subjects</a> to enroll.
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
                                <div class="progress-fill"
                                     style="width:<%= e.getProgressPercent() %>%;"></div>
                            </div>
                        </div>
                    </div>
                <%
                        }
                    }
                %>
            </div>

            <!-- RIGHT: Upcoming assessments -->
            <div>
                <div class="section-header">
                    <span class="section-kicker">Next 7 days</span>
                    <h2 class="section-title">Upcoming assessments</h2>
                </div>

                <%
                    if (upcoming == null || upcoming.isEmpty()) {
                %>
                    <div class="assessment-item">
                        <div class="a-top">
                            <div>
                                <div class="a-title">No upcoming assessments</div>
                                <div class="a-meta">Add assessments to see them here</div>
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

        </div>
    </div>
</section>

</body>
</html>