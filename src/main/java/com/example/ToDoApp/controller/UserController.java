package com.example.ToDoApp.controller;

import com.example.ToDoApp.model.User;
import com.example.ToDoApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/users")
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {

    @Autowired
    private  UserService userService;



    @GetMapping("{id}")
    @Operation(summary = "getting user by id", description = "Getting user by id")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Adds a new user to the system")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
            return ResponseEntity.ok("User created successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Removes a user by ID")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting user: " + e.getMessage());
        }
    }

}