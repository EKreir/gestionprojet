package fr.dawan.gestionprojet.service.impl;

import fr.dawan.gestionprojet.DTO.ProjectDTO;
import fr.dawan.gestionprojet.DTO.TaskDTO;
import fr.dawan.gestionprojet.DTO.UserDTO;
import fr.dawan.gestionprojet.exception.ResourceNotFoundException;
import fr.dawan.gestionprojet.model.entity.Project;
import fr.dawan.gestionprojet.model.entity.Task;
import fr.dawan.gestionprojet.model.entity.User;
import fr.dawan.gestionprojet.repository.ProjectRepository;
import fr.dawan.gestionprojet.repository.TaskRepository;
import fr.dawan.gestionprojet.repository.UserRepository;
import fr.dawan.gestionprojet.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> findAll() {

        return projectRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO findById(Long id) {
        Project p = projectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Project not find", id));
        return toDto(p);
    }

    @Override
    public ProjectDTO create(ProjectDTO dto) {
        Project p = new Project();
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setStartDate(dto.getStartDate());
        p.setEndDate(dto.getEndDate());
        Project saved = projectRepository.save(p);
        return toDto(saved);
    }

    @Override
    public ProjectDTO update(Long id, ProjectDTO dto) {
        Project p = projectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Project not find", id));
        if (dto.getName() != null) p.setName(dto.getName());
        if (dto.getDescription() != null) p.setDescription(dto.getDescription());
        if (dto.getStartDate() != null) p.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) p.setEndDate(dto.getEndDate());
        Project saved = projectRepository.save(p);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found", id);
        }
        projectRepository.deleteById(id); // ✅ suppression réelle
    }

    @Override
    @Transactional
    public ProjectDTO addMember(Long projectId, Long userId) {
        Project p = projectRepository.findById(projectId).orElseThrow(()->new ResourceNotFoundException("Project not find", projectId));
        User u = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not find", userId));
        if (p.getMembers() == null) p.setMembers(new HashSet<>());
        p.getMembers().add(u);
        Project saved = projectRepository.save(p);
        return toDto(saved);
    }

    @Override
    @Transactional
    public ProjectDTO removeMember(Long projectId, Long userId) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found", projectId));
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", userId));

        if (p.getMembers() != null) {
            p.getMembers().remove(u); // supprime réellement
        }

        Project saved = projectRepository.save(p);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<UserDTO> getMembers(Long projectId) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found", projectId));

        if (p.getMembers() == null) return new HashSet<>();

        return p.getMembers().stream()
                .map(u -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(u.getId());
                    dto.setUsername(u.getUsername());
                    dto.setEmail(u.getEmail());
                    if (u.getRoles() != null)
                        dto.setRoles(u.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet()));
                    return dto;
                })
                .collect(Collectors.toSet());
    }

    private TaskDTO taskToDto(Task t) {
        TaskDTO dto = new TaskDTO();
        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setDescription(t.getDescription());
        dto.setStatus(t.getStatus());
        dto.setDueDate(t.getDueDate());
        if (t.getAssignedUser() != null) dto.setAssignedUserId(t.getAssignedUser().getId());
        dto.setProjectId(t.getProject() != null ? t.getProject().getId() : null);
        return dto;
    }

    private ProjectDTO toDto(Project p) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setStartDate(p.getStartDate());
        dto.setEndDate(p.getEndDate());
        if (p.getMembers() != null)
            dto.setMemberIds(p.getMembers().stream().map(User::getId).collect(Collectors.toSet()));
        // optionel: ajout des tâches si on veut :
        // dto.tasks = p.getTasks().stream().map(this::taskToDto).collect(Collectors.toList());
        return dto;
    }

    /*

    Rôle :
    Gérer la logique métier des projets (création, mise à jour, suppression, affectation de tâches).

    Pourquoi cette logique ?

    Un projet peut être manipulé par plusieurs acteurs (admin, chef de projet).

    On doit encapsuler les règles métier ici : par exemple, un projet terminé ne peut plus recevoir de nouvelles tâches.

    Méthodes typiques :

    findAll(), findById(), create(ProjectDTO dto), update(ProjectDTO dto), delete(id)

    assignTaskToProject(projectId, taskId) (optionnel)

    */

}
