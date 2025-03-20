package com.example.ToDoApp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private int id;
    private String title;
    private boolean completed;
}
