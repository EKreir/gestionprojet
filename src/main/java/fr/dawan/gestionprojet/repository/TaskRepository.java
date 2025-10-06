package fr.dawan.gestionprojet.repository;

import fr.dawan.gestionprojet.model.entity.Task;
import fr.dawan.gestionprojet.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssignedUserId(Long userId);
    List<Task> findByStatus(TaskStatus status);
}
