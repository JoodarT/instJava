package com.example.instajava.service.impl;

import com.example.instajava.dto.response.CommentResponseDto;
import com.example.instajava.exception.ResourceNotFoundException;
import com.example.instajava.models.Comment;
import com.example.instajava.models.Post;
import com.example.instajava.models.User;
import com.example.instajava.repository.CommentRepository;
import com.example.instajava.service.CommentService;
import com.example.instajava.service.PostService;
import com.example.instajava.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    @Override
    @Transactional
    public CommentResponseDto addComment(Long postId, Long userId, String text) {
        if (text == null || text.isBlank()) {
            log.warn("Попытка добавления пустого комментария к посту id={} пользователем id={}", postId, userId);
            throw new IllegalArgumentException("Текст комментария не может быть пустым");
        }

        User author = userService.findById(userId);
        Post post = postService.getPostEntityById(postId);

        Comment comment = Comment.builder()
                .post(post)
                .author(author)
                .text(text.trim())
                .build();

        Comment savedComment = commentRepository.save(comment);
        log.info("Пользователь '{}' оставил комментарий id={} к посту id={}", author.getUsername(), savedComment.getId(), postId);

        boolean canDelete = post.getAuthor().getId().equals(userId);
        return CommentResponseDto.from(savedComment, canDelete);
    }

    @Override
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        log.debug("Запрос комментариев для публикации id={}", postId);
        Post post = postService.getPostEntityById(postId);
        Optional<User> currentUserOpt = userService.getCurrentUser();

        boolean isPostAuthor = currentUserOpt
                .map(u -> u.getId().equals(post.getAuthor().getId()))
                .orElse(false);

        List<CommentResponseDto> comments = commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(comment -> CommentResponseDto.from(comment, isPostAuthor))
                .toList();

        log.debug("Для публикации id={} получено {} комментариев", postId, comments.size());
        return comments;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long currentUserId) {
        Comment comment = commentRepository.findByIdWithPostAndAuthor(commentId)
                .orElseThrow(() -> {
                    log.warn("Комментарий с id={} не найден для удаления", commentId);
                    return new ResourceNotFoundException("Комментарий с id " + commentId + " не найден");
                });

        Long postAuthorId = comment.getPost().getAuthor().getId();
        if (!postAuthorId.equals(currentUserId)) {
            log.warn("Пользователь id={} попытался удалить комментарий id={} под чужим постом id={}",
                    currentUserId, commentId, comment.getPost().getId());
            throw new AccessDeniedException("Вы можете удалять комментарии только под своими публикациями");
        }

        commentRepository.delete(comment);
        log.info("Комментарий id={} успешно удален автором публикации id={}", commentId, currentUserId);
    }

    @Override
    public long getPostCommentsCount(Long postId) {
        long count = commentRepository.countByPostId(postId);
        log.debug("Количество комментариев для публикации id={}: {}", postId, count);
        return count;
    }
}