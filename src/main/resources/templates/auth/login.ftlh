<#import "../layout.ftlh" as layout>
<@layout.mainLayout title="Вход">
    <div class="post-form" style="max-width: 400px; margin: 0 auto; width: 100%;">
        <h2 style="margin-top: 0; text-align: center;">Вход в Microgram</h2>

        <#if registered??>
            <p style="color: #388e3c; font-size: 14px; text-align: center;">Регистрация прошла успешно, теперь можно войти.</p>
        </#if>
        <#if RequestParameters.error??>
            <p class="form-error" style="text-align: center;">Неверный логин или пароль</p>
        </#if>
        <#if RequestParameters.logout??>
            <p style="color: var(--text-muted); font-size: 14px; text-align: center;">Вы вышли из системы</p>
        </#if>

        <form method="post" action="/login" style="display: flex; flex-direction: column; gap: 14px;">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <label>
                Логин или email
                <input type="text" name="usernameOrEmail" style="padding: 8px; border: 1px solid var(--border-color); border-radius: 6px;" required/>
            </label>
            <label>
                Пароль
                <input type="password" name="password" style="padding: 8px; border: 1px solid var(--border-color); border-radius: 6px;" required/>
            </label>

            <button type="submit" style="background: var(--accent); color: #fff; border: none; border-radius: 8px; padding: 10px; font-weight: 600;">Войти</button>
        </form>

        <p style="text-align: center; font-size: 14px; margin-bottom: 0;">
            Нет аккаунта? <a href="/register" style="color: var(--accent); font-weight: 600;">Зарегистрироваться</a>
        </p>
    </div>
</@layout.mainLayout>