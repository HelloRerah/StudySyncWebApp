<%-- 
    Document   : nodes
    Created on : May 2026
    Author     : rerah
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="studysync.model.Student"%>
<%@page import="studysync.model.Node"%>
<%@page import="studysync.model.Enrollment"%>
<%@page import="studysync.util.HashUtil"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Nodes · StudySync</title>
        <link rel="stylesheet" href="css/studysync.css"/>
    </head>
    <body>
        <%
            Student student = (Student) session.getAttribute("student");
            Enrollment enrollment = (Enrollment) request.getAttribute("enrollment");
            List<Node> nodes = (List<Node>) request.getAttribute("nodes");
            Integer completed = (Integer) request.getAttribute("completed");
            Integer total = (Integer) request.getAttribute("total");
            String error = request.getParameter("error");

            if (completed == null) {
                completed = 0;
            }
            if (total == null) {
                total = 0;
            }

            int progressPercent = total == 0 ? 0
                    : (int) Math.round((completed * 100.0) / total);
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

                <!-- Header -->
                <div class="section-header">
                    <span class="section-kicker">
                        <%= enrollment != null ? HashUtil.escapeHtml(enrollment.getSubjectCode()) : ""%>
                    </span>
                    <h2 class="section-title">
                        <%= enrollment != null ? HashUtil.escapeHtml(enrollment.getSubjectName()) : "Learning Nodes"%>
                    </h2>
                </div>

                <!-- Progress bar -->
                <div class="progress-bar-wrap" style="margin-bottom: 2rem;">
                    <div class="progress-label">
                        <span class="text-sm text-muted">Progress — <%= completed%> of <%= total%> nodes completed</span>
                        <span class="text-sm font-semibold"><%= progressPercent%>%</span>
                    </div>
                    <div class="progress-track">
                        <div class="progress-fill" style="width: <%= progressPercent%>%;"></div>
                    </div>
                </div>

                <% if ("1".equals(error)) { %>
                <div class="alert alert-error">Please enter a node title.</div>
                <% } %>

                <div class="dash-grid">

                    <!-- LEFT: Node list -->
                    <div>
                        <div class="section-header">
                            <span class="section-kicker">Study path</span>
                            <h2 class="section-title">Learning nodes</h2>
                        </div>

                        <%
                            if (nodes == null || nodes.isEmpty()) {
                        %>
                        <div class="subject-card">
                            <div class="subject-top">
                                <div>
                                    <div class="next-node">
                                        No nodes yet. Add your first node using the form.
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%
                        } else {
                            for (Node n : nodes) {
                        %>
                        <div class="subject-card" style="opacity: <%= n.isCompleted() ? "0.6" : "1"%>;">
                            <div class="subject-top">
                                <div>
                                    <div class="flex items-center gap-2">
                                        <span style="font-size:1.1rem;">
                                            <%= n.isCompleted() ? "&#9989;" : "&#9711;"%>
                                        </span>
                                        <span class="font-semibold"
                                              style="<%= n.isCompleted() ? "text-decoration:line-through;" : ""%>">
                                            <%= HashUtil.escapeHtml(n.getTitle())%>
                                        </span>
                                    </div>
                                    <% if (n.getDescription() != null && !n.getDescription().isBlank()) {%>
                                    <div class="subject-meta" style="margin-top: 0.25rem;">
                                        <%= HashUtil.escapeHtml(n.getDescription())%>
                                    </div>
                                    <% } %>
                                </div>
                                <% if (!n.isCompleted()) { %>
                                <span class="badge" style="font-size:0.75rem; padding: 0.4rem 0.9rem; opacity:0.7;">
                                    &#128274; Quiz coming soon
                                </span>
                                <% } %>
                            </div>
                        </div>
                        <%
                                }
                            }
                        %>
                    </div>

                    <!-- RIGHT: Add node form -->
                    <div>
                        <div class="card">
                            <div class="card-header">
                                <div class="kicker">Add</div>
                                <h2>New learning node</h2>
                            </div>
                            <div class="card-body">
                                <form action="NodeServlet" method="POST">
                                    <input type="hidden" name="action"       value="add"/>
                                    <input type="hidden" name="enrollmentId" value="<%= enrollment != null ? enrollment.getEnrollmentId() : ""%>"/>
                                    <div class="form-row">
                                        <label class="form-label">Node title</label>
                                        <input class="form-input" type="text"
                                               name="title"
                                               placeholder="e.g. Linked Lists — traversal and insertion"
                                               required/>
                                    </div>
                                    <div class="form-row">
                                        <label class="form-label">Description — optional</label>
                                        <input class="form-input" type="text"
                                               name="description"
                                               placeholder="What this node covers"/>
                                    </div>
                                    <button class="btn btn-primary w-full mt-3" type="submit">
                                        &#43; Add node
                                    </button>
                                </form>
                            </div>
                        </div>

                        <div class="highlight-block mt-3">
                            <div class="text-sm font-semibold">&#128293; How nodes work</div>
                            <div class="text-sm text-muted mt-1">
                                Add a node for each topic you need to study. Take the AI quiz
                                and score 60% or higher to mark it complete and keep your streak alive.
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </section>
    </body>
</html>