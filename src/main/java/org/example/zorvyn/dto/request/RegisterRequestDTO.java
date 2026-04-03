package org.example.zorvyn.dto.request;

import lombok.Data;
import org.example.zorvyn.entity.Role;

@Data
public class RegisterRequestDTO {
    private String username;
    private String password;
    private Role role; // VIEWER, ANALYST, or ADMIN
}