<#import "/spring.ftl" as spring/>
<#import "../layout.ftlh" as layout>
<@layout.mainLayout title="Регистрация">
    <div class="post-form" style="max-width: 400px; margin: 0 auto; width: 100%;">
        <h2 style="margin-top: 0; text-align: center;">Регистрация в Microgram</h2>

        <#-- Вывод глобальных ошибок (например, "Username already taken") -->
        <@spring.bind "registrationRequest"/>
        <#if spring.status.errorMessages?has_content>
            <div style="margin-bottom: 10px;">
                <#list spring.status.errorMessages as error>
                    <div class="form-error" style="text-align: center; margin-bottom: 6px;">${error}</div>
                </#list>
            </div>
        </#if>

        <form method="post" action="/register" style="display: flex; flex-direction: column; gap: 14px;">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <label>
                Логин
                <input type="text" name="username" value="${registrationRequest.username()!}" style="padding: 8px; border: 1px solid var(--border-color); border-radius: 6px;" required/>
                <@spring.bind "registrationRequest.username"/>
                <#list spring.status.errorMessages as error>
                    <span class="form-error">${error}</span>
                </#list>
            </label>

            <label>
                Email
                <input type="email" name="email" value="${registrationRequest.email()!}" style="padding: 8px; border: 1px solid var(--border-color); border-radius: 6px;" required/>
                <@spring.bind "registrationRequest.email"/>
                <#list spring.status.errorMessages as error>
                    <span class="form-error">${error}</span>
                </#list>
            </label>

            <label>
                Пароль
                <input type="password" name="password" style="padding: 8px; border: 1px solid var(--border-color); border-radius: 6px;" required/>
                <@spring.bind "registrationRequest.password"/>
                <#list spring.status.errorMessages as error>
                    <span class="form-error">${error}</span>
                </#list>
            </label>

            <label>
                Полное имя
                <input type="text" name="fullName" value="${registrationRequest.fullName()!}" style="padding: 8px; border: 1px solid var(--border-color); border-radius: 6px;"/>
            </label>

            <button type="submit" style="background: var(--accent); color: #fff; border: none; border-radius: 8px; padding: 10px; font-weight: 600;">Зарегистрироваться</button>
        </form>

        <p style="text-align: center; font-size: 14px; margin-bottom: 0;">
            Уже есть аккаунт? <a href="/login" style="color: var(--accent); font-weight: 600;">Войти</a>
        </p>
    </div>
</@layout.mainLayout>