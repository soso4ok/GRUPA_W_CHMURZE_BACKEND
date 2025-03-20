package com.example.ToDoApp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {
    private int id;
    private String email;
    private String password;
    private List<Task> tasks; // User's tasks
}
