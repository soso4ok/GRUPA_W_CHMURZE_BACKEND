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
        if (users.isPresent()) {
            User user = users.get();
            if (user.getTasks() != null && !user.getTasks().isEmpty()) {
                for (int i = 0; i < user.getTasks().size(); i++) {
                    if (user.getTasks().get(i).getId().equals(updatedTask.getId())) {
                        user.getTasks().set(i, updatedTask);
                    }
                }
                userRepository.save(user);
            } else {
                throw new Exception("User has no tasks to update");
            }
        } else {
            throw new Exception("User not found");
        }
    }


}
