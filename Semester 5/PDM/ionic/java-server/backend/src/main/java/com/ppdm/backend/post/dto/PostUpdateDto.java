package com.ppdm.backend.post.dto;

import lombok.Data;

@Data
public class PostUpdateDto {
    private String description;
    private String photoUrl;
    private String location;
}
