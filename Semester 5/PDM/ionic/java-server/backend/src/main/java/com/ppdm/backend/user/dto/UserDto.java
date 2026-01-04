package com.ppdm.backend.user.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private LocalDateTime lastActive;
    private Boolean active;
}
