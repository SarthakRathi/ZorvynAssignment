package org.example.zorvyn.repository;

import org.example.zorvyn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // We will need this later for Spring Security and login
    Optional<User> findByUsername(String username);
}