package com.example.ToDoApp.repository;

import com.example.ToDoApp.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

@Repository
public class JsonDatabase {
    private final String FILE_PATH = "src/main/resources/database.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<User> readUsers() throws Exception {
        File file = new File(FILE_PATH);
        if (!file.exists()) return List.of();
        return objectMapper.readValue(file, new TypeReference<List<User>>() {});
    }

    public void writeUser(User user) throws Exception {
        objectMapper.writeValue(new File(FILE_PATH), user);
    }
}