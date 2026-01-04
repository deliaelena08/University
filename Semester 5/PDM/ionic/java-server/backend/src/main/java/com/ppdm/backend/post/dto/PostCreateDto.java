package com.ppdm.backend.post.dto;

import lombok.Data;

@Data
public class PostCreateDto {
    private Long userId;
    private String description;
    private String photoUrl;
    private String location;
}