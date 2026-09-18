<#import "avatar.ftlh" as av>
<#macro postCard post>
<article class="post-card" data-post-id="${post.id?c}">
    <header class="post-header">
        <a href="/users/${post.author.username}" class="post-author">
            <@av.avatar avatarPath=post.author.avatarPath username=post.author.username size="sm"/>
            <span class="post-author-name">${post.author.username}</span>
        </a>
        <#if post.owner>
            <button type="button" class="link-button danger btn-delete-post" data-post-id="${post.id?c}">Удалить</button>
        </#if>
    </header>

    <img class="post-image" src="${post.imagePath}" alt="post image">

    <div class="post-body">
        <div class="post-actions">
            <button type="button" class="btn-like<#if post.likedByCurrentUser> liked</#if>"
                    data-post-id="${post.id?c}" data-liked="${post.likedByCurrentUser?c}">
                <#if post.likedByCurrentUser>♥<#else>♡</#if>
            </button>
            <span class="likes-count">${post.likesCount}</span>
        </div>

        <#if post.caption?? && post.caption?has_content>
        <p class="post-caption"><strong>${post.author.username}</strong> ${post.caption}</p>
        </#if>

        <button type="button" class="link-button btn-toggle-comments" data-post-id="${post.id?c}">
            Комментарии (<span class="comments-count">${post.commentsCount}</span>)
        </button>

        <div class="comments-section" data-post-id="${post.id?c}" hidden>
            <ul class="comments-list"></ul>
            <form class="add-comment-form" data-post-id="${post.id?c}">
                <input type="text" name="text" placeholder="Добавить комментарий..." maxlength="2000" required>
                <button type="submit">Отправить</button>
            </form>
        </div>
    </div>
</article>
</#macro>
