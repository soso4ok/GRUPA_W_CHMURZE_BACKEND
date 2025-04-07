package com.example.ToDoApp.controller;

import com.example.ToDoApp.model.Task;
import com.example.ToDoApp.repository.UserRepository;
import com.example.ToDoApp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "getting task for user by user id", description = "These endpoint allows to  highlight tasks for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Tasks found successfully"),
            @ApiResponse(responseCode = "204" , description = "No tasks found for these user"),
            @ApiResponse(responseCode = "500" , description = "Internal server error")
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Tasks added successfully"),
            @ApiResponse(responseCode = "500" , description = "failed to add task")
    })
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
    @Operation(summary = "deleting user" , description = "Deleting user from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "User deleted successfully"),
            @ApiResponse(responseCode = "500" , description = "Failed to delete task")
    })
    public ResponseEntity<String> DeleteUserTask(@PathVariable String userId, @PathVariable String taskId){
        try {
        taskService.deleteTaskFromUser(taskId , userId);
        return ResponseEntity.ok("Task deleted sucessfully");
    }catch (Exception e){
            return ResponseEntity.internalServerError().body("Error deleting task: " + e.getMessage());
        }
    }
    @PutMapping("/{userid}/tasks")
    @Operation(summary = "Updating task for user" , description = "Updating task for particular user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "user task updated"),
            @ApiResponse(responseCode = "500", description = "failed to update task")
    })
    public ResponseEntity<String> updateUserTask(@PathVariable String userId, @RequestBody Task updatedTask){
        try {
            taskService.updateTaskForUser(userId , updatedTask);
            return ResponseEntity.ok("Task updated Ok");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Error updating task");
        }

    }

}