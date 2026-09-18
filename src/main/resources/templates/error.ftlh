<#import "layout.ftlh" as layout>
<@layout.mainLayout title="Ошибка ${status!500}">
    <div class="error-page">
        <h1>${status!500}</h1>
        <p>${message!"Что-то пошло не так"}</p>
        <a href="/">На главную</a>
    </div>
</@layout.mainLayout>
