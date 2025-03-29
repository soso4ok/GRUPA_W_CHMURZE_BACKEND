package com.example.ToDoApp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter

@Document("Users")
public class User {

    @Id
    private String id;

    private String email;
    private String password;
    @DBRef
    private List<Task> tasks; // User's tasks
}
