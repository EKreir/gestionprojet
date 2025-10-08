package fr.dawan.gestionprojet.controller;


import fr.dawan.gestionprojet.DTO.TaskDTO;
import fr.dawan.gestionprojet.DTO.UserDTO;
import fr.dawan.gestionprojet.model.entity.Task;
import fr.dawan.gestionprojet.model.enums.TaskStatus;
import fr.dawan.gestionprojet.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTask() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskDTO>> getTaskByProject(@PathVariable Long projectId){
        return ResponseEntity.ok(taskService.findByProjectId(projectId));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO dto) {
        TaskDTO created = taskService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO dto) {
        return ResponseEntity.ok(taskService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<TaskDTO> assignTaskToUser(@PathVariable Long taskId, @PathVariable Long userId){
        return ResponseEntity.ok(taskService.assignTask(taskId,userId));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskDTO> changeTaskStatus(@PathVariable Long taskId, @RequestParam TaskStatus status){
        return ResponseEntity.ok((taskService.changeStatus(taskId, status)));
    }
}