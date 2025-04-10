package com.example.ToDoApp.service;

import com.example.ToDoApp.model.Task;
import com.example.ToDoApp.model.User;
import com.example.ToDoApp.repository.TaskRepository;
import com.example.ToDoApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        // Save the task first to generate an ID and store it in the Tasks collection
        Task savedTask = taskRepository.save(task);

        Optional<User> users = userRepository.findById(userId);
        if (users.isPresent()) {
            User user = users.get();
            // The constructor ensures tasks is initialized, but this check adds robustness
            if (user.getTasks() == null) {
                user.setTasks(new ArrayList<>());
            }
            user.getTasks().add(savedTask);
            userRepository.save(user);
        } else {
            throw new Exception("User not found");
        }
    }


    public void deleteTaskFromUser(String userId, String taskId) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getTasks() != null && !user.getTasks().isEmpty()) {
                user.setTasks(
                        user.getTasks().stream()
                                .filter(task -> !task.getId().equals(taskId))
                                .toList()
                );
                userRepository.save(user);
            }
        } else {
            throw new Exception("User not found");
        }
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
