package com.ppdm.backend.post;

import com.ppdm.backend.post.dto.PostDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDto entityToDto(PostEntity post);
    List<PostDto> entityToDtoList(List<PostEntity> posts);
    PostEntity dtoToEntity(PostDto dto);
    List<PostEntity> dtoToEntityList(List<PostDto> dtos);
}
