
package com.example.ToDoApp.service;

import com.example.ToDoApp.model.User;
import com.example.ToDoApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public User getUserById(String userId) throws Exception {
        Optional<User> users = userRepository.findById(userId);
        return users.orElseThrow(() -> new Exception("User with ID " + userId + " not found"));
    }

    public void saveUser(User user) throws Exception {
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("User with email " + user.getEmail() + " already exists");
        }
        userRepository.save(user);
    }

    public void deleteUser(String userId) throws Exception {
        Optional<User> users = userRepository.findById(userId);
        users.orElseThrow(() -> new Exception("User with ID " + userId + " not found"));
        userRepository.deleteById(userId);
    }
}
