package com.example.instajava.service.impl;

import com.example.instajava.dto.response.PostResponseDto;
import com.example.instajava.exception.ResourceNotFoundException;
import com.example.instajava.models.Post;
import com.example.instajava.models.User;
import com.example.instajava.repository.PostRepository;
import com.example.instajava.service.CommentService;
import com.example.instajava.service.FileStorageService;
import com.example.instajava.service.LikeService;
import com.example.instajava.service.PostService;
import com.example.instajava.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final LikeService likeService;
    private final CommentService commentService;

    public PostServiceImpl(PostRepository postRepository,
                           UserService userService,
                           FileStorageService fileStorageService,
                           @Lazy LikeService likeService,
                           @Lazy CommentService commentService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.likeService = likeService;
        this.commentService = commentService;
    }

    @Override
    @Transactional
    public PostResponseDto createPost(MultipartFile image, String caption, Long authorId) {
        User author = userService.findById(authorId);
        String imagePath = fileStorageService.saveFile(image);

        Post post = Post.builder()
                .author(author)
                .imagePath(imagePath)
                .caption(caption != null ? caption.trim() : null)
                .build();

        Post savedPost = postRepository.save(post);
        log.info("Пользователь '{}' (id={}) создал публикацию id={}", author.getUsername(), authorId, savedPost.getId());

        return PostResponseDto.from(savedPost, 0L, 0L, false, true);
    }

    @Override
    public PostResponseDto getPostById(Long postId) {
        log.debug("Запрос публикации по id={}", postId);
        Post post = postRepository.findByIdWithAuthor(postId)
                .orElseThrow(() -> {
                    log.warn("Публикация с id={} не найдена", postId);
                    return new ResourceNotFoundException("Публикация с id " + postId + " не найдена");
                });

        return toDto(post);
    }

    @Override
    public List<PostResponseDto> getUserPosts(Long userId) {
        log.debug("Запрос публикаций пользователя id={}", userId);
        if (!userService.existsUserById(userId)) {
            log.warn("Запрос публикаций: пользователь с id={} не найден", userId);
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден");
        }

        List<Post> posts = postRepository.findAllByAuthorIdOrderByCreatedAtDesc(userId);
        log.debug("Получено {} публикаций для пользователя id={}", posts.size(), userId);
        return posts.stream().map(this::toDto).toList();
    }

    @Override
    public List<PostResponseDto> getFeed(Long currentUserId) {
        log.debug("Формирование ленты новостей для пользователя id={}", currentUserId);
        if (!userService.existsUserById(currentUserId)) {
            log.warn("Формирование ленты: пользователь с id={} не найден", currentUserId);
            throw new ResourceNotFoundException("Пользователь с id " + currentUserId + " не найден");
        }

        List<Post> feedPosts = postRepository.findFeedByUserId(currentUserId);
        log.debug("Для пользователя id={} получено {} публикаций в ленте", currentUserId, feedPosts.size());
        return feedPosts.stream().map(this::toDto).toList();
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long currentUserId) {
        Post post = getPostEntityById(postId);

        if (!post.getAuthor().getId().equals(currentUserId)) {
            log.warn("Пользователь id={} попытался удалить чужую публикацию id={}", currentUserId, postId);
            throw new AccessDeniedException("Вы можете удалять только свои публикации");
        }

        String imagePath = post.getImagePath();

        postRepository.delete(post);

        fileStorageService.deleteFile(imagePath);
        log.info("Публикация id={} успешно удалена автором id={}", postId, currentUserId);
    }

    @Override
    public long getUserPostCount(Long userId) {
        if (!userService.existsUserById(userId)) {
            log.warn("Запрос количества публикаций: пользователь с id={} не найден", userId);
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден");
        }
        long count = postRepository.countByAuthorId(userId);
        log.debug("Количество публикаций пользователя id={}: {}", userId, count);
        return count;
    }

    @Override
    public Post getPostEntityById(Long postId) {
        log.debug("Поиск сущности поста по id={}", postId);
        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    log.warn("Сущность поста с id={} не найдена", postId);
                    return new ResourceNotFoundException("Публикация с id " + postId + " не найдена");
                });
    }

    private PostResponseDto toDto(Post post) {
        long likesCount = likeService.getPostLikesCount(post.getId());
        long commentsCount = commentService.getPostCommentsCount(post.getId());

        Optional<User> currentUserOpt = userService.getCurrentUser();

        boolean isLikedByCurrentUser = currentUserOpt
                .map(cur -> likeService.isPostLikedByUser(post.getId(), cur.getId()))
                .orElse(false);

        boolean isOwner = currentUserOpt
                .map(cur -> cur.getId().equals(post.getAuthor().getId()))
                .orElse(false);

        return PostResponseDto.from(post, likesCount, commentsCount, isLikedByCurrentUser, isOwner);
    }
}