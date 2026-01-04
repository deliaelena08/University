package com.ppdm.backend.post;

import com.ppdm.backend.post.dto.PostDto;
import com.ppdm.backend.user.UserMapper;
import com.ppdm.backend.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Creează o postare, presupunând că photoUrl a fost deja încărcat pe Front-end.
     * @param description Descrierea postării.
     * @param photoUrl URL-ul pozei (încărcată de Front-end).
     * @param location Locația postării.
     * @param user Utilizatorul care creează postarea.
     * @return PostDto salvat.
     */
    public PostDto createPost(String description, String photoUrl, String location, UserDto user) {
        var post = new PostEntity();
        post.setDescription(description);
        post.setUser(userMapper.dtoToEntity(user));
        post.setCreatedAt(LocalDateTime.now());

        if (photoUrl != null && !photoUrl.trim().isEmpty()) {
            post.setPhotoUrl(photoUrl);
        }

        if (location != null && !location.trim().isEmpty()) {
            post.setLocation(location);
        }

        var saved = postRepository.save(post);

        Map<String, Object> message = Map.of(
                "type", "CREATE",
                "payload", postMapper.entityToDto(saved)
        );

        simpMessagingTemplate.convertAndSend(
                "/ws/posts",
                message
        );

        return postMapper.entityToDto(saved);
    }

    public List<PostDto> getPostsByUserId(Long userId) {
        var posts = postRepository.getPostsByUserIdOrderByCreatedAtDesc(userId);
        return postMapper.entityToDtoList(posts);
    }

    public PostDto getPostByIdForUser(Long postId, Long userId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }
        return postMapper.entityToDto(post);
    }

    public PostDto updatePostForUser(Long id, PostDto dto, Long userId) {
        var existing = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!existing.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        existing.setDescription(dto.getDescription());
        existing.setPhotoUrl(dto.getPhotoUrl());
        existing.setLocation(dto.getLocation());
        var updated = postRepository.save(existing);

        Map<String, Object> message = Map.of(
                "type", "UPDATE",
                "payload", postMapper.entityToDto(updated)
        );

        simpMessagingTemplate.convertAndSend(
                "/ws/posts",
                message
        );

        return postMapper.entityToDto(updated);
    }

    public void deletePostForUser(Long id, Long userId) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        postRepository.delete(post);

        Map<String, Object> message = Map.of(
                "type", "DELETE",
                "payload", postMapper.entityToDto(post)
        );

        simpMessagingTemplate.convertAndSend(
                "/ws/posts",
                message
        );
    }
}