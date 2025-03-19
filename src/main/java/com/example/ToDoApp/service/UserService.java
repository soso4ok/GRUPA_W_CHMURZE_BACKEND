
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

    public List<User> getAllUsers() throws Exception {
        return jsonDatabase.readUsers();
    }

    public void saveUsers(List<User> users) throws Exception {
        jsonDatabase.writeUsers(users);
    }
}
