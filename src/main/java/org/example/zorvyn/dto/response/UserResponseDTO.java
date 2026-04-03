package org.example.zorvyn.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.zorvyn.entity.Role;
import org.example.zorvyn.entity.UserStatus;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private Role role;
    private UserStatus status;
}