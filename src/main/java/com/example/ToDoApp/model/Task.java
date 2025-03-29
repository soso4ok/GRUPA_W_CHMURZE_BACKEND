package com.example.ToDoApp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("Tasks")
public class Task {
    @Id
    private String id;
    private String title;
    private boolean completed;
}
