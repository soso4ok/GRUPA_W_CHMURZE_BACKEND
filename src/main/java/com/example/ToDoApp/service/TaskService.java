package com.example.ToDoApp.service;

import com.example.ToDoApp.model.Task;
import com.example.ToDoApp.model.User;
import com.example.ToDoApp.repository.JsonDatabase;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class TaskService {

    private final JsonDatabase jsonDatabase;
    private final UserService userService;

    public TaskService(JsonDatabase jsonDatabase, UserService userService) {
        this.jsonDatabase = jsonDatabase;
        this.userService = userService;
    }

    public List<Task> getTasksForUser(int userId) throws Exception {
        User user = userService.getUserById(userId);
        return user.getTasks();
    }

    public void addTaskToUser(int userId, Task task) throws Exception {
        List<User> users = jsonDatabase.readUsers();
        for (User user : users) {
            if (user.getId() == userId) {
                user.getTasks().add(task);
                break;
            }
        }
        jsonDatabase.writeUsers(users);
    }

    public void deleteTaskFromUser(int userId, int taskId) throws Exception {
        List<User> users = jsonDatabase.readUsers();
        for (User user : users) {
            if (user.getId() == userId) {
                user.setTasks(
                        user.getTasks().stream()
                                .filter(task -> task.getId() != taskId)
                                .collect(Collectors.toList())
                );
                break;
            }
        }
        jsonDatabase.writeUsers(users);
    }

    public void updateTaskForUser(int userId, Task updatedTask) throws Exception {
        List<User> users = jsonDatabase.readUsers();
        for (User user : users) {
            if (user.getId() == userId) {
                List<Task> tasks = user.getTasks();
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getId() == updatedTask.getId()) {
                        tasks.set(i, updatedTask);
                        break;
                    }
                }
                break;
            }
        }
        jsonDatabase.writeUsers(users);
    }

}
