package org.example.zorvyn.dto.request;

import lombok.Data;
import org.example.zorvyn.entity.Role;
import org.example.zorvyn.entity.UserStatus;

@Data
public class UserUpdateRequestDTO {
    private Role role;
    private UserStatus status;
}