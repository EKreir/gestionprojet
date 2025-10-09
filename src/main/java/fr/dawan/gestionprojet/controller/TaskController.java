package fr.dawan.gestionprojet.controller;


import fr.dawan.gestionprojet.DTO.TaskDTO;
import fr.dawan.gestionprojet.model.enums.TaskStatus;
import fr.dawan.gestionprojet.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
//@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

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

    /*

    Annotation @PatchMapping :

    Met à jour partiellement la ressource
    Peut contenir seulement les champs à modifier
    Si un champ est absent -> il n’est pas modifié
    ex : PATCH /users/1 avec { "age": 26 } -> ne change que l’âge

    Exemple :

    Imaginons un ProjectDTO :

    {
      "id": 1,
      "name": "Site Web",
      "description": "Développement front-end",
      "status": "IN_PROGRESS"
    }

    Requête PUT :

    PUT /api/projects/1

    Content-Type: application/json

    {
      "id": 1,
      "name": "Site Web v2",
      "description": "Développement front-end",
      "status": "IN_PROGRESS"
    }

    Ici, tous les champs doivent être envoyés.
    Si on oublie description, elle risque d’être mise à null.

    Requête PATCH :

    PATCH /api/projects/1

    Content-Type: application/json

    {
      "status": "DONE"
    }

    Ici, seul le champ status est modifié.
    Les autres champs restent inchangés.

    Résumé des 3 annotations de modifications et créations Spring:

    | **Action**             | **Annotation Spring** | **Type de mise à jour** | **Données envoyées**      |
    | ---------------------- | --------------------- | ----------------------- | ------------------------- |
    | Créer                  | `@PostMapping`        | Nouvelle ressource      | Objet complet             |
    | Remplacer              | `@PutMapping`         | Remplacement total      | Objet complet             |
    | Modifier partiellement | `@PatchMapping`       | Mise à jour partielle   | Champs modifiés seulement |

    */

}