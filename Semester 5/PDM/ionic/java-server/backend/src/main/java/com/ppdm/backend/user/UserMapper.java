package com.ppdm.backend.user;

import com.ppdm.backend.user.dto.UserDto;
import com.ppdm.backend.user.dto.UserUpdateDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto entityToDto(UserEntity userEntity);

    List<UserDto> entityToDto(List<UserEntity> userEntities);

    UserEntity dtoToEntity(UserDto dto);

    UserUpdateDto userUpdateDtoFromEntity(UserEntity user);
}
