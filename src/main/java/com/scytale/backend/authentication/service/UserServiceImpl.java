package com.scytale.backend.authentication.service;

import com.scytale.backend.authentication.model.ERole;
import com.scytale.backend.authentication.model.Role;
import com.scytale.backend.authentication.model.User;
import com.scytale.backend.authentication.repo.RoleRepo;
import com.scytale.backend.authentication.repo.UserRepo;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User new_user = userRepo.save(user);
        return addRoleToUser(new_user.getUsername(), ERole.ROLE_USER);
    }

    @Override
    public Role saveRole(Role role) {
        if (roleRepo.existsByName(role.getName())) {
            return roleRepo.findByName(role.getName());
        }
        return roleRepo.save(role);

    }

    @Override
    public Boolean userExist(String username) {
        return userRepo.existsByUsername(username);
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

    @Override
    public boolean existRole(ERole role) {
        return roleRepo.existsByName(role);
    }

    @Override
    public void deleteUser(String userName) {
        userRepo.deleteUserByUsername(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in database");

        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName().name())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

}
