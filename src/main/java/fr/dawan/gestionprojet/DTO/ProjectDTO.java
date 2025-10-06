package fr.dawan.gestionprojet.DTO;

import java.time.LocalDate;
import java.util.Set;

public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<Long>  memberIds;
    // private Long taskIds;
}
