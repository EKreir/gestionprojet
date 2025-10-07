package fr.dawan.gestionprojet.DTO;

import fr.dawan.gestionprojet.model.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus  status;
    private LocalDate  dueDate;
    private Long  projectId;
    private Long assignedUserId;
}
