<#-- Аватар с надёжным фолбэком: если avatarPath задан, но файл не грузится (404 и т.п.),
     onerror удаляет <img>, открывая инициал логина, который лежит под ним всегда. -->
<#macro avatar avatarPath username size="sm">
<span class="avatar avatar-${size}">
    <#if avatarPath??>
    <img src="${avatarPath}" alt="${username}" onerror="this.remove()">
    </#if>
    ${username?substring(0,1)?upper_case}
</span>
</#macro>
