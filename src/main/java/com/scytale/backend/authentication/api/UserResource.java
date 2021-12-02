package com.scytale.backend.authentication.api;

import com.scytale.backend.authentication.model.User;
import com.scytale.backend.authentication.service.UserService;
import com.scytale.backend.authentication.service.UserServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserResource {
    private final UserService userService;

    @GetMapping("/users")
    ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());

    }

    @GetMapping("/user/save")
    ResponseEntity<User> saveUser(@RequestBody User user){
        return ResponseEntity.ok().body(userService.saveUser(user));

    }

}
