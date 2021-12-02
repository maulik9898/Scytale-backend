package com.scytale.backend.authentication.service;

import com.scytale.backend.authentication.model.ERole;
import com.scytale.backend.authentication.model.Role;
import com.scytale.backend.authentication.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    Role saveRole(Role role);

    User addRoleToUser(String username , ERole name);

    User getUser(String username);

    List<User> getUsers();


}