package com.ppdm.backend.user;

import com.ppdm.backend.security.controller.LoginRequest;
import com.ppdm.backend.user.dto.UserDto;
import com.ppdm.backend.user.dto.UserUpdateDto;
import com.ppdm.backend.user.exception.InvalidUserException;
import com.ppdm.backend.user.exception.LoginFaildException;
import com.ppdm.backend.user.exception.UserAlreadyExistException;
import com.ppdm.backend.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public UserDto findUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return mapper.entityToDto(user);
    }

    public List<UserDto> findAllUsers() {
        return mapper.entityToDto(userRepository.findAllByOrderById());
    }

    public UserDto saveUser(UserDto userDto) {
        UserEntity user = mapper.dtoToEntity(userDto);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException();
        }

        user.setLastActive(LocalDateTime.now());
        user.setActive(true);

        return mapper.entityToDto(userRepository.save(user));
    }

    public UserDto updateUser(Long id, UserUpdateDto userDto) {
        UserEntity user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        boolean emailChanged = !user.getUsername().equals(userDto.getUsername());

        user.setUsername(userDto.getUsername());
        user.setLastActive(LocalDateTime.now());

        if (emailChanged && userRepository.existsByUsername(user.getUsername())) {
            throw new InvalidUserException("Email already exists!");
        }

        UserEntity updatedEntity = userRepository.save(user);
        simpMessagingTemplate.convertAndSend("/ws/user-updated", mapper.userUpdateDtoFromEntity(updatedEntity));
        return mapper.entityToDto(updatedEntity);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }

    public UserDto login(LoginRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new LoginFaildException("There is no user with the email: " + request.getUsername());
        }
        if (!request.getPassword().equals(user.getPassword())) {
            throw new LoginFaildException("Wrong password");
        }

        user.setLastActive(LocalDateTime.now());
        userRepository.save(user);

        return mapper.entityToDto(user);
    }
}
