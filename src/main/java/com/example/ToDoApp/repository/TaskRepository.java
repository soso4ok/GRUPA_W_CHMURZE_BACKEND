package com.example.ToDoApp.repository;

import com.example.ToDoApp.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
