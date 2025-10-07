package fr.dawan.gestionprojet.service;

import fr.dawan.gestionprojet.DTO.TaskDTO;
import fr.dawan.gestionprojet.model.enums.TaskStatus;

import java.util.List;

public interface TaskService {

    List<TaskDTO> findAll();
    TaskDTO findById(Long id);
    TaskDTO create(TaskDTO dto);
    TaskDTO update(Long id, TaskDTO dto);
    void delete(Long id);

    List<TaskDTO> findByProjectId(Long projectId);
    TaskDTO assignTask(Long taskId, Long userId);
    TaskDTO changeStatus(Long taskId, TaskStatus status);

}
