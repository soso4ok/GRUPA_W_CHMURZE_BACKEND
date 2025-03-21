
package com.example.ToDoApp.service;

import com.example.ToDoApp.model.User;
import com.example.ToDoApp.repository.JsonDatabase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final JsonDatabase jsonDatabase;

    public UserService(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public User getUserById(int userId) throws Exception {
        return jsonDatabase.readUsers().stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new Exception("User with ID " + userId + " not found"));
    }

    public void saveUser(User user) throws Exception {
        List<User> users = jsonDatabase.readUsers();
        // Optionally, check if user already exists
        users.add(user);
        jsonDatabase.writeUsers(users);
    }

    public void deleteUser(int userId) throws Exception {
        List<User> users = jsonDatabase.readUsers();
        users.removeIf(user -> user.getId() == userId);
        jsonDatabase.writeUsers(users);
    }
}
