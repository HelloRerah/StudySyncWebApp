<%-- 
    Document   : streak
    Created on : May 11, 2026, 12:07:30 PM
    Author     : rerah
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="studysync.model.Student"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.DayOfWeek"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Streak · StudySync</title>
    <link rel="stylesheet" href="css/studysync.css"/>
</head>
<body>
<%
    Student student     = (Student) session.getAttribute("student");
    Integer currentStreak = (Integer) request.getAttribute("currentStreak");
    Integer bestStreak    = (Integer) request.getAttribute("bestStreak");
    Integer totalNodes    = (Integer) request.getAttribute("totalNodes");
    Boolean loggedToday   = (Boolean) request.getAttribute("loggedToday");
    List<LocalDate> thisWeek = (List<LocalDate>) request.getAttribute("thisWeek");
    if (currentStreak == null) currentStreak = 0;
    if (bestStreak    == null) bestStreak    = 0;
    if (totalNodes    == null) totalNodes    = 0;
    if (loggedToday   == null) loggedToday   = false;

    LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
    String[] dayLabels = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
%>
<div class="hero-backdrop"></div>
<nav class="navbar">
    <div class="container nav-inner">
        <div class="nav-brand"><span>&#127979;</span> StudySync</div>
        <div class="nav-links">
            <a href="DashboardServlet" class="nav-link">Dashboard</a>
            <a href="SubjectsServlet"    class="nav-link">Subjects</a>
            <a href="AssessmentsServlet" class="nav-link">Assessments</a>
            <a href="PredicateServlet"   class="nav-link">Predicate</a>
            <a href="StreakServlet"      class="nav-link active">Streak</a>
            <a href="SignOutServlet"     class="nav-link" style="color:var(--muted-fg);">Sign out</a>
        </div>
    </div>
</nav>

<section class="section">
    <div class="container">
        <div class="section-header">
            <span class="section-kicker">Daily habit</span>
            <h2 class="section-title">Study Streak</h2>
        </div>

        <div class="stat-grid">
            <div class="stat-card">
                <div class="stat-top">
                    <div>
                        <div class="stat-label">Current streak</div>
                        <div class="stat-value"><%= currentStreak %> days</div>
                    </div>
                    <div class="icon-well">&#128293;</div>
                </div>
                <div class="stat-helper">
                    <%= loggedToday ? "You have logged today. Keep it up!" : "Complete a node today to keep your streak alive" %>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-top">
                    <div>
                        <div class="stat-label">Best streak</div>
                        <div class="stat-value"><%= bestStreak %> days</div>
                    </div>
                    <div class="icon-well">&#127942;</div>
                </div>
                <div class="stat-helper">Your personal best this semester</div>
            </div>
            <div class="stat-card">
                <div class="stat-top">
                    <div>
                        <div class="stat-label">Nodes completed</div>
                        <div class="stat-value"><%= totalNodes %></div>
                    </div>
                    <div class="icon-well">&#9989;</div>
                </div>
                <div class="stat-helper">Total study nodes marked complete</div>
            </div>
        </div>

        <div class="card mt-4">
            <div class="card-header">
                <div class="kicker">This week</div>
                <h2>Daily activity</h2>
            </div>
            <div class="card-body">
                <div class="streak-week">
                    <%
                        for (int i = 0; i < 7; i++) {
                            LocalDate day = monday.plusDays(i);
                            boolean completed = thisWeek != null && thisWeek.contains(day);
                            boolean isToday   = day.equals(LocalDate.now());
                    %>
                        <div class="streak-day">
                            <div class="day-label"><%= dayLabels[i] %></div>
                            <div class="streak-dot <%= completed ? "active" : "inactive" %>"
                                 title="<%= isToday ? "Today" : day.toString() %>"></div>
                        </div>
                    <%
                        }
                    %>
                </div>
                <div class="flex gap-3 mt-3" style="font-size: 0.8rem; color: var(--muted-fg);">
                    <div class="flex items-center gap-1">
                        <div style="width:10px;height:10px;border-radius:50%;background:var(--primary);"></div>
                        Completed
                    </div>
                    <div class="flex items-center gap-1">
                        <div style="width:10px;height:10px;border-radius:50%;background:var(--muted);"></div>
                        Not yet
                    </div>
                </div>
            </div>
        </div>

        <!-- Log today button -->
        <% if (!loggedToday) { %>
        <div class="card mt-4">
            <div class="card-body">
                <form action="StreakServlet" method="POST">
                    <p class="text-sm text-muted" style="margin-bottom: 1rem;">
                        Click below to log today's study activity and keep your streak alive.
                    </p>
                    <button class="btn btn-primary" type="submit">
                        &#9989; Log today's activity
                    </button>
                </form>
            </div>
        </div>
        <% } else { %>
        <div class="highlight-block mt-4">
            <div class="text-sm font-semibold">&#128293; Today is logged!</div>
            <div class="text-sm text-muted mt-1">
                You have already completed your study activity for today. Come back tomorrow.
            </div>
        </div>
        <% } %>

        <div class="highlight-block mt-3">
            <div class="text-sm font-semibold">&#128293; How the streak works</div>
            <div class="text-sm text-muted mt-1">
                Log in and complete at least one learning node each day to keep your streak alive.
                Miss a day and it resets to 1. The streak motivates daily consistent study — not
                cramming at the last minute.
            </div>
        </div>

    </div>
</section>
</body>
</html>