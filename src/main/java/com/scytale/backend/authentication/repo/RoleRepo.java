package com.scytale.backend.authentication.repo;

import com.scytale.backend.authentication.model.ERole;
import com.scytale.backend.authentication.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Role findByName(ERole name);

    boolean existsByName(ERole name);
}
