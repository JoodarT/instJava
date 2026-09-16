<#import "/spring.ftl" as spring/>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация — Microgram</title>
</head>
<body>
<h1>Регистрация</h1>

<@spring.bind "registrationRequest"/>
<#list spring.status.errorMessages as error>
    <div style="color:red">${error}</div>
</#list>

<form method="post" action="/register">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <div>
        <label>Логин</label>
        <input type="text" name="username" value="${registrationRequest.username()!}"/>
        <@spring.bind "registrationRequest.username"/>
        <#list spring.status.errorMessages as error>
            <div style="color:red">${error}</div>
        </#list>
    </div>

    <div>
        <label>Email</label>
        <input type="email" name="email" value="${registrationRequest.email()!}"/>
        <@spring.bind "registrationRequest.email"/>
        <#list spring.status.errorMessages as error>
            <div style="color:red">${error}</div>
        </#list>
    </div>

    <div>
        <label>Пароль</label>
        <input type="password" name="password"/>
        <@spring.bind "registrationRequest.password"/>
        <#list spring.status.errorMessages as error>
            <div style="color:red">${error}</div>
        </#list>
    </div>

    <div>
        <label>Полное имя</label>
        <input type="text" name="fullName" value="${registrationRequest.fullName()!}"/>
    </div>

    <button type="submit">Зарегистрироваться</button>
</form>

<p>Уже есть аккаунт? <a href="/login">Войти</a></p>
</body>
</html>
