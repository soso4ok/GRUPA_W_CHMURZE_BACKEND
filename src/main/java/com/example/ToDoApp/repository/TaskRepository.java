package com.example.ToDoApp.repository;

import com.example.ToDoApp.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
}
