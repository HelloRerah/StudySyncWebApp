<%-- 
    Document   : register
    Created on : May 11, 2026, 12:06:21 PM
    Author     : rerah
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Register · StudySync</title>
    <link rel="stylesheet" href="css/studysync.css"/>
</head>
<body>
<div class="hero-backdrop"></div>
<!-- NAVBAR -->
<nav class="navbar">
    <div class="container nav-inner">
        <div class="nav-brand"><span>&#127979;</span> StudySync</div>
        <div class="nav-links">
            <a href="login.jsp" class="nav-link">Login</a>
            <a href="register.jsp" class="nav-link active">Register</a>
        </div>
    </div>
</nav>
<section class="section">
    <div class="container" style="max-width: 480px;">
        <!-- Header -->
        <div class="section-header">
            <span class="section-kicker">Create account</span>
            <h2 class="section-title">Register for StudySync</h2>
        </div>
        <!-- Note -->
        <div class="register-note">
            StudySync is independent of your university LMS. Your data stays private and is never shared.
        </div>

        <!-- Error messages -->
        <%
            String error = request.getParameter("error");
            if ("1".equals(error)) {
        %>
            <div class="alert alert-error">Please fill in all fields.</div>
        <% } else if ("2".equals(error)) { %>
            <div class="alert alert-error">Student number already registered.</div>
        <% } else if ("3".equals(error)) { %>
            <div class="alert alert-error">Email already registered.</div>
        <% } %>

        <!-- Registration form -->
        <form action="RegisterServlet" method="POST">
            <div class="form-row">
                <label class="form-label" for="studentId">Student Number</label>
                <input class="form-input" type="text" id="studentId" name="studentId" placeholder="e.g. 219123456" required/>
            </div>
            <div class="form-row">
                <label class="form-label" for="name">Full Name</label>
                <input class="form-input" type="text" id="name" name="name" placeholder="Your full name" required/>
            </div>
            <div class="form-row">
                <label class="form-label" for="email">Email</label>
                <input class="form-input" type="email" id="email" name="email" placeholder="you@example.com" required/>
            </div>
            <div class="form-row">
                <label class="form-label" for="password">Password</label>
                <input class="form-input" type="password" id="password" name="password" placeholder="Choose a password" required/>
            </div>
            <button class="btn btn-primary w-full mt-3" type="submit">
                &#43; Create account
            </button>
        </form>
        <!-- Footer -->
        <div style="text-align:center; margin-top:1.5rem; font-size:0.85rem; color:var(--muted-fg);">
            Already have an account? <a href="login.jsp" style="color:var(--primary); font-weight:600;">Sign in</a>
        </div>
    </div>
</section>
</body>
</html>