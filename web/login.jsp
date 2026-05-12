<%-- 
    Document   : login
    Created on : May 11, 2026, 12:04:43 PM
    Author     : rerah
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Login · StudySync</title>
    <link rel="stylesheet" href="css/studysync.css"/>
</head>
<body>
<div class="hero-backdrop"></div>
<div class="login-wrap">
    <div class="login-card">
        <!-- Logo -->
        <div class="login-logo">
            <div style="font-size: 2.5rem; margin-bottom: 0.5rem;">&#127979;</div>
            <span>StudySync</span>
            <p class="text-sm text-muted" style="margin-top: 0.25rem;">Personal academic dashboard</p>
        </div>

        <!-- Messages -->
        <%
            String error = request.getParameter("error");
            String registered = request.getParameter("registered");
            if ("1".equals(error)) {
        %>
            <div class="alert alert-error">
                Invalid student number or password. Please try again.
            </div>
        <% } else if ("1".equals(registered)) { %>
            <div class="alert alert-success">
                Account created successfully. Please sign in.
            </div>
        <% } %>

        <!-- Login Form -->
        <form action="LoginServlet" method="POST">
            <div class="form-row">
                <label class="form-label" for="studentId">Student Number</label>
                <input class="form-input"
                       type="text"
                       id="studentId"
                       name="studentId"
                       placeholder="e.g. 219123456"
                       required/>
            </div>
            <div class="form-row">
                <label class="form-label" for="password">Password</label>
                <input class="form-input"
                       type="password"
                       id="password"
                       name="password"
                       placeholder="Enter your password"
                       required/>
            </div>
            <button class="btn btn-primary w-full mt-3" type="submit">
                Sign in
            </button>
        </form>

        <!-- Register link -->
        <div style="text-align: center; margin-top: 1.25rem;">
            <p class="text-sm text-muted">
                Don't have an account?
                <a href="register.jsp" style="color: var(--primary); font-weight: 600;">Register</a>
            </p>
        </div>
        <!-- Footer note -->
        <div style="text-align: center; margin-top: 1.5rem; padding-top: 1rem; border-top: 1px solid var(--border);">
            <p class="text-xs text-muted">
                StudySync is independent of your university's LMS.<br/>
                Your data stays private and is never shared.
            </p>
        </div>
    </div>
</div>
</body>
</html>