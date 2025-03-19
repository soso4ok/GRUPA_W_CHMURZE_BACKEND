package com.example.ToDoApp.service;

import com.example.ToDoApp.model.Task;
import com.example.ToDoApp.model.User;
import com.example.ToDoApp.repository.JsonDatabase;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final JsonDatabase jsonDatabase;

    public TaskService(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public List<Task> getTasksForUser(String userId) throws Exception {
        List<User> users = jsonDatabase.readUsers();
        Optional<User> user = users.stream().filter(u -> u.getId().equals(userId)).findFirst();
        return user.map(User::getTasks).orElse(List.of());
    }

    public void addTaskToUser(String userId, Task task) throws Exception {
        List<User> users = jsonDatabase.readUsers();
        users.stream().filter(u -> u.getId().equals(userId)).findFirst()
                .ifPresent(user -> user.getTasks().add(task));
        jsonDatabase.writeUsers(users);
    }
}
