(function () {
    'use strict';

    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

    function authHeaders(extra) {
        const headers = Object.assign({}, extra);
        if (csrfToken && csrfHeader) {
            headers[csrfHeader] = csrfToken;
        }
        return headers;
    }

    function escapeHtml(str) {
        const div = document.createElement('div');
        div.textContent = str == null ? '' : str;
        return div.innerHTML;
    }

    function renderComment(comment, postId) {
        const li = document.createElement('li');
        li.className = 'comment';
        li.dataset.commentId = comment.id;

        const text = document.createElement('span');
        text.innerHTML = '<strong>' + escapeHtml(comment.author.username) + '</strong> ' + escapeHtml(comment.text);
        li.appendChild(text);

        if (comment.canDelete) {
            const btn = document.createElement('button');
            btn.type = 'button';
            btn.className = 'link-button danger btn-delete-comment';
            btn.textContent = 'Удалить';
            btn.dataset.commentId = comment.id;
            btn.dataset.postId = postId;
            li.appendChild(btn);
        }

        return li;
    }

    function loadComments(postId, section) {
        const list = section.querySelector('.comments-list');
        if (list.dataset.loaded === 'true') {
            return;
        }
        fetch('/api/posts/' + postId + '/comments')
            .then(function (res) {
                return res.json();
            })
            .then(function (comments) {
                list.innerHTML = '';
                comments.forEach(function (c) {
                    list.appendChild(renderComment(c, postId));
                });
                list.dataset.loaded = 'true';
            })
            .catch(function () {
                list.innerHTML = '<li>Не удалось загрузить комментарии</li>';
            });
    }

    document.addEventListener('click', function (e) {
        const likeBtn = e.target.closest('.btn-like');
        if (likeBtn) {
            const postId = likeBtn.dataset.postId;
            fetch('/api/posts/' + postId + '/like', {
                method: 'POST',
                headers: authHeaders()
            })
                .then(function (res) {
                    if (!res.ok) {
                        throw new Error('like failed');
                    }
                    return res.json();
                })
                .then(function (data) {
                    likeBtn.dataset.liked = data.liked;
                    likeBtn.classList.toggle('liked', data.liked);
                    likeBtn.textContent = data.liked ? '♥' : '♡';
                    const card = likeBtn.closest('.post-card');
                    card.querySelector('.likes-count').textContent = data.likesCount;
                })
                .catch(function () {
                    alert('Не удалось поставить лайк. Возможно, нужно войти в систему.');
                });
            return;
        }

        const toggleBtn = e.target.closest('.btn-toggle-comments');
        if (toggleBtn) {
            const postId = toggleBtn.dataset.postId;
            const card = toggleBtn.closest('.post-card');
            const section = card.querySelector('.comments-section');
            const wasHidden = section.hasAttribute('hidden');
            if (wasHidden) {
                loadComments(postId, section);
            }
            section.toggleAttribute('hidden');
            return;
        }

        const delCommentBtn = e.target.closest('.btn-delete-comment');
        if (delCommentBtn) {
            if (!confirm('Удалить комментарий?')) {
                return;
            }
            const commentId = delCommentBtn.dataset.commentId;
            const postId = delCommentBtn.dataset.postId;
            fetch('/api/comments/' + commentId, {
                method: 'DELETE',
                headers: authHeaders()
            })
                .then(function (res) {
                    if (!res.ok) {
                        throw new Error('delete comment failed');
                    }
                    delCommentBtn.closest('li').remove();
                    const card = document.querySelector('.post-card[data-post-id="' + postId + '"]');
                    if (card) {
                        const countEl = card.querySelector('.comments-count');
                        countEl.textContent = Math.max(0, parseInt(countEl.textContent, 10) - 1);
                    }
                })
                .catch(function () {
                    alert('Не удалось удалить комментарий');
                });
            return;
        }

        const delPostBtn = e.target.closest('.btn-delete-post');
        if (delPostBtn) {
            if (!confirm('Удалить публикацию?')) {
                return;
            }
            const postId = delPostBtn.dataset.postId;
            fetch('/api/posts/' + postId, {
                method: 'DELETE',
                headers: authHeaders()
            })
                .then(function (res) {
                    if (!res.ok) {
                        throw new Error('delete post failed');
                    }
                    delPostBtn.closest('.post-card').remove();
                })
                .catch(function () {
                    alert('Не удалось удалить публикацию');
                });
            return;
        }

        const followBtn = e.target.closest('.btn-follow');
        if (followBtn) {
            const userId = followBtn.dataset.userId;
            const isFollowing = followBtn.dataset.following === 'true';
            const url = '/api/users/' + userId + '/' + (isFollowing ? 'unfollow' : 'follow');
            fetch(url, {method: 'POST', headers: authHeaders()})
                .then(function (res) {
                    if (!res.ok) {
                        return res.json().then(function (err) {
                            throw err;
                        }).catch(function () {
                            throw new Error('follow toggle failed');
                        });
                    }
                    return res.json();
                })
                .then(function (data) {
                    followBtn.dataset.following = data.following;
                    followBtn.classList.toggle('following', data.following);
                    followBtn.textContent = data.following ? 'Отписаться' : 'Подписаться';

                    const followersCountEl = document.querySelector('.followers-count');
                    if (followersCountEl) {
                        followersCountEl.textContent = data.followersCount;
                    }
                })
                .catch(function (err) {
                    alert((err && err.message) || 'Не удалось выполнить действие. Возможно, нужно войти в систему.');
                });
        }
    });

    document.addEventListener('submit', function (e) {
        const commentForm = e.target.closest('.add-comment-form');
        if (commentForm) {
            e.preventDefault();
            const postId = commentForm.dataset.postId;
            const input = commentForm.querySelector('input[name="text"]');
            const text = input.value.trim();
            if (!text) {
                return;
            }

            fetch('/api/posts/' + postId + '/comments', {
                method: 'POST',
                headers: authHeaders({'Content-Type': 'application/json'}),
                body: JSON.stringify({text: text})
            })
                .then(function (res) {
                    if (!res.ok) {
                        throw new Error('add comment failed');
                    }
                    return res.json();
                })
                .then(function (comment) {
                    const section = commentForm.closest('.comments-section');
                    section.querySelector('.comments-list').appendChild(renderComment(comment, postId));
                    input.value = '';
                    const card = commentForm.closest('.post-card');
                    const countEl = card.querySelector('.comments-count');
                    countEl.textContent = parseInt(countEl.textContent, 10) + 1;
                })
                .catch(function () {
                    alert('Не удалось добавить комментарий');
                });
            return;
        }

        const createForm = e.target.closest('#create-post-form');
        if (createForm) {
            e.preventDefault();
            const errorBox = document.getElementById('create-post-error');
            errorBox.hidden = true;
            const formData = new FormData(createForm);

            fetch('/api/posts', {
                method: 'POST',
                headers: authHeaders(),
                body: formData
            })
                .then(function (res) {
                    if (!res.ok) {
                        return res.json().then(function (err) {
                            throw err;
                        });
                    }
                    return res.json();
                })
                .then(function () {
                    window.location.href = '/';
                })
                .catch(function (err) {
                    errorBox.textContent = (err && err.message) || 'Не удалось создать публикацию';
                    errorBox.hidden = false;
                });
        }
    });
})();
