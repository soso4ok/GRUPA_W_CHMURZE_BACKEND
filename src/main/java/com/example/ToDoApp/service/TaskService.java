package com.example.ToDoApp.service;

import com.example.ToDoApp.model.Task;
import com.example.ToDoApp.model.User;
import com.example.ToDoApp.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final UserRepository userRepository;
    private final UserService userService;

    public TaskService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<Task> getTasksForUser(String  userId) throws Exception {
        User user = userService.getUserById(userId);
        return user.getTasks();
    }

    public void addTaskToUser(String userId, Task task) throws Exception {
        Optional<User> users = userRepository.findById(userId);
        if (users.isPresent()) {
            User user = users.get();
            user.getTasks().add(task);
            userRepository.save(user);
        }
        else {
            throw new Exception("User not found");
            }
        }


    public void deleteTaskFromUser(String userId, String taskId) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setTasks(
                    user.getTasks().stream()
                            .filter(task -> !task.getId().equals(taskId))
                            .toList()
            );
            userRepository.save(user);
        } else {
            throw new Exception("User not found");
        }
    }

    public void updateTaskForUser(String userId, Task updatedTask) throws Exception {
        Optional<User> users = userRepository.findById(userId);

        if (users.isPresent()) {
            User user = users.get();
            List<Task> tasks = user.getTasks();
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getId().equals(updatedTask.getId())) {
                    tasks.set(i, updatedTask);

                }
            }
            userRepository.save(user);
        }
        else {
            throw new Exception("User not found");
        }

    }


}
