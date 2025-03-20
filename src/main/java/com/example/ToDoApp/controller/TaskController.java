package com.example.ToDoApp.controller;

import com.example.ToDoApp.model.Task;
import com.example.ToDoApp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksForUser(@PathVariable int userId){
        try {
            List<Task> tasks = taskService.getTasksForUser(userId);
            if (tasks.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tasks);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(null);
        }

    }
    @PostMapping("/user/{userId}")
    public ResponseEntity<List<Task>> addTaskForUser(@PathVariable int userId , @RequestBody Task task){
        try {
            taskService.addTaskToUser(userId , task);
            return ResponseEntity.ok().build();
        }
        catch (Exception e ){
            return ResponseEntity.internalServerError().build();
        }
    }

}