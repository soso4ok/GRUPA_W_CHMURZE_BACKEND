
package com.example.ToDoApp.service;

import com.example.ToDoApp.model.User;
import com.example.ToDoApp.repository.JsonDatabase;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final JsonDatabase jsonDatabase;

    public UserService(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public User getUserById(String userId) throws Exception {
        return jsonDatabase.readUsers().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new Exception("User with ID " + userId + " not found"));
    }

    public void saveUser(User user) throws Exception {
        jsonDatabase.writeUser(user);
    }


}
