package com.example.ToDoApp.controller;

import com.example.ToDoApp.model.User;
import com.example.ToDoApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/users")

public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        User user = null;
        try {
            user = userService.getUserById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping
//    public User createUser(@RequestBody User user){
//        return userService.createUser(user);
//    }


}