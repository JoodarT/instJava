<#macro mainLayout title="Microgram">
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} — Microgram</title>
    <link rel="stylesheet" href="/css/style.css">
    <#if _csrf??>
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    </#if>
</head>
<body>
<header class="topbar">
    <div class="topbar-inner">
        <a class="brand" href="/">Microgram</a>

        <form class="search-form" action="/search" method="get">
            <input type="text" name="q" placeholder="Поиск людей..." value="${(RequestParameters.q)!''}">
        </form>

        <nav class="nav-links">
            <#if currentUser??>
                <a href="/posts/new" title="Создать публикацию">+ Пост</a>
                <a href="/users/${currentUser.username}">${currentUser.username}</a>
                <form action="/logout" method="post" class="logout-form">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="link-button">Выйти</button>
                </form>
            <#else>
                <a href="/login">Войти</a>
                <a href="/register">Регистрация</a>
            </#if>
        </nav>
    </div>
</header>

<main class="page">
    <#nested/>
</main>

<script src="/js/app.js"></script>
</body>
</html>
</#macro>
