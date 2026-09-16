<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход — Microgram</title>
</head>
<body>
<h1>Вход</h1>

<#if registered??>
    <p style="color:green">Регистрация прошла успешно, теперь можно войти.</p>
</#if>
<#if RequestParameters.error??>
    <p style="color:red">Неверный логин или пароль</p>
</#if>
<#if RequestParameters.logout??>
    <p>Вы вышли из системы</p>
</#if>

<form method="post" action="/login">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <div>
        <label>Логин или email</label>
        <input type="text" name="usernameOrEmail"/>
    </div>
    <div>
        <label>Пароль</label>
        <input type="password" name="password"/>
    </div>

    <button type="submit">Войти</button>
</form>

<p>Нет аккаунта? <a href="/register">Зарегистрироваться</a></p>
</body>
</html>
