package fr.dawan.gestionprojet.service.impl;

import fr.dawan.gestionprojet.DTO.TaskDTO;
import fr.dawan.gestionprojet.exception.ResourceNotFoundException;
import fr.dawan.gestionprojet.model.entity.Project;
import fr.dawan.gestionprojet.model.entity.Task;
import fr.dawan.gestionprojet.model.entity.User;
import fr.dawan.gestionprojet.model.enums.TaskStatus;
import fr.dawan.gestionprojet.repository.ProjectRepository;
import fr.dawan.gestionprojet.repository.TaskRepository;
import fr.dawan.gestionprojet.repository.UserRepository;
import fr.dawan.gestionprojet.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;


    @Override
    public List<TaskDTO> findAll() {
        return taskRepository.findAll().stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public TaskDTO findById(Long id) {
        Task t = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found", id));
        return toDto(t);
    }

    @Override
    public TaskDTO create(TaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : TaskStatus.TODO);
        task.setDueDate(dto.getDueDate());

        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project", dto.getProjectId()));
            task.setProject(project);
        } else {
            throw new IllegalArgumentException("projectId is required to create a Task");
        }

        if (dto.getAssignedUserId() != null) {
            User u = userRepository.findById(dto.getAssignedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", dto.getAssignedUserId()));
            task.setAssignedUser(u);
        }

        Task saved = taskRepository.save(task);
        return toDto(saved);
    }

    @Override
    public TaskDTO update(Long id, TaskDTO dto) {
        Task t = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));

        if (dto.getTitle() != null) t.setTitle(dto.getTitle());
        if (dto.getDescription() != null) t.setDescription(dto.getDescription());
        if (dto.getStatus() != null) t.setStatus(dto.getStatus());
        if (dto.getDueDate() != null) t.setDueDate(dto.getDueDate());

        if (dto.getAssignedUserId() != null) {
            User u = userRepository.findById(dto.getAssignedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", dto.getAssignedUserId()));
            t.setAssignedUser(u);
        } else {
            t.setAssignedUser(null);
        }

        Task saved = taskRepository.save(t);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found", id);
        }
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDTO> findByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public TaskDTO assignTask(Long taskId, Long userId) {
        Task t = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        t.setAssignedUser(user);
        return toDto(taskRepository.save(t));
    }

    @Override
    public TaskDTO changeStatus(Long taskId, TaskStatus status) {
        Task t = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));
        t.setStatus(status);
        return toDto(taskRepository.save(t));
    }

    // ----- mapping helpers -----
    private TaskDTO toDto(Task t) {
        TaskDTO dto = new TaskDTO();
        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setDescription(t.getDescription());
        dto.setStatus(t.getStatus());
        dto.setDueDate(t.getDueDate());
        if (t.getProject() != null) dto.setProjectId(t.getProject().getId());
        if (t.getAssignedUser() != null) dto.setAssignedUserId(t.getAssignedUser().getId());
        return dto;
    }

    /*

    Rôle :
    Gérer les tâches : leur création, leur statut, leur affectation à un utilisateur et à un projet.

    Pourquoi cette logique ?

    Une tâche doit être créée dans le cadre d’un projet.

    Son statut évolue (TODO , IN_PROGRESS , DONE)

    Le service encapsule ces transitions et les règles associées.

    Méthodes typiques :

    create(TaskDTO dto)

    updateStatus(Long id, TaskStatus newStatus)

    assignToUser(Long taskId, Long userId)

    delete(Long id)

    En résumé :
    Ce service permet de contrôler le cycle de vie des tâches et de s’assurer qu’elles restent cohérentes avec leur projet.

    */

}
