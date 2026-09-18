<#import "../layout.ftlh" as layout>
<@layout.mainLayout title="Новая публикация">
    <form id="create-post-form" class="post-form">
        <label>
            Изображение
            <input type="file" name="image" accept="image/*" required>
        </label>
        <label>
            Подпись
            <textarea name="caption" maxlength="2000" placeholder="Подпись к фото..."></textarea>
        </label>
        <div class="form-error" id="create-post-error" hidden></div>
        <button type="submit">Опубликовать</button>
    </form>
</@layout.mainLayout>
