<%-- 
    Document   : quiz
    Created on : May 2026
    Author     : rerah
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="studysync.model.Student"%>
<%@page import="studysync.model.Node"%>
<%@page import="studysync.model.Question"%>
<%@page import="studysync.util.HashUtil"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Quiz · StudySync</title>
    <link rel="stylesheet" href="css/studysync.css"/>
</head>
<body>
<%
    Student student        = (Student) session.getAttribute("student");
    Node node              = (Node) request.getAttribute("node");
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    Integer attempts       = (Integer) request.getAttribute("attempts");
    Integer bestScore      = (Integer) request.getAttribute("bestScore");
    String error           = request.getParameter("error");
    if (attempts  == null) attempts  = 0;
    if (bestScore == null) bestScore = 0;
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
            <span class="section-kicker">
                <%= node != null ? HashUtil.escapeHtml(node.getSubjectCode()) : "" %>
            </span>
            <h2 class="section-title">
                <%= node != null ? HashUtil.escapeHtml(node.getTitle()) : "Quiz" %>
            </h2>
        </div>

        <% if ("1".equals(error)) { %>
            <div class="alert alert-error">Please paste some study material before generating.</div>
        <% } else if ("2".equals(error)) { %>
            <div class="alert alert-error">Could not generate questions. Please try again.</div>
        <% } %>

        <%-- GENERATE FORM — shown when no questions exist yet --%>
        <% if (questions == null || questions.isEmpty()) { %>
            <div class="dash-grid">
                <div>
                    <div class="card">
                        <div class="card-header">
                            <div class="kicker">Step 1</div>
                            <h2>Paste your study material</h2>
                        </div>
                        <div class="card-body">
                            <form action="QuizServlet" method="POST">
                                <input type="hidden" name="action" value="generate"/>
                                <input type="hidden" name="nodeId"
                                       value="<%= node != null ? node.getNodeId() : "" %>"/>
                                <div class="form-row">
                                    <label class="form-label">Study notes or content</label>
                                    <textarea class="form-input"
                                              name="studyContent"
                                              rows="10"
                                              placeholder="Paste your notes, textbook content, or any study material here. The AI will generate 5 quiz questions from it."
                                              style="resize:vertical; font-family: inherit;"
                                              required></textarea>
                                </div>
                                <button class="btn btn-primary w-full mt-3" type="submit">
                                    &#9889; Generate Quiz
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
                <div>
                    <div class="highlight-block">
                        <div class="text-sm font-semibold">&#127775; How it works</div>
                        <div class="text-sm text-muted mt-1">
                            Paste your study notes or any content related to this node.
                            The AI will generate 5 multiple choice questions. You need
                            to score 60% or higher to mark this node as complete.
                        </div>
                    </div>
                    <% if (attempts > 0) { %>
                        <div class="stat-card mt-3">
                            <div class="stat-top">
                                <div>
                                    <div class="stat-label">Previous attempts</div>
                                    <div class="stat-value"><%= attempts %></div>
                                </div>
                                <div class="icon-well">&#128196;</div>
                            </div>
                            <div class="stat-helper">Best score: <%= bestScore %>%</div>
                        </div>
                    <% } %>
                </div>
            </div>

        <%-- QUIZ FORM — shown when questions exist --%>
        <% } else { %>
            <% if (attempts > 0) { %>
                <div class="alert alert-error" style="margin-bottom:1rem;">
                    You scored <%= bestScore %>% last time. You need 60% to pass.
                    Generate new questions or try again below.
                </div>
            <% } %>

            <div class="dash-grid">
                <div>
                    <form action="QuizServlet" method="POST">
                        <input type="hidden" name="action" value="submit"/>
                        <input type="hidden" name="nodeId"
                               value="<%= node != null ? node.getNodeId() : "" %>"/>

                        <%
                            int qNum = 1;
                            for (Question q : questions) {
                        %>
                            <div class="card mb-4">
                                <div class="card-body">
                                    <p class="font-semibold" style="margin-bottom:1rem;">
                                        <%= qNum %>. <%= HashUtil.escapeHtml(q.getQuestionText()) %>
                                    </p>
                                    <div style="display:flex; flex-direction:column; gap:0.5rem;">
                                        <label style="display:flex; align-items:center; gap:0.5rem; cursor:pointer;">
                                            <input type="radio" name="q<%= q.getQuestionId() %>" value="A" required/>
                                            <span>A. <%= HashUtil.escapeHtml(q.getOptionA()) %></span>
                                        </label>
                                        <label style="display:flex; align-items:center; gap:0.5rem; cursor:pointer;">
                                            <input type="radio" name="q<%= q.getQuestionId() %>" value="B"/>
                                            <span>B. <%= HashUtil.escapeHtml(q.getOptionB()) %></span>
                                        </label>
                                        <label style="display:flex; align-items:center; gap:0.5rem; cursor:pointer;">
                                            <input type="radio" name="q<%= q.getQuestionId() %>" value="C"/>
                                            <span>C. <%= HashUtil.escapeHtml(q.getOptionC()) %></span>
                                        </label>
                                        <label style="display:flex; align-items:center; gap:0.5rem; cursor:pointer;">
                                            <input type="radio" name="q<%= q.getQuestionId() %>" value="D"/>
                                            <span>D. <%= HashUtil.escapeHtml(q.getOptionD()) %></span>
                                        </label>
                                    </div>
                                </div>
                            </div>
                        <%
                                qNum++;
                            }
                        %>

                        <button class="btn btn-primary w-full" type="submit">
                            &#9989; Submit answers
                        </button>
                    </form>
                </div>

                <div>
                    <div class="card">
                        <div class="card-header">
                            <div class="kicker">Regenerate</div>
                            <h2>Try different questions</h2>
                        </div>
                        <div class="card-body">
                            <form action="QuizServlet" method="POST">
                                <input type="hidden" name="action" value="generate"/>
                                <input type="hidden" name="nodeId"
                                       value="<%= node != null ? node.getNodeId() : "" %>"/>
                                <div class="form-row">
                                    <label class="form-label">Paste new study material</label>
                                    <textarea class="form-input"
                                              name="studyContent"
                                              rows="6"
                                              placeholder="Paste different notes to generate new questions."
                                              style="resize:vertical; font-family: inherit;"
                                              required></textarea>
                                </div>
                                <button class="btn btn-soft w-full mt-3" type="submit">
                                    &#9889; Generate new questions
                                </button>
                            </form>
                        </div>
                    </div>
                    <div class="stat-card mt-3">
                        <div class="stat-top">
                            <div>
                                <div class="stat-label">Attempts</div>
                                <div class="stat-value"><%= attempts %></div>
                            </div>
                            <div class="icon-well">&#128196;</div>
                        </div>
                        <div class="stat-helper">
                            Pass threshold: 60% — Best score: <%= bestScore %>%
                        </div>
                    </div>
                </div>
            </div>
        <% } %>

    </div>
</section>
</body>
</html>