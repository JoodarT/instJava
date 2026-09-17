package com.example.instajava.service.impl;

import com.example.instajava.dto.request.PostCreateRequestDto;
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
    public PostResponseDto createPost(PostCreateRequestDto request) {
        User currentUser = userService.getRequiredCurrentUser();
        return createPost(request.getImage(), request.getCaption(), currentUser.getId());
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
        log.info("Пользователь '{}' создал публикацию id={}", author.getUsername(), savedPost.getId());

        return PostResponseDto.from(savedPost, 0L, 0L, false, true);
    }

    @Override
    public PostResponseDto getPostById(Long postId) {
        log.debug("Запрос публикации id={}", postId);
        Post post = postRepository.findByIdWithAuthor(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Публикация с id " + postId + " не найдена"));

        return toDto(post);
    }

    @Override
    public List<PostResponseDto> getUserPosts(Long userId) {
        if (!userService.existsUserById(userId)) {
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return postRepository.findAllByAuthorIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<PostResponseDto> getUserPosts(String username) {
        User user = userService.findByUsername(username);
        return getUserPosts(user.getId());
    }

    @Override
    public List<PostResponseDto> getFeed() {
        User currentUser = userService.getRequiredCurrentUser();
        return getFeed(currentUser.getId());
    }

    @Override
    public List<PostResponseDto> getFeed(Long currentUserId) {
        if (!userService.existsUserById(currentUserId)) {
            throw new ResourceNotFoundException("Пользователь с id " + currentUserId + " не найден");
        }
        log.debug("Формирование ленты новостей для id={}", currentUserId);
        return postRepository.findFeedByUserId(currentUserId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        User currentUser = userService.getRequiredCurrentUser();
        deletePost(postId, currentUser.getId());
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long currentUserId) {
        Post post = getPostEntityById(postId);

        if (!post.getAuthor().getId().equals(currentUserId)) {
            log.warn("Пользователь id={} попытался удалить чужой пост id={}", currentUserId, postId);
            throw new AccessDeniedException("Вы можете удалять только свои публикации");
        }

        String imagePath = post.getImagePath();
        postRepository.delete(post);
        fileStorageService.deleteFile(imagePath);
        log.info("Публикация id={} удалена автором id={}", postId, currentUserId);
    }

    @Override
    public long getUserPostCount(Long userId) {
        if (!userService.existsUserById(userId)) {
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return postRepository.countByAuthorId(userId);
    }

    @Override
    public Post getPostEntityById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Публикация с id " + postId + " не найдена"));
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