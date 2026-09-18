<#import "layout.ftlh" as layout>
<#import "fragments/avatar.ftlh" as av>
<@layout.mainLayout title="Поиск">
    <form class="search-page-form" action="/search" method="get">
        <input type="text" name="q" placeholder="Логин, email или имя" value="${(query)!''}">
        <button type="submit">Найти</button>
    </form>

    <#if query?? && query?has_content>
        <#if results?? && (results?size > 0)>
        <ul class="user-results">
            <#list results as user>
            <li>
                <a href="/users/${user.username}" class="user-result">
                    <@av.avatar avatarPath=user.avatarPath username=user.username size="sm"/>
                    <span>
                        <strong>${user.username}</strong>
                        <#if user.fullName?? && user.fullName?has_content> — ${user.fullName}</#if>
                    </span>
                </a>
            </li>
            </#list>
        </ul>
        <#else>
        <div class="empty-state"><p>Никого не найдено по запросу «${query}».</p></div>
        </#if>
    </#if>
</@layout.mainLayout>
