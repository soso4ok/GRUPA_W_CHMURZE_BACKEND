package com.example.ToDoApp.service;

import com.example.ToDoApp.model.Task;
import com.example.ToDoApp.model.User;
import com.example.ToDoApp.repository.TaskRepository;
import com.example.ToDoApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final TaskRepository taskRepository;

    public List<Task> getTasksForUser(String userId) throws Exception {
        User user = userService.getUserById(userId);
        return user.getTasks() != null ? user.getTasks() : new ArrayList<>();
    }

    public void addTaskToUser(String userId, Task task) throws Exception {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new Exception("User not found");
        }
        User user = userOpt.get();

        if (user.getTasks() == null) {
            user.setTasks(new ArrayList<>());
        }

        boolean existsInUser = user.getTasks()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(t -> t.getId().equals(task.getId()));

        if (existsInUser) {
            throw new IllegalArgumentException(
                    "Task with ID " + task.getId() + " already exists for this user!"
            );
        }

        Task savedTask = taskRepository.save(task);
        user.getTasks().add(savedTask);
        userRepository.save(user);
    }


    public void deleteTaskFromUser(String userId, String taskId) throws Exception {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new Exception("User not found");
        }

        User user = userOpt.get();
        boolean removed = user.getTasks().removeIf(task -> task != null && task.getId().equals(taskId));

        if (!removed) {
            throw new Exception("Task not found for user");
        }

        userRepository.save(user);
    }


    public void updateTaskForUser(String userId, Task updatedTask) throws Exception {
        Optional<User> users = userRepository.findById(userId);
        if (users.isEmpty()) {
            throw new Exception("User not found");
        }

        User user = users.get();
        if (user.getTasks() == null || user.getTasks().isEmpty()) {
            throw new Exception("User has no tasks to update");
        }

        boolean found = false;
        for (Task task : user.getTasks()) {
            if (task.getId().equals(updatedTask.getId())) {
                found = true;
                break;
            }
        }

        if (!found) {
            throw new Exception("Task not found for this user");
        }

        taskRepository.save(updatedTask);

        userRepository.save(user);

    }
}
