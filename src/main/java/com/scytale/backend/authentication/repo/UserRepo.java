package com.scytale.backend.authentication.repo;

import com.scytale.backend.authentication.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User , Long> {
    User findByUsername(String username);
    Boolean existsByUsername(String username);

    void deleteUserByUsername(String userName);
}
