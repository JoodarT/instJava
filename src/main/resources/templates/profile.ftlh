<#import "layout.ftlh" as layout>
<#import "fragments/postCard.ftlh" as fragments>
<#import "fragments/avatar.ftlh" as av>
<@layout.mainLayout title=profile.username>
    <section class="profile-header">
        <@av.avatar avatarPath=profile.avatarPath username=profile.username size="lg"/>

        <div class="profile-info">
            <div class="profile-name-row">
                <h1>${profile.username}</h1>
                <#if !profile.currentUser>
                    <button type="button" class="btn-follow<#if profile.following> following</#if>"
                            data-user-id="${profile.id?c}" data-following="${profile.following?c}">
                        <#if profile.following>Отписаться<#else>Подписаться</#if>
                    </button>
                </#if>
            </div>
            <#if profile.fullName?? && profile.fullName?has_content>
            <p class="profile-fullname">${profile.fullName}</p>
            </#if>
            <#if profile.bio?? && profile.bio?has_content>
            <p class="profile-bio">${profile.bio}</p>
            </#if>

            <div class="profile-stats">
                <span><strong class="posts-count">${profile.postsCount}</strong> публикаций</span>
                <span><strong class="followers-count">${profile.followersCount}</strong> подписчиков</span>
                <span><strong class="following-count">${profile.followingCount}</strong> подписок</span>
            </div>
        </div>
    </section>

    <section class="profile-posts">
        <#if posts?? && (posts?size > 0)>
            <#list posts as post>
                <@fragments.postCard post=post/>
            </#list>
        <#else>
            <div class="empty-state"><p>Публикаций пока нет.</p></div>
        </#if>
    </section>
</@layout.mainLayout>
