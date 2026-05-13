<%-- 
    Document   : quiz-result
    Created on : May 2026
    Author     : rerah
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="studysync.model.Student"%>
<%@page import="studysync.model.Node"%>
<%@page import="studysync.util.HashUtil"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Quiz Result · StudySync</title>
    <link rel="stylesheet" href="css/studysync.css"/>
</head>
<body>
<%
    Student student  = (Student) session.getAttribute("student");
    Node node        = (Node) request.getAttribute("node");
    Integer score    = (Integer) request.getAttribute("score");
    Boolean passed   = (Boolean) request.getAttribute("passed");
    Integer correct  = (Integer) request.getAttribute("correct");
    Integer total    = (Integer) request.getAttribute("total");
    Boolean already  = (Boolean) request.getAttribute("already");

    if (score   == null) score   = 0;
    if (passed  == null) passed  = false;
    if (correct == null) correct = 0;
    if (total   == null) total   = 0;
    if (already == null) already = false;
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
    <div class="container" style="max-width: 600px;">

        <div class="section-header">
            <span class="section-kicker">
                <%= node != null ? HashUtil.escapeHtml(node.getSubjectCode()) : "" %>
            </span>
            <h2 class="section-title">Quiz Result</h2>
        </div>

        <% if (already) { %>
            <div class="card">
                <div class="card-body" style="text-align:center;">
                    <div style="font-size:3rem; margin-bottom:1rem;">&#9989;</div>
                    <h2 style="margin-bottom:0.5rem;">Already passed!</h2>
                    <p class="text-muted">
                        You have already passed the quiz for
                        <strong><%= node != null ? HashUtil.escapeHtml(node.getTitle()) : "" %></strong>.
                        This node is marked complete.
                    </p>
                    <div style="margin-top:1.5rem;">
                        <a href="NodeServlet?enrollmentId=<%= node != null ? node.getEnrollmentId() : "" %>"
                           class="btn btn-primary">
                            &#8592; Back to nodes
                        </a>
                    </div>
                </div>
            </div>

        <% } else if (passed) { %>
            <div class="card">
                <div class="card-body" style="text-align:center;">
                    <div style="font-size:3rem; margin-bottom:1rem;">&#127881;</div>
                    <h2 style="margin-bottom:0.5rem;">You passed!</h2>
                    <p class="text-muted">
                        You scored <strong><%= score %>%</strong>
                        (<%= correct %> out of <%= total %> correct).
                        This node has been marked complete and your streak updated.
                    </p>
                    <div class="stat-grid" style="margin-top:1.5rem; margin-bottom:1.5rem;">
                        <div class="stat-card">
                            <div class="stat-top">
                                <div>
                                    <div class="stat-label">Your score</div>
                                    <div class="stat-value"><%= score %>%</div>
                                </div>
                                <div class="icon-well">&#127919;</div>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-top">
                                <div>
                                    <div class="stat-label">Correct answers</div>
                                    <div class="stat-value"><%= correct %>/<%= total %></div>
                                </div>
                                <div class="icon-well">&#9989;</div>
                            </div>
                        </div>
                    </div>
                    <a href="NodeServlet?enrollmentId=<%= node != null ? node.getEnrollmentId() : "" %>"
                       class="btn btn-primary">
                        &#8592; Back to nodes
                    </a>
                </div>
            </div>

        <% } else { %>
            <div class="card">
                <div class="card-body" style="text-align:center;">
                    <div style="font-size:3rem; margin-bottom:1rem;">&#128546;</div>
                    <h2 style="margin-bottom:0.5rem;">Not quite</h2>
                    <p class="text-muted">
                        You scored <strong><%= score %>%</strong>
                        (<%= correct %> out of <%= total %> correct).
                        You need 60% to pass. Review your notes and try again.
                    </p>
                    <div class="stat-grid" style="margin-top:1.5rem; margin-bottom:1.5rem;">
                        <div class="stat-card">
                            <div class="stat-top">
                                <div>
                                    <div class="stat-label">Your score</div>
                                    <div class="stat-value"><%= score %>%</div>
                                </div>
                                <div class="icon-well">&#127919;</div>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-top">
                                <div>
                                    <div class="stat-label">Pass threshold</div>
                                    <div class="stat-value">60%</div>
                                </div>
                                <div class="icon-well">&#127937;</div>
                            </div>
                        </div>
                    </div>
                    <a href="QuizServlet?nodeId=<%= node != null ? node.getNodeId() : "" %>"
                       class="btn btn-primary">
                        &#8635; Try again
                    </a>
                </div>
            </div>
        <% } %>

    </div>
</section>
</body>
</html>