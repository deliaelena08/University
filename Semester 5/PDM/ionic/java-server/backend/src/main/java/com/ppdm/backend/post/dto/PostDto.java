package com.ppdm.backend.post.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {
    private Long id;
    private Long userId;
    private String description;
    private String photoUrl;
    private LocalDateTime createdAt;
    private String location;
}
