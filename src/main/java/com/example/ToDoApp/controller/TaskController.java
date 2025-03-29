package com.example.ToDoApp.controller;

import com.example.ToDoApp.model.Task;
import com.example.ToDoApp.repository.UserRepository;
import com.example.ToDoApp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
@Tag(name = "Task management", description = "Endpoints for Tasks for user")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    @Operation(summary = "getting task for user by user id", description = "Allows these endpoint to  highlight tasks for user")
    public ResponseEntity<List<Task>> getTasksForUser(@PathVariable String userId){
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
    @Operation(summary = "Adding task for user", description = "These endpoint allows to add task to user")
    @PostMapping("/user/{userId}")
    public ResponseEntity<List<Task>> addTaskForUser(@PathVariable String userId , @RequestBody Task task){
        try {
            taskService.addTaskToUser(userId , task);
            return ResponseEntity.ok().build();
        }
        catch (Exception e ){
            return ResponseEntity.internalServerError().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> DeleteUserTask(@PathVariable String userId, @PathVariable String taskId){
        try {
        taskService.deleteTaskFromUser(taskId , userId);
        return ResponseEntity.ok("Task deleted sucessfully");
    }catch (Exception e){
            return ResponseEntity.internalServerError().body("Error deleting task: " + e.getMessage());
        }
    }
    @PutMapping("/{userid}/tasks")
    public ResponseEntity<String> updateUserTask(@PathVariable String userId, @RequestBody Task updatedTask){
        try {
            taskService.updateTaskForUser(userId , updatedTask);
            return ResponseEntity.ok("Task updated Ok");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Error updating task");
        }

    }

}