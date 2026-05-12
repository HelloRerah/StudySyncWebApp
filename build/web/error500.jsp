<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Server Error · StudySync</title>
    <link rel="stylesheet" href="css/studysync.css"/>
</head>
<body>
<div class="hero-backdrop"></div>
<nav class="navbar">
    <div class="container nav-inner">
        <div class="nav-brand"><span>&#127979;</span> StudySync</div>
    </div>
</nav>
<section class="section">
    <div class="container" style="max-width: 480px; text-align: center;">
        <div style="font-size: 4rem; margin-bottom: 1rem;">&#9889;</div>
        <h2 class="section-title">Something went wrong</h2>
        <p class="text-muted" style="margin: 1rem 0 2rem;">
            A server error occurred. Please try again. If the problem
            persists, contact support.
        </p>
        <a href="DashboardServlet" class="btn btn-primary">&#8592; Back to dashboard</a>
    </div>
</section>
</body>
</html>