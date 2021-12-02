package com.scytale.backend.authentication.service;

import com.scytale.backend.authentication.model.ERole;
import com.scytale.backend.authentication.model.Role;
import com.scytale.backend.authentication.model.User;
import com.scytale.backend.authentication.repo.RoleRepo;
import com.scytale.backend.authentication.repo.UserRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public User saveUser(User user) {
        User new_user = userRepo.save(user);
        return addRoleToUser(new_user.getUsername(),ERole.ROLE_USER);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public User addRoleToUser(String username, ERole name) {
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(name);
        user.getRoles().add(role);
        return user;
    }

    @Override
    public User getUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }
}
