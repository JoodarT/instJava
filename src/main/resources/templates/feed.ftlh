<#import "layout.ftlh" as layout>
<#import "fragments/postCard.ftlh" as fragments>
<@layout.mainLayout title="Лента">
    <#if posts?? && (posts?size > 0)>
        <#list posts as post>
            <@fragments.postCard post=post/>
        </#list>
    <#else>
        <div class="empty-state">
            <p>Ваша лента пуста — вы ни на кого не подписаны.</p>
            <a href="/search">Найти людей</a>
        </div>
    </#if>
</@layout.mainLayout>
